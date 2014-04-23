package org.greengin.senseitweb.tests.projects;


import org.greengin.senseitweb.entities.projects.ProjectDescription;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.logic.project.metadata.ProjectRequest;
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
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext-tests.xml")
public class ProjectMetadataTests extends AbstractProjectTests {


    public ProjectMetadataTests() {
        super(ProjectType.CHALLENGE);
    }

    @Test
    public void testInitialDescription() {
        assertNotNull(project().getDescription());
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

        ProjectDescription description = new ProjectDescription();
        description.setTeaser("teaser");

        ProjectRequest request = new ProjectRequest();
        request.setTitle(null);
        request.setDescription(description);

        projectActions(author).updateMetadata(request, null);

        assertEquals("challenge", project().getTitle());
        assertNotNull(project().getDescription());
        assertEquals("teaser", project().getDescription().getTeaser());
    }

}
