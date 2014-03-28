package org.greengin.senseitweb.tests;


import org.apache.commons.io.FileUtils;
import org.greengin.senseitweb.logic.data.FileManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet-tests.xml")
public class FileManagerTests {

    @Autowired
    FileManager fileManager;

    @Test
    public void testNotNull() {
        Assert.assertNotNull(fileManager);
    }

    @Test
    public void testPathNotNull() {
        Assert.assertNotNull(fileManager.getPath());
    }

    @Test
    public void testPathExists() {
        File path = fileManager.getPath();
        Assert.assertTrue(path.exists());
    }

    @Test
    public void testSaveFile() throws IOException {
        clearFolder();
        Assert.assertEquals(0, fileManager.getPath().listFiles().length);
        saveDummyFile();

        Assert.assertEquals(1, fileManager.getPath().listFiles().length);
        File context = fileManager.getPath().listFiles()[0];
        Assert.assertEquals("0", context.getName());
        Assert.assertTrue(context.isDirectory());

        Assert.assertEquals(1, context.listFiles().length);
        File file = context.listFiles()[0];
        Assert.assertEquals("test", file.getName());
        Assert.assertEquals(4, file.length());
    }

    private void saveDummyFile() throws IOException {
        byte[] data = new byte[] {0, 0, 0, 0};
        fileManager.uploadFile("0", "test", new ByteArrayInputStream(data));
    }

    private void clearFolder() throws IOException {
        File path = fileManager.getPath();
        FileUtils.cleanDirectory(path);
    }

}

