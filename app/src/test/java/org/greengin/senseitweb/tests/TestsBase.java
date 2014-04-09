package org.greengin.senseitweb.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.greengin.senseitweb.entities.users.UserProfile;
import org.greengin.senseitweb.logic.ContextBean;
import org.greengin.senseitweb.logic.permissions.AccessLevel;
import org.greengin.senseitweb.logic.project.ProjectActions;
import org.greengin.senseitweb.tests.helpers.DbHelper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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


    protected DbHelper helper;

    @Before
    public void before() {
        helper = new DbHelper(context);
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
        mockSession.setAttribute("openid", user.getName());
        mockSession.setAttribute("token", "token");

        return builder.session(mockSession).header("token", tokenOk ? "token" : "bad");
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
