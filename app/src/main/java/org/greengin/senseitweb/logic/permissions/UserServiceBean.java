package org.greengin.senseitweb.logic.permissions;

import org.greengin.senseitweb.entities.users.UserProfile2;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by evilfer on 4/9/14.
 */
public class UserServiceBean implements UserDetailsService, InitializingBean {

    private final static String USER_QUERY = "SELECT u from UserProfile2 u WHERE LOWER(u.username)=LOWER(:username)";

    @Autowired
    CustomEntityManagerFactoryBean customEntityManagerFactory;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @Autowired
    RememberMeServices rememberMeServices;


    @Override
    public UserProfile2 loadUserByUsername(String s) throws UsernameNotFoundException {
        TypedQuery<UserProfile2> query = customEntityManagerFactory.createEntityManager().createQuery(USER_QUERY, UserProfile2.class);
        try {
            query.setParameter("username", s);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new UsernameNotFoundException(s);
        }
    }

    private Status2Response createResponse(Authentication auth) {
        Status2Response result = new Status2Response();

        if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile2) {
            result.setLogged(true);
            result.setProfile((UserProfile2) auth.getPrincipal());
        } else {
            result.setLogged(false);
            result.setProfile(null);
        }

        return result;
    }
    public Status2Response status(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return createResponse(auth);
    }

    public Status2Response login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = null;
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
            rememberMeServices.loginSuccess(request, response, auth);
            
        } catch (BadCredentialsException ex) {
            auth = null;
        }

        return createResponse(auth);
    }

    public RegistrationResponse createUser(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        RegistrationResponse result = new RegistrationResponse();
        try {
            loadUserByUsername(username);
            result.setResponse(null);
            result.setExplanation("username exists");
        } catch (UsernameNotFoundException e) {
            UserProfile2 user = new UserProfile2();
            user.setUsername(username);
            user.setPassword(password);

            EntityManager em = customEntityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

            result.setResponse(login(username, password, request, response));
            result.setExplanation(null);
        }

        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        return;
    }
}
