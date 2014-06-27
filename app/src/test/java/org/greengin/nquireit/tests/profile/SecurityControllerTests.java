package org.greengin.nquireit.tests.profile;

import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.tests.TestsBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext-tests.xml")
public class SecurityControllerTests extends TestsBase {

    MockMvc mockMvc;

    @Before
    public void before() {
        mockMvc = mcv();
    }

    private void checkUserCount(int count) {
        EntityManager em = context.getEntityManager();
        int uc = em.createQuery("SELECT u FROM UserProfile u", UserProfile.class).getResultList().size();
        assertEquals(uc, count);
    }

    private void checkUser(int index, String username) {
        checkUser(index, username, null, null);
    }

    private void checkUser(int index, String username, String providerId, String providerUserId) {
        EntityManager em = context.getEntityManager();
        List<UserProfile> users = em.createQuery("SELECT u FROM UserProfile u", UserProfile.class).getResultList();
        assertTrue(users.size() > index);

        UserProfile user = users.get(index);
        assertEquals(user.getUsername(), username);

        if (providerId != null) {
            assertEquals(1, user.getAuthorities().size());
            assertEquals(String.format("%s:%s", providerId, providerUserId), user.getAuthorities().get(0).getAuthority());
        } else {
            assertEquals(0, user.getAuthorities().size());
        }
    }


    @Test
    public void testNotLoggedIn() throws Exception {
        checkUserCount(0);

        mockMvc.perform(get("/api/security/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.logged", is(false)));
    }

    @Test
    public void testLoggin() throws Exception {

        context.getUsersManager().providerSignIn("username", "provider", "id");

        checkUserCount(1);
        checkUser(0, "username", "provider", "id");

        mockMvc.perform(get("/api/security/status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.logged", is(true)))
                .andExpect(jsonPath("$.profile.username", is("username")));


    }


}
