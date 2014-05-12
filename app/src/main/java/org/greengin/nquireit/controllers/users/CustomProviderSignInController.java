package org.greengin.nquireit.controllers.users;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;

import javax.inject.Inject;

/**
 * Created by evilfer on 4/10/14.
 */
public class CustomProviderSignInController extends ProviderSignInController implements InitializingBean {

    @Getter
    @Setter
    private String applicationUrlBase;

    @Inject
    public CustomProviderSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) {
        super(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        super.setApplicationUrl(applicationUrlBase);
    }


}
