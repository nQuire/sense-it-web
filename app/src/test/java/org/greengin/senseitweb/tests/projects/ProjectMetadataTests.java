package org.greengin.senseitweb.tests.projects;


import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.logic.project.ProjectRequest;
import org.greengin.senseitweb.tests.AbstractProjectTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet-tests.xml")
public class ProjectMetadataTests extends AbstractProjectTests {


    public ProjectMetadataTests() {
        super("challenge", ProjectType.CHALLENGE);
    }

    @Test
    public void testInitialDescription() {
        assertNotNull(project().getDescription());
    }

    @Test
    public void testEmpty() {
        assertEquals(0, project().getDescription().size());
    }

    @Test
    public void testTitleChange() {
        assertEquals("challenge", project().getTitle());

        ProjectRequest request = new ProjectRequest();
        request.setTitle("nt");
        request.setDescription(null);

        projectActions(author).updateMetadata(request, null);

        assertEquals("nt", project().getTitle());
    }

    @Test
    public void testMetadataChange() {
        assertEquals("challenge", project().getTitle());

        HashMap<String, String> metadata = new HashMap<String, String>();
        metadata.put("k", "v");

        ProjectRequest request = new ProjectRequest();
        request.setTitle(null);
        request.setDescription(metadata);

        projectActions(author).updateMetadata(request, null);

        assertEquals("challenge", project().getTitle());
        assertNotNull(project().getDescription());
        assertEquals(1, project().getDescription().size());
        assertTrue(project().getDescription().containsKey("k"));
        assertEquals("v", project().getDescription().get("k"));
    }

}
