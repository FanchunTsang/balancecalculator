package com.zengfanjun.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileUtilTest {

    @Test
    public void testInitData() {
        // Setup
        final Map<String, Double> expectedResult = new HashMap<>();

        // Run the test
        final Map<String, Double> result = FileUtil.initData("fileString");

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testReadFile() {
        assertEquals(null, FileUtil.readFile("path"));
        assertEquals("CNY 2000,USD 1000", FileUtil.readFile("E:\\test.txt"));
    }

    @Test
    public void testIsNumeric() {
        assertTrue(!FileUtil.isNumeric("str"));
        assertTrue(FileUtil.isNumeric("1"));
        assertTrue(FileUtil.isNumeric("-1"));
        assertTrue(FileUtil.isNumeric("-0.1"));
        assertTrue(FileUtil.isNumeric("0"));
        assertTrue(FileUtil.isNumeric("0.1"));
        assertTrue(!FileUtil.isNumeric("-test"));
    }
}
