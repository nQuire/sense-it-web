package org.greengin.nquireit.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.ContextBean;
import org.greengin.nquireit.logic.users.AccessLevel;
import org.greengin.nquireit.logic.project.ProjectActions;
import org.greengin.nquireit.tests.helpers.DbHelper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.charset.Charset;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public abstract class TestsBase {


    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    protected ContextBean context;

    @PersistenceContext
    protected EntityManager em;


    protected DbHelper helper;

    @Before
    public void before() {
        helper = new DbHelper(context, em);
        helper.clear();
    }

    public ProjectActions projectActions(Long projectId, UserProfile user) {
        return new ProjectActions(context, projectId, user, true);
    }

    public AccessLevel accessLevel(Long projectId, UserProfile user) {
        return context.getSubscriptionManager().getAccessLevel(helper.getProject(projectId), user);
    }

    protected MockMvc mcv() {
        return MockMvcBuilders.webAppContextSetup(wac).build();
    }
    protected MockHttpServletRequestBuilder login(MockHttpServletRequestBuilder builder, UserProfile user, boolean tokenOk) {
        MockHttpSession mockSession = new MockHttpSession(wac.getServletContext(), UUID.randomUUID().toString());
        context.getUsersManager().testLogin(user, mockSession, "token");
        return builder.session(mockSession).header("nquire-it-token", tokenOk ? "token" : "bad");
    }

    protected MockHttpServletRequestBuilder loginPost(String path, Object body, UserProfile user, boolean tokenOk) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        byte[] bytes = mapper.writeValueAsBytes(body);

        return login(post(path).contentType(APPLICATION_JSON_UTF8).content(bytes), user, tokenOk);
    }

    protected MockHttpServletRequestBuilder loginDelete(String path, UserProfile user, boolean tokenOk) throws Exception {
        return login(delete(path), user, tokenOk);
    }

    protected MockHttpServletRequestBuilder loginGet(String path, UserProfile user, boolean tokenOk) throws Exception {
        return login(get(path), user, tokenOk);
    }

}
