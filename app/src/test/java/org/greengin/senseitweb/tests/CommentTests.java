package org.greengin.senseitweb.tests;

import com.jayway.jsonpath.JsonPath;
import org.greengin.senseitweb.entities.projects.ProjectType;
import org.greengin.senseitweb.entities.rating.Comment;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.logic.rating.CommentRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet-tests.xml")
public class CommentTests extends TestsBase {


    Long threadId;
    UserProfile author;
    UserProfile user1;
    UserProfile user2;
    UserProfile user3;


    private ProjectActions actions(UserProfile user) {
        return projectActions(threadId, user);
    }

    private List<Comment> commentAction(UserProfile user, String comment) {
        CommentRequest request = new CommentRequest();
        request.setComment(comment);
        return actions(user).comment(request);
    }

    private List<Comment> deleteAction(UserProfile user, int index) {
        Comment c = helper.getProject(threadId).getComments().get(index);
        return actions(user).deleteComment(c.getId());
    }


    private void testCount(int count) {
        assertEquals(count, helper.getProject(threadId).getComments().size());
    }

    private void testIs(int index, UserProfile commenter, String comment) {
        assertTrue(helper.getProject(threadId).getComments().size() > index);
        Comment c = helper.getProject(threadId).getComments().get(index);
        assertEquals(commenter, c.getUser());
        assertEquals(comment, c.getComment());
    }

    private void testExists(Long id, boolean exists) {
        EntityManager em = context.createEntityManager();
        Comment c = em.find(Comment.class, id);
        if (exists) {
            assertNotNull(c);
        } else {
            assertNull(c);
        }
    }

    @Before
    public void before() {
        super.before();
        author = helper.createUser("author");
        threadId = helper.createProject(author, "project", ProjectType.CHALLENGE).getId();
        user1 = helper.createUser("user1");
        user2 = helper.createUser("user2");
        user3 = helper.createUser("user3");

        actions(author).setOpen(true);
        actions(user1).join();
        actions(user2).join();
        actions(author).setOpen(false);

    }

    @Test
    public void testNoComments() {
        assertNotNull(helper.getProject(threadId).getComments());
        assertEquals(0, helper.getProject(threadId).getComments().size());
    }

    @Test
    public void testCommentCreation() {
        CommentRequest request = new CommentRequest();
        request.setComment("comment1");

        Comment comment = context.getCommentManager().comment(user1, helper.getProject(threadId), request);
        assertNotNull(comment);
        testCount(1);
        testExists(comment.getId(), true);

        request.setComment("comment2");
        Comment comment2 = context.getCommentManager().comment(user2, helper.getProject(threadId), request);
        assertNotNull(comment2);
        testCount(2);
        testExists(comment2.getId(), true);
    }

    @Test
    public void testCommentDelete() {
        CommentRequest request = new CommentRequest();

        request.setComment("comment1");
        Comment comment1 = context.getCommentManager().comment(user1, helper.getProject(threadId), request);
        Long c1 = comment1.getId();

        request.setComment("comment2");
        Comment comment2 = context.getCommentManager().comment(user2, helper.getProject(threadId), request);
        Long c2 = comment2.getId();

        testCount(2);

        context.getCommentManager().deleteComment(user2, helper.getProject(threadId), c1);
        testCount(2);
        testExists(c1, true);

        context.getCommentManager().deleteComment(user1, helper.getProject(threadId), c1);
        testCount(1);
        testExists(c1, false);

        context.getCommentManager().deleteComment(user1, helper.getProject(threadId), c2);
        testCount(1);
        testExists(c2, true);

        context.getCommentManager().deleteComment(user2, helper.getProject(threadId), c2);
        testCount(0);
        testExists(c2, false);
    }

    private void testGetAction(int count, boolean open) {
        List<Comment> list = actions(author).getComments();
        assertNotNull(list);
        assertEquals(count, list.size());


        list = actions(user1).getComments();
        if (open) {
            assertNotNull(list);
            assertEquals(count, list.size());
        } else {
            assertNull(list);
        }

        list = actions(user3).getComments();
        assertNull(list);
    }

    @Test
    public void testCommentActions() {
        assertEquals(0, helper.getProject(threadId).getComments().size());

        commentAction(user1, "bad");
        testCount(0);
        testGetAction(0, false);


        commentAction(author, "a1");
        testCount(1);
        testIs(0, author, "a1");
        testGetAction(1, false);

        commentAction(user2, "bad");
        testCount(1);
        testGetAction(1, false);

        commentAction(user3, "bad");
        testCount(1);
        testGetAction(1, false);

        actions(author).setOpen(true);

        testGetAction(1, true);

        commentAction(user1, "11");
        commentAction(user1, "12");
        commentAction(user2, "21");
        testCount(4);
        testGetAction(4, true);

        commentAction(user3, "bad");
        testCount(4);
        testGetAction(4, true);

        testIs(1, user1, "11");
        testIs(2, user1, "12");
        testIs(3, user2, "21");

        deleteAction(user3, 1);
        testCount(4);
        deleteAction(user2, 1);
        testCount(4);
        deleteAction(user1, 1);
        testCount(3);
        testIs(1, user1, "12");
        testIs(2, user2, "21");

    }

    @Test
    public void testCommentController() throws Exception {
        MockMvc mockMvc = mcv();

        testCount(0);
        testGetAction(0, false);

        actions(author).setOpen(true);

        testGetAction(0, true);

        String basePath = "/api/project/" + threadId + "/comments";

        mockMvc.perform(loginGet(basePath, author, true)).andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));

        CommentRequest request = new CommentRequest();
        request.setComment("c1");

        MvcResult result = mockMvc.perform(loginPost(basePath, request, user1, true))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].comment", is("c1")))
                .andReturn();

        int id = JsonPath.read(result.getResponse().getContentAsString(), "$[0].id");

        mockMvc.perform(loginDelete(basePath + "/" + id, user2, true))
                .andExpect(status().isOk())
                .andExpect(content().string("null"));

        testCount(1);

        mockMvc.perform(loginDelete(basePath + "/" + id, user1, true))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));

        testCount(0);
    }

}
