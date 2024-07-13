package com.zhangyun.file.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class CommonTest {

    @Test
    public void test1() {
        long start = System.currentTimeMillis();
        try (FileWriter fileWriter = new FileWriter("test.txt")) {
            for (int i = 0; i < 1024; i++) {
                fileWriter.write(StringUtils.repeat("123\n", 1024 * 1024));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        log.info("cost: {}", (end - start) / 1000.0);
    }

    @Test
    public void test2() {
        System.out.println(Long.MAX_VALUE / 1024.0 / 1024.0 / 1024.0);
    }

    public static void main1(String[] args) {
        new Thread(() -> {
            log.info("开始时间: {}", System.currentTimeMillis());
            try {
                FileUtils.copyFile(new File("test.txt"), new File("test1.txt"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.info("结束时间: {}", System.currentTimeMillis());
        }, "复制线程").start();

        for (int i = 0; i < 10; i++) {
            log.info("当前时间: {}", System.currentTimeMillis());
        }

        new Thread(() -> {
            log.info("开始时间: {}", System.currentTimeMillis());
            File file = new File("test.txt");
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rws")) {
                randomAccessFile.seek(file.length() - 1);
                randomAccessFile.write("x".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            log.info("结束时间: {}", System.currentTimeMillis());
        }, "修改线程").start();
    }

    public static void main(String[] args) {
        File file = new File("test1.txt");
        byte[] b = new byte[10];
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            randomAccessFile.seek(file.length() - 10);
            randomAccessFile.read(b);
            System.out.println(Arrays.toString(b));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test() {
        File file = new File("test.txt");
        for (int i = 0; i < 100; i++) {
            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
                randomAccessFile.seek(file.length());
                randomAccessFile.write("1\n".getBytes(StandardCharsets.UTF_8));
                System.out.println(file.lastModified());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void test3() {
        File file = new File("test.txt");
        System.out.println(file.getAbsoluteFile().getParentFile());
    }
}
