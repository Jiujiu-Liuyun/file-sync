package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.domain.doc.old.DocIdentityV1;
import com.zhangyun.file.common.enums.DocTypeEnum;
import org.junit.Test;

import java.util.HashSet;
import java.util.Objects;

public class DocIdentityV1Test {
    @Test
    public void test1() {
        DocIdentityV1 doc1 = DocIdentityV1.builder().name("1").typeEnum(DocTypeEnum.FILE).build();
        DocIdentityV1 doc2 = DocIdentityV1.builder().name("1").typeEnum(DocTypeEnum.FILE).build();
        DocIdentityV1 doc3 = DocIdentityV1.builder().name("1").typeEnum(DocTypeEnum.DIR).build();
        System.out.println(Objects.equals(doc1, doc2));
        HashSet<DocIdentityV1> set1 = new HashSet<>();
        set1.add(doc1);
        HashSet<DocIdentityV1> set2 = new HashSet<>();
        set2.add(doc2);
        set2.add(doc3);
        set2.removeAll(set1);
        System.out.println(set2);
    }
}