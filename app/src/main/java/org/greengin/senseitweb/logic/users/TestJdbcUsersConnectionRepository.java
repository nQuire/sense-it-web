package org.greengin.senseitweb.logic.users;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import javax.sql.DataSource;

/**
 * Created by evilfer on 4/11/14.
 */
public class TestJdbcUsersConnectionRepository extends JdbcUsersConnectionRepository {

    public TestJdbcUsersConnectionRepository(DataSource dataSource, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
        super(dataSource, connectionFactoryLocator, textEncryptor);
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        ConnectionRepository r = super.createConnectionRepository(userId);
        return r;
    }


}
