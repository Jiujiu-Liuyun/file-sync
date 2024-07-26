package com.zhangyun.file.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;

@Slf4j
public class CommonTest {

    @Test
    public void test() {
        HashMap<String, String> map = new HashMap<>();
        map.remove("1");
    }
}
