package org.greengin.nquireit.tests;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext-tests.xml")
public class SocialConnectTests extends TestsBase {

    @Test
    public void loginPageExists() throws Exception {
        MockMvc mockMvc = mcv();

        mockMvc.perform(get("/social/facebook/login")).andExpect(status().isOk());
    }

    @Test
    public void facebookLogin() throws Exception {
        MockMvc mockMvc = mcv();

        MvcResult result = mockMvc.perform(get("/signin/facebook")).andReturn();
        String redirect = result.getResponse().getRedirectedUrl();

        System.out.println(redirect);

    }

}
