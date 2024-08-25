package com.zhangyun.file.common.uilt;

import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class FileUtilTest {

    @Test
    public void getRelativePath() {
        System.out.println(FileUtil.getRelativePath(new File("C:\\Users\\26044\\IdeaProjects\\file-sync\\sync-dir\\client-1"),
                "C:\\"));
    }

    @Test
    public void getRelativePath1() {
        System.out.println(FileUtil.getRelativePath(new File("C:\\Users\\26044\\IdeaProjects\\file-sync\\sync-dir\\client-1"),
                "C:\\Users\\26044\\IdeaProjects\\file-sync\\sync-dir\\client-1"));
    }

    @Test
    public void getAbsolutePath() {
        System.out.println(FileUtil.getAbsolutePath("client-1", "C:\\Users\\26044\\IdeaProjects\\file-sync"));
    }

    @Test
    public void test_writeFile() {
        FileUtil.writeFile("hello".getBytes(StandardCharsets.UTF_8), new File("C:\\Users\\26044\\IdeaProjects\\file-sync\\sync-dir\\client-1\\test1\\123\\hello\\hello.txt"));
    }
}