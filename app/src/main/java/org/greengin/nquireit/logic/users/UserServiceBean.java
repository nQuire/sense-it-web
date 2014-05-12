package org.greengin.nquireit.logic.users;

import org.greengin.nquireit.entities.users.UserProfile;
import org.greengin.nquireit.logic.persistence.CustomEntityManagerFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.social.connect.Connection;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;


public class UserServiceBean implements UserDetailsService, InitializingBean {

    static final String AUTHORITY_QUERY = "SELECT userId FROM UserConnection WHERE providerId = ? AND providerUserId = ?";

    private final static String USER_QUERY = "SELECT u from UserProfile u WHERE LOWER(u.username)=LOWER(:username)";

    @Autowired
    CustomEntityManagerFactoryBean customEntityManagerFactory;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @Autowired
    RememberMeServices rememberMeServices;

    @Autowired
    PasswordEncoder passwordEncoder;


    SecureRandom random;

    @Override
    public UserProfile loadUserByUsername(String s) throws UsernameNotFoundException {
        TypedQuery<UserProfile> query = customEntityManagerFactory.createEntityManager().createQuery(USER_QUERY, UserProfile.class);
        query.setParameter("username", s);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            throw new UsernameNotFoundException(s);
        }
    }

    private UserProfile loadUserByProviderUserId(String providerId, String id) {

        EntityManager em = customEntityManagerFactory.createEntityManager();
        Query query = em.createNativeQuery(AUTHORITY_QUERY);

        query.setParameter(1, providerId);
        query.setParameter(2, id);
        try {
            em.getTransaction().begin();
            Object obj = query.getSingleResult();
            em.getTransaction().commit();
            return loadUserByUsername((String) obj);
        } catch (NoResultException e) {
            em.getTransaction().rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private StatusResponse createResponse(Authentication auth, HashMap<String, Connection<?>> connections, HttpSession session) {
        StatusResponse result = new StatusResponse();
        result.getConnections().clear();

        if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile) {
            result.setLogged(true);
            result.setProfile((UserProfile) auth.getPrincipal());
            result.setToken((String) session.getAttribute("nquire-it-token"));

            for (Map.Entry<String, Connection<?>> entry : connections.entrySet()) {
                if (entry.getValue() != null) {
                    StatusConnectionResponse scr = new StatusConnectionResponse();
                    scr.setProvider(entry.getKey());
                    scr.setProviderProfileUrl(entry.getValue().getProfileUrl());
                    result.getConnections().put(entry.getKey(), scr);
                }
            }
        } else {
            result.setLogged(false);
            result.setProfile(null);
        }

        return result;
    }

    public boolean usernameIsAvailable(String username) {
        try {
            loadUserByUsername(username);
            return false;
        } catch (UsernameNotFoundException e) {
            return true;
        }
    }

    public StatusResponse status(HashMap<String, Connection<?>> connections, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return createResponse(auth, connections, session);
    }

    public UserProfile currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile ?
                (UserProfile) auth.getPrincipal() : null;
    }

    public boolean checkToken(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object attr = session.getAttribute("nquire-it-token");
        return attr != null && attr.toString().equals(request.getHeader("nquire-it-token"));
    }

    public Authentication login(UserProfile user, HttpSession session, String token) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        session.setAttribute("nquire-it-token", token);
        return auth;
    }

    public Authentication login(UserProfile user, HttpSession session) {
        return login(user, session, new BigInteger(260, random).toString(32));
    }

    public void login(UserProfile user, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = login(user, request.getSession());
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
        rememberMeServices.loginSuccess(request, response, auth);
    }

    public Boolean login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        Authentication auth;
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
            rememberMeServices.loginSuccess(request, response, auth);
            request.getSession().setAttribute("nquire-it-token", new BigInteger(260, random).toString(32));
        } catch (BadCredentialsException ex) {
            auth = null;
        }


        return auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile;
    }

    public StatusResponse logout(HashMap<String, Connection<?>> connections, HttpServletRequest request, HttpServletResponse response) {
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);

        return status(connections, request.getSession());
    }

    public UserProfile providerSignIn(String username, String providerId, String providerUserId) {
        UserProfile existingUser = loadUserByProviderUserId(providerId, providerUserId);
        if (existingUser != null) {
            return existingUser;
        } else {
            String initialUsername = username;
            for (int i = 1; !usernameIsAvailable(initialUsername); i++) {
                initialUsername = String.format("%s(%d)", username, i);
            }

            return createUser(initialUsername, null);
        }
    }


    public StatusResponse registerUser(String username, String password, HashMap<String, Connection<?>> connections, HttpServletRequest request) {
        try {
            loadUserByUsername(username);
            StatusResponse result = new StatusResponse();
            result.setLogged(false);
            result.setProfile(null);
            result.getResponses().put("registration", "username_exists");
            return result;
        } catch (UsernameNotFoundException e) {
            UserProfile user = createUser(username, password);
            login(user, request.getSession());

            return status(connections, request.getSession());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        random = new SecureRandom();
    }



    public boolean matchPassword(UserProfile user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void setPassword(UserProfile user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }

    private UserProfile createUser(String username, String password) {
        UserProfile user = new UserProfile();
        user.setUsername(username);
        user.setPassword(password != null ? passwordEncoder.encode(password) : null);

        EntityManager em = customEntityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        return user;
    }

}
