/*
 * Copy of org.springframework.social.connect.web.ProviderSignInController
 * Provides more informative error handling.
 */
package org.greengin.nquireit.controllers.users;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.GenericTypeResolver;
import org.springframework.social.ApiException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.*;
import org.springframework.social.support.URIBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

@RequestMapping("/signin")
public class CustomProviderSignInController2 implements InitializingBean {
    private final static Log logger = LogFactory.getLog(CustomProviderSignInController2.class);

    private final ConnectionFactoryLocator connectionFactoryLocator;

    private final UsersConnectionRepository usersConnectionRepository;

    private final MultiValueMap<Class<?>, ProviderSignInInterceptor<?>> signInInterceptors = new LinkedMultiValueMap<Class<?>, ProviderSignInInterceptor<?>>();

    private final SignInAdapter signInAdapter;



    private String signInUrl = "/signin";

    private String signUpUrl = "/signup";

    private String postSignInUrl = "/";

    private ConnectSupport connectSupport;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Setter
    private String applicationUrlBase;


    /**
     * Creates a new provider sign-in controller.
     * @param connectionFactoryLocator the locator of {@link ConnectionFactory connection factories} used to support provider sign-in.
     * Note: this reference should be a serializable proxy to a singleton-scoped target instance.
     * This is because {@link ProviderSignInAttempt} are session-scoped objects that hold ConnectionFactoryLocator references.
     * If these references cannot be serialized, NotSerializableExceptions can occur at runtime.
     * @param usersConnectionRepository the global store for service provider connections across all users.
     * Note: this reference should be a serializable proxy to a singleton-scoped target instance.
     * @param signInAdapter handles user sign-in
     */
    @Inject
    public CustomProviderSignInController2(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) {
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.usersConnectionRepository = usersConnectionRepository;
        this.signInAdapter = signInAdapter;
    }

    /**
     * Configure the list of sign in interceptors that should receive callbacks during the sign in process.
     * Convenient when an instance of this class is configured using a tool that supports JavaBeans-based configuration.
     * @param interceptors the sign in interceptors to add
     */
    public void setSignInInterceptors(List<ProviderSignInInterceptor<?>> interceptors) {
        for (ProviderSignInInterceptor<?> interceptor : interceptors) {
            addSignInInterceptor(interceptor);
        }
    }

    /**
     * Sets the URL of the application's sign in page.
     * Defaults to "/signin".
     * @param signInUrl the signIn URL
     */
    public void setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
    }

    /**
     * Sets the URL to redirect the user to if no local user account can be mapped when signing in using a provider.
     * Defaults to "/signup".
     * @param signUpUrl the signUp URL
     */
    public void setSignUpUrl(String signUpUrl) {
        this.signUpUrl = signUpUrl;
    }

    /**
     * Sets the default URL to redirect the user to after signing in using a provider.
     * Defaults to "/".
     * @param postSignInUrl the postSignIn URL
     */
    public void setPostSignInUrl(String postSignInUrl) {
        this.postSignInUrl = postSignInUrl;
    }

    /**
     * Configures the base secure URL for the application this controller is being used in e.g. <code>https://myapp.com</code>. Defaults to null.
     * If specified, will be used to generate OAuth callback URLs.
     * If not specified, OAuth callback URLs are generated from web request info.
     * You may wish to set this property if requests into your application flow through a proxy to your application server.
     * In this case, the request URI may contain a scheme, host, and/or port value that points to an internal server not appropriate for an external callback URL.
     * If you have this problem, you can set this property to the base external URL for your application and it will be used to construct the callback URL instead.
     * @param applicationUrl the application URL value
     */
    public void setApplicationUrl(String applicationUrl) {
        connectSupport.setApplicationUrl(applicationUrl);
    }

    /**
     * Sets a strategy to use when persisting information that is to survive past the boundaries of a request.
     * The default strategy is to set the data as attributes in the HTTP Session.
     * @param sessionStrategy the session strategy.
     */
    public void setSessionStrategy(SessionStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
    }

    /**
     * Adds a ConnectInterceptor to receive callbacks during the connection process.
     * Useful for programmatic configuration.
     * @param interceptor the connect interceptor to add
     */
    public void addSignInInterceptor(ProviderSignInInterceptor<?> interceptor) {
        Class<?> serviceApiType = GenericTypeResolver.resolveTypeArgument(interceptor.getClass(), ProviderSignInInterceptor.class);
        signInInterceptors.add(serviceApiType, interceptor);
    }

    private RedirectView signInError(Exception e) {
        URIBuilder builder = URIBuilder.fromUri(signInUrl).queryParam("error", "provider");
        builder.queryParam("m", e.getMessage());
        if (e instanceof ApiException) {
            builder.queryParam("p", ((ApiException) e).getProviderId());
        }
        return redirect(builder.build().toString());
    }

    /**
     * Process a sign-in form submission by commencing the process of establishing a connection to the provider on behalf of the user.
     * For OAuth1, fetches a new request token from the provider, temporarily stores it in the session, then redirects the user to the provider's site for authentication authorization.
     * For OAuth2, redirects the user to the provider's site for authentication authorization.
     */
    @RequestMapping(value="/{providerId}", method=RequestMethod.POST)
    public RedirectView signIn(@PathVariable String providerId, NativeWebRequest request) {
        ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(providerId);
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
        preSignIn(connectionFactory, parameters, request);
        try {
            return new RedirectView(connectSupport.buildOAuthUrl(connectionFactory, request, parameters));
        } catch (Exception e) {
            return signInError(e);
            //logger.error("Exception while building authorization URL: ", e);
            //return redirect(URIBuilder.fromUri(signInUrl).queryParam("error", "provider").build().toString());
        }
    }

    /**
     * Process the authentication callback from an OAuth 1 service provider.
     * Called after the member authorizes the authentication, generally done once by having he or she click "Allow" in their web browser at the provider's site.
     * Handles the provider sign-in callback by first determining if a local user account is associated with the connected provider account.
     * If so, signs the local user in by delegating to {@link SignInAdapter#signIn(String, Connection, NativeWebRequest)}
     * If not, redirects the user to a signup page to create a new account with {@link ProviderSignInAttempt} context exposed in the HttpSession.
     * @see ProviderSignInAttempt
     * @see ProviderSignInUtils
     */
    @RequestMapping(value="/{providerId}", method=RequestMethod.GET, params="oauth_token")
    public RedirectView oauth1Callback(@PathVariable String providerId, NativeWebRequest request) {
        try {
            OAuth1ConnectionFactory<?> connectionFactory = (OAuth1ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
            Connection<?> connection = connectSupport.completeConnection(connectionFactory, request);
            return handleSignIn(connection, connectionFactory, request);
        } catch (Exception e) {
            return signInError(e);
            //logger.error("Exception while completing OAuth 1.0(a) connection: ", e);
            //return redirect(URIBuilder.fromUri(signInUrl).queryParam("error", "provider").build().toString());
        }
    }

    /**
     * Process the authentication callback from an OAuth 2 service provider.
     * Called after the user authorizes the authentication, generally done once by having he or she click "Allow" in their web browser at the provider's site.
     * Handles the provider sign-in callback by first determining if a local user account is associated with the connected provider account.
     * If so, signs the local user in by delegating to {@link SignInAdapter#signIn(String, Connection, NativeWebRequest)}.
     * If not, redirects the user to a signup page to create a new account with {@link ProviderSignInAttempt} context exposed in the HttpSession.
     * @see ProviderSignInAttempt
     * @see ProviderSignInUtils
     */
    @RequestMapping(value="/{providerId}", method=RequestMethod.GET, params="code")
    public RedirectView oauth2Callback(@PathVariable String providerId, @RequestParam("code") String code, NativeWebRequest request) {
        try {
            OAuth2ConnectionFactory<?> connectionFactory = (OAuth2ConnectionFactory<?>) connectionFactoryLocator.getConnectionFactory(providerId);
            Connection<?> connection = connectSupport.completeConnection(connectionFactory, request);
            return handleSignIn(connection, connectionFactory, request);
        } catch (Exception e) {
            return signInError(e);
            //logger.error("Exception while completing OAuth 2 connection: ", e);
            //return redirect(URIBuilder.fromUri(signInUrl).queryParam("error", "provider").build().toString());
        }
    }

    /**
     * Process an error callback from an OAuth 2 authorization as described at http://tools.ietf.org/html/rfc6749#section-4.1.2.1.
     * Called after upon redirect from an OAuth 2 provider when there is some sort of error during authorization, typically because the user denied authorization.
     * Simply carries the error parameters through to the sign-in page.
     */
    @RequestMapping(value="/{providerId}", method=RequestMethod.GET, params="error")
    public RedirectView oauth2ErrorCallback(@PathVariable String providerId,
                                            @RequestParam("error") String error,
                                            @RequestParam(value="error_description", required=false) String errorDescription,
                                            @RequestParam(value="error_uri", required=false) String errorUri,
                                            NativeWebRequest request) {
        logger.warn("Error during authorization: " + error);
        URIBuilder uriBuilder = URIBuilder.fromUri(signInUrl).queryParam("error", error);
        if (errorDescription != null ) { uriBuilder.queryParam("error_description", errorDescription); }
        if (errorUri != null ) { uriBuilder.queryParam("error_uri", errorUri); }
        return redirect(uriBuilder.build().toString());
    }

    /**
     * Process the authentication callback when neither the oauth_token or code parameter is given, likely indicating that the user denied authorization with the provider.
     * Redirects to application's sign in URL, as set in the signInUrl property.
     */
    @RequestMapping(value="/{providerId}", method=RequestMethod.GET)
    public RedirectView canceledAuthorizationCallback() {
        return redirect(signInUrl);
    }

    // From InitializingBean
    public void afterPropertiesSet() throws Exception {
        this.connectSupport = new ConnectSupport(sessionStrategy);
        this.connectSupport.setUseAuthenticateUrl(true);
    };

    // internal helpers

    private RedirectView handleSignIn(Connection<?> connection, ConnectionFactory<?> connectionFactory, NativeWebRequest request) {
        List<String> userIds = usersConnectionRepository.findUserIdsWithConnection(connection);
        if (userIds.size() == 0) {
            ProviderSignInAttempt signInAttempt = new ProviderSignInAttempt(connection, connectionFactoryLocator, usersConnectionRepository);
            sessionStrategy.setAttribute(request, ProviderSignInAttempt.SESSION_ATTRIBUTE, signInAttempt);
            return redirect(signUpUrl);
        } else if (userIds.size() == 1) {
            usersConnectionRepository.createConnectionRepository(userIds.get(0)).updateConnection(connection);
            String originalUrl = signInAdapter.signIn(userIds.get(0), connection, request);
            postSignIn(connectionFactory, connection, (WebRequest) request);
            return originalUrl != null ? redirect(originalUrl) : redirect(postSignInUrl);
        } else {
            return redirect(URIBuilder.fromUri(signInUrl).queryParam("error", "multiple_users").build().toString());
        }
    }

    private RedirectView redirect(String url) {
        return new RedirectView(url, true);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void preSignIn(ConnectionFactory<?> connectionFactory, MultiValueMap<String, String> parameters, WebRequest request) {
        for (ProviderSignInInterceptor interceptor : interceptingSignInTo(connectionFactory)) {
            interceptor.preSignIn(connectionFactory, parameters, request);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void postSignIn(ConnectionFactory<?> connectionFactory, Connection<?> connection, WebRequest request) {
        for (ProviderSignInInterceptor interceptor : interceptingSignInTo(connectionFactory)) {
            interceptor.postSignIn(connection, request);
        }
    }

    private List<ProviderSignInInterceptor<?>> interceptingSignInTo(ConnectionFactory<?> connectionFactory) {
        Class<?> serviceType = GenericTypeResolver.resolveTypeArgument(connectionFactory.getClass(), ConnectionFactory.class);
        List<ProviderSignInInterceptor<?>> typedInterceptors = signInInterceptors.get(serviceType);
        if (typedInterceptors == null) {
            typedInterceptors = Collections.emptyList();
        }
        return typedInterceptors;
    }

}
