package com.zhangyun.file.common.uilt;

import com.zhangyun.file.common.domain.doc.Doc;
import com.zhangyun.file.common.domain.doc.DocDiff;
import com.zhangyun.file.common.domain.doc.DocIdentity;
import com.zhangyun.file.common.domain.doc.old.DocIdentityV1;
import com.zhangyun.file.common.domain.doc.old.Document;
import com.zhangyun.file.common.domain.doc.old.DocumentDiff;
import com.zhangyun.file.common.domain.doc.DocTree;
import com.zhangyun.file.common.enums.DocDiffTypeEnum;
import com.zhangyun.file.common.enums.DocTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class FileUtil {
    public static String getRelativePath(File file, String rootPath) {
        Path path = Paths.get(rootPath);
        Path relativize = path.relativize(file.toPath());
        return relativize.toString();
    }

    public static String getAbsolutePath(String relativePath, String rootPath) {
        return Paths.get(rootPath, relativePath).toString();
    }

    public static DocTree visitPath(File file, String rootPath) {
        if (file == null || !file.exists()) {
            return null;
        }
        Doc doc = Doc.of(file.getAbsoluteFile().toPath(), Paths.get(rootPath));
        List<DocTree> subDocTrees = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    DocTree subNode = visitPath(f, rootPath);
                    subDocTrees.add(subNode);
                }
            }
        }
        return new DocTree(doc, subDocTrees);
    }

    public static void compareDocTree(DocTree docTreeOld, DocTree docTreeNew, List<DocDiff> docDiffList) {
        if (docTreeOld == null && docTreeNew == null) {
            return;
        }
        DocDiff diff = DocDiff.of(ObjectUtil.map(docTreeOld, DocTree::getNode), ObjectUtil.map(docTreeNew, DocTree::getNode));
        if (diff != null && diff.getDiffTypeEnum() != DocDiffTypeEnum.DELETE) {
            docDiffList.add(diff);
        }
        // 对比子文档
        Map<DocIdentity, DocTree> mapOld = ObjectUtil.mapOrDefault(docTreeOld, DocTree::getSubDocIdentityMap, Collections.emptyMap());
        Map<DocIdentity, DocTree> mapNew = ObjectUtil.mapOrDefault(docTreeNew, DocTree::getSubDocIdentityMap, Collections.emptyMap());
        ListUtil.diffMap(mapOld, mapNew).values().forEach(doc -> compareDocTree(doc, null, docDiffList));
        ListUtil.diffMap(mapNew, mapOld).values().forEach(doc -> compareDocTree(null, doc, docDiffList));
        for (DocIdentity docIdentity : mapOld.keySet()) {
            if (mapNew.containsKey(docIdentity)) {
                compareDocTree(mapOld.get(docIdentity), mapNew.get(docIdentity), docDiffList);
            }
        }
        if (diff != null && diff.getDiffTypeEnum() == DocDiffTypeEnum.DELETE) {
            docDiffList.add(diff);
        }
    }

    public static Document recursionPath(File file, String rootPath) {
        if (file == null || !file.exists()) {
            return null;
        }
        Document document;
        if (file.isFile()) {
            document = Document.builder()
                    .name(file.getName())
                    .relativePath(getRelativePath(file, rootPath))
                    .typeEnum(DocTypeEnum.FILE)
                    .lastModifyTime(file.lastModified())
                    .subDocuments(new ArrayList<>()).build();
        } else {
            File[] files = file.listFiles();
            ArrayList<Document> subs = new ArrayList<>();
            if (files != null) {
                for (File f : files) {
                    Document d = recursionPath(f, rootPath);
                    subs.add(d);
                }
            }
            document = Document.builder()
                    .name(file.getName())
                    .relativePath(getRelativePath(file, rootPath))
                    .typeEnum(DocTypeEnum.DIR)
                    .lastModifyTime(file.lastModified())
                    .subDocuments(subs).build();
        }
        return document;
    }

    public static void compareDocument(Document oldDocument, Document newDocument, List<DocumentDiff> documentDiffList,
                                       boolean isHeader, Set<DocumentDiff> ignoreDocDiffSet) {
        if (oldDocument == null && newDocument == null) {
            return;
        }
        DocumentDiff diff = getDocumentDiff(oldDocument, newDocument, isHeader);
        if (diff != null && diff.getDiffTypeEnum() != DocDiffTypeEnum.DELETE) {
            if (ignoreDocDiffSet.contains(diff)) {
                log.info("文件变动为同步变动，无需记录，diff: {}", diff);
                ignoreDocDiffSet.remove(diff);
            } else {
                documentDiffList.add(diff);
            }
        }
        // 对比子文档
        Map<DocIdentityV1, Document> oldMap = oldDocument == null ? new HashMap<>() : oldDocument.getSubDocIdenMap();
        Map<DocIdentityV1, Document> newMap = newDocument == null ? new HashMap<>() : newDocument.getSubDocIdenMap();
        ListUtil.diffMap(oldMap, newMap).values().forEach(doc -> compareDocument(doc, null, documentDiffList, false, ignoreDocDiffSet));
        ListUtil.diffMap(newMap, oldMap).values().forEach(doc -> compareDocument(null, doc, documentDiffList, false, ignoreDocDiffSet));
        for (DocIdentityV1 old : oldMap.keySet()) {
            if (newMap.containsKey(old)) {
                compareDocument(oldMap.get(old), newMap.get(old), documentDiffList, false, ignoreDocDiffSet);
            }
        }
        if (diff != null && diff.getDiffTypeEnum() == DocDiffTypeEnum.DELETE) {
            if (ignoreDocDiffSet.contains(diff)) {
                log.info("文件变动为同步变动，无需记录，diff: {}", diff);
                ignoreDocDiffSet.remove(diff);
            } else {
                documentDiffList.add(diff);
            }
        }
    }

    private static DocumentDiff getDocumentDiff(Document oldDocument, Document newDocument, boolean isHeader) {
        DocumentDiff diff = null;
        if (oldDocument == null) {
            diff = new DocumentDiff(newDocument, DocDiffTypeEnum.CREATE);
        } else if (newDocument == null) {
            diff = new DocumentDiff(oldDocument, DocDiffTypeEnum.DELETE);
        } else {
            if (!isHeader) {
                // 文档一致性校验
                if (!Objects.equals(oldDocument.getDocIdentity(), newDocument.getDocIdentity())) {
                    throw new RuntimeException("文档对比错误，两者不为同一个文档");
                }
                if (!Objects.equals(newDocument.getLastModifyTime(), oldDocument.getLastModifyTime())) {
                    diff = new DocumentDiff(newDocument, DocDiffTypeEnum.CHANGE);
                }
            }
        }
        return diff;
    }

    public static byte[] readFile(File file) {
        byte[] content = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(content);
        } catch (IOException e) {
            log.error("读取文件异常, e: {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
        return content;
    }

    public static void writeFile(byte[] content, File file) {
        if (!file.getAbsoluteFile().getParentFile().exists()) {
            file.getAbsoluteFile().getParentFile().mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(content, 0, content.length);
            fileOutputStream.flush();
            log.info("写文件成功, file: {}", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeJsonStr2File(String json, File file) {
        writeFile(GsonUtil.prettyPrint(json).getBytes(StandardCharsets.UTF_8), file);
    }

    public static boolean deleteFile(File file) {
        if (!file.isFile()) {
            return false;
        }
        return file.delete();
    }

    public static boolean deleteDir(File file) {
        if (!file.isDirectory()) {
            return false;
        }
        return file.delete();
    }

    public static boolean createDir(File file) {
        if (file.exists()) {
            return false;
        }
        return file.mkdir();
    }
}
