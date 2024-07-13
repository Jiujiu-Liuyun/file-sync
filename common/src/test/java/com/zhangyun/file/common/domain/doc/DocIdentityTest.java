package com.zhangyun.file.common.domain.doc;

import com.zhangyun.file.common.enums.DocumentTypeEnum;
import org.junit.Test;

import java.util.HashSet;
import java.util.Objects;

import static org.junit.Assert.*;

public class DocIdentityTest {
    @Test
    public void test1() {
        DocIdentity doc1 = DocIdentity.builder().name("1").typeEnum(DocumentTypeEnum.FILE).build();
        DocIdentity doc2 = DocIdentity.builder().name("1").typeEnum(DocumentTypeEnum.FILE).build();
        DocIdentity doc3 = DocIdentity.builder().name("1").typeEnum(DocumentTypeEnum.DIR).build();
        System.out.println(Objects.equals(doc1, doc2));
        HashSet<DocIdentity> set1 = new HashSet<>();
        set1.add(doc1);
        HashSet<DocIdentity> set2 = new HashSet<>();
        set2.add(doc2);
        set2.add(doc3);
        set2.removeAll(set1);
        System.out.println(set2);
    }
}