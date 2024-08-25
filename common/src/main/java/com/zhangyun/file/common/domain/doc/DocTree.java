package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocDiffTypeEnum;
import com.zhangyun.file.common.uilt.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DocTree implements Serializable {
    private Doc node;

    private List<DocTree> subNodes;

    public Map<DocIdentity, DocTree> getSubDocIdentityMap() {
        if (CollectionUtils.isEmpty(subNodes)) {
            return Collections.emptyMap();
        }
        return subNodes.stream().collect(Collectors.toMap(subNode -> subNode.getNode().getDocIdentity(), Function.identity()));
    }

    public static void updateTreeNode(DocTree root, Path relativePath, Path rootPath, DocDiffTypeEnum diffTypeEnum) {
        List<Doc> docLayers = new ArrayList<>();
        DocTree cur = root;
        List<DocTree> curSub = cur.getSubNodes();
        for (int i = 0; i < relativePath.getNameCount(); i++) {
            Doc doc = Doc.of(Paths.get(rootPath.toString(), relativePath.subpath(0, i + 1).toString()), rootPath);
            cur = curSub.stream().filter(subNode -> subNode.getNode().getDocIdentity().equals(doc.getDocIdentity())).findFirst().orElse(null);
            if (i == relativePath.getNameCount() - 1) {
                curSub.remove(cur);
                if (diffTypeEnum != DocDiffTypeEnum.DELETE) {
                    DocTree docTree = new DocTree(doc, ObjectUtil.mapOrDefault(cur, DocTree::getSubNodes, new ArrayList<>()));
                    curSub.add(docTree);
                }
                break;
            }
            curSub = cur.getSubNodes();
        }
    }

    public static void main(String[] args) {
        System.out.println(Paths.get("1", "2", "3").subpath(0, 1));
    }
}
