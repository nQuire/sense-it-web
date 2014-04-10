package org.greengin.senseitweb.tests;

import org.greengin.senseitweb.logic.project.senseit.FileMapUpload;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext-tests.xml")
public class FileMapUploadTests {

    FileMapUpload fu;

    @Before
    public void before() {
        fu = new FileMapUpload();
    }

    @Test
    public void testHasMap() {
        assertNotNull(fu.getData());
    }

    @Test
    public void testAddFile() {
        fu.add("key", "filename", System.in);

        Map<String, FileMapUpload.FileData> data = fu.getData();
        assertEquals(1, data.size());

        assertTrue(data.containsKey("key"));
        assertEquals("filename", data.get("key").filename);
        assertEquals(System.in, data.get("key").data);
    }

}
