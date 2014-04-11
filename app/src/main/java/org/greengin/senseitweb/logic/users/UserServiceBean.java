package org.greengin.senseitweb.logic.users;

import org.greengin.senseitweb.entities.users.UserProfile;
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
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;


public class UserServiceBean implements UserDetailsService, InitializingBean {

    static final String UPDATE_USER_CONNECTIONS = "UPDATE UserConnection SET userId = ? WHERE userId = ?";

    static final String AUTHORITY_QUERY = "SELECT u FROM UserProfile u JOIN u.authorities a WHERE a.providerId = :provider AND a.providerUserId = :user";

    private final static String USER_QUERY = "SELECT u from UserProfile u WHERE LOWER(u.username)=LOWER(:username)";

    @Autowired
    CustomEntityManagerFactoryBean customEntityManagerFactory;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @Autowired
    RememberMeServices rememberMeServices;

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

    public UserProfile loadUserByProviderUserId(String providerId, String id) {
        TypedQuery<UserProfile> query = customEntityManagerFactory.createEntityManager().createQuery(AUTHORITY_QUERY, UserProfile.class);
        query.setParameter("provider", providerId);
        query.setParameter("user", id);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    private StatusResponse createResponse(Authentication auth, HttpSession session) {
        StatusResponse result = new StatusResponse();

        if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof UserProfile) {
            result.setLogged(true);
            result.setProfile((UserProfile) auth.getPrincipal());
            result.setToken((String) session.getAttribute("nquire-it-token"));
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

    public StatusResponse status(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return createResponse(auth, session);
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

    public StatusResponse login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
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

        return createResponse(auth, request.getSession());
    }

    public StatusResponse logout(HttpServletRequest request, HttpServletResponse response) {
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);

        return status(request.getSession());
    }

    public boolean providerSignIn(String username, String providerId, String providerUserId) {
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

        UserProfile user = new UserProfile();
        user.setUsername(initialUsername);
        user.setPassword(null);
        user.addAuthority(providerId, providerUserId);

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
            UserProfile user = new UserProfile();
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
        random = new SecureRandom();
    }

    public ProfileUpdateResponse updateProfile(ProfileRequest data) {
        UserProfile profile = currentUser();
        if (profile == null || data.getUsername() == null) {
            return null;
        }

        ProfileUpdateResponse response = new ProfileUpdateResponse();
        response.setProfile(profile);
        response.setExplanation(null);

        if (!data.getUsername().equals(profile.getUsername())) {

            if (data.getUsername().length() == 0) {
                response.setExplanation("username_empty");
            } else if (!usernameIsAvailable(data.getUsername())) {
                response.setExplanation("username_not_available");
            } else {
                EntityManager em = customEntityManagerFactory.createEntityManager();

                em.getTransaction().begin();

                Query query = em.createNativeQuery(UPDATE_USER_CONNECTIONS);
                query.setParameter(1, data.getUsername());
                query.setParameter(2, profile.getUsername());
                query.executeUpdate();

                profile.setUsername(data.getUsername());

                em.getTransaction().commit();
            }
        }

        return response;
    }
}
