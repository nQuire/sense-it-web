package org.greengin.nquireit.tests.projects;


import org.greengin.nquireit.entities.projects.ProjectMetadata;
import org.greengin.nquireit.entities.projects.ProjectType;
import org.greengin.nquireit.logic.project.metadata.ProjectRequest;
import org.greengin.nquireit.tests.AbstractProjectTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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
        assertNotNull(project().getMetadata());
    }


    @Test
    public void testTitleChange() {
        assertEquals("challenge", project().getTitle());

        ProjectRequest request = new ProjectRequest();
        request.setTitle("nt");
        request.setMetadata(null);

        projectActions(author).updateMetadata(request, null);

        assertEquals("nt", project().getTitle());
    }

    @Test
    public void testMetadataChange() {
        assertEquals("challenge", project().getTitle());

        ProjectMetadata metadata = new ProjectMetadata();
        metadata.setTeaser("teaser");

        ProjectRequest request = new ProjectRequest();
        request.setTitle(null);
        request.setMetadata(metadata);

        projectActions(author).updateMetadata(request, null);

        assertEquals("challenge", project().getTitle());
        assertNotNull(project().getMetadata());
        assertEquals("teaser", project().getMetadata().getTeaser());
    }

}
