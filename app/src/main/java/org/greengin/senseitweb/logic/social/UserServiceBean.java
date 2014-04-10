package org.greengin.senseitweb.logic.social;

import org.greengin.senseitweb.entities.users.UserProfile2;
import org.greengin.senseitweb.logic.permissions.RegistrationResponse;
import org.greengin.senseitweb.logic.permissions.Status2Response;
import org.greengin.senseitweb.logic.persistence.CustomEntityManagerFactoryBean;
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


public class UserServiceBean implements UserDetailsService {

    static final String AUTHORITY_QUERY = "SELECT u FROM UserProfile2 u JOIN u.authorities a WHERE a.authority = :authority";

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
        query.setParameter("username", s);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            throw new UsernameNotFoundException(s);
        }
    }

    public UserProfile2 loadUserByProviderUserId(String providerId, String id) {
        TypedQuery<UserProfile2> query = customEntityManagerFactory.createEntityManager().createQuery(AUTHORITY_QUERY, UserProfile2.class);
        query.setParameter("authority", String.format("%s:%s", providerId, id));
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
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

    private boolean usernameIsAvailable(String username) {
        try {
            loadUserByUsername(username);
            return false;
        } catch (UsernameNotFoundException e) {
            return true;
        }
    }

    public Status2Response status() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return createResponse(auth);
    }

    public void login(UserProfile2 user, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
        rememberMeServices.loginSuccess(request, response, auth);
    }

    public Status2Response login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth;
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

    public boolean createUser(String username, String providerId, String providerUserId) {
        String id = String.format("%s:%s", providerId, providerUserId);


        String initialUsername;

        if (usernameIsAvailable(username)) {
            initialUsername = username;
        } else {
            initialUsername = id;
            for (int i = 1; !usernameIsAvailable(initialUsername); i++) {
                initialUsername = String.format("%s(%d)", id, i);
            }
        }

        UserProfile2 user = new UserProfile2();
        user.setUsername(initialUsername);
        user.setPassword(null);
        user.addAuthority(id);

        EntityManager em = customEntityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        return true;

    }

    public RegistrationResponse registerUser(String username, String password, HttpServletRequest request, HttpServletResponse response) {
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

}
