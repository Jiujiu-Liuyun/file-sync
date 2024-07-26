package com.zhangyun.file.client.util;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class DocCompareVisitorTest {

    @Test
    public void test() throws IOException {
        Files.walkFileTree(Paths.get("C:\\Users\\26044\\IdeaProjects\\file-sync\\sync-dir\\client-1"), new DocCompareVisitor());
    }

}