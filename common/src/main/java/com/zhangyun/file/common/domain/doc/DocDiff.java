package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.domain.doc.old.DocumentDiff;
import com.zhangyun.file.common.enums.DocumentDiffTypeEnum;
import com.zhangyun.file.common.enums.DocumentTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Data
@Slf4j
@Builder
public class DocDiff {
    private DocIdentity docIdentity;
    private DocumentDiffTypeEnum diffTypeEnum;

    public static DocDiff of(Doc old, Doc newDoc) {
        DocDiff docDiff = null;
        if (old == null && newDoc != null) {
            docDiff = DocDiff.builder()
                    .diffTypeEnum(DocumentDiffTypeEnum.CREATE)
                    .docIdentity(newDoc.getDocIdentity().deepCopy())
                    .build();
        } else if (newDoc == null && old != null) {
            docDiff = DocDiff.builder()
                    .diffTypeEnum(DocumentDiffTypeEnum.DELETE)
                    .docIdentity(old.getDocIdentity().deepCopy())
                    .build();
        } else if (newDoc != null && old != null) {
            // 文档一致性校验
            if (!Objects.equals(old.getDocIdentity(), newDoc.getDocIdentity())) {
                log.error("文档对比错误，两者不为同一个文档, old:{}, new:{}", old, newDoc);
                throw new RuntimeException("文档对比错误，两者不为同一个文档");
            }
            if (!Objects.equals(old.getDocProperty().getLastModifyTime(), newDoc.getDocProperty().getLastModifyTime())) {
                docDiff = DocDiff.builder()
                        .diffTypeEnum(DocumentDiffTypeEnum.CHANGE)
                        .docIdentity(old.getDocIdentity().deepCopy())
                        .build();
            }
        }
        return docDiff;
    }
}
