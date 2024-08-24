package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocDiffTypeEnum;
import com.zhangyun.file.common.enums.DocTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class DocDiff {
    private DocIdentity docIdentity;
    private DocDiffTypeEnum diffTypeEnum;

    public static DocDiff of(Doc docOld, Doc docNew) {
        DocDiff docDiff = null;
        if (docOld == null && docNew != null) {
            docDiff = new DocDiff(docNew.getDocIdentity().deepCopy(), DocDiffTypeEnum.CREATE);
        } else if (docNew == null && docOld != null) {
            docDiff = new DocDiff(docOld.getDocIdentity().deepCopy(), DocDiffTypeEnum.DELETE);
        } else if (docNew != null && docOld != null) {
            // 文档一致性校验
            if (!Objects.equals(docOld.getDocIdentity(), docNew.getDocIdentity())) {
                log.error("文档对比错误，两者不为同一个文档, docOld:{}, new:{}", docOld, docNew);
                throw new RuntimeException("文档对比错误，两者不为同一个文档");
            }
            if (!Objects.equals(docOld.getDocProperty().getLastModifyTime(), docNew.getDocProperty().getLastModifyTime())
                    && docOld.getDocIdentity().getDocTypeEnum() == DocTypeEnum.FILE) {
                docDiff = new DocDiff(docOld.getDocIdentity().deepCopy(), DocDiffTypeEnum.CHANGE);
            }
        }
        return docDiff;
    }
}
