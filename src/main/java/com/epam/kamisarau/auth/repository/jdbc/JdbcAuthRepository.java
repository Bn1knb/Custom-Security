package com.epam.kamisarau.auth.repository.jdbc;

import com.epam.kamisarau.auth.exception.DatabaseConnectionFailedException;
import com.epam.kamisarau.auth.exception.NoTokenFound;
import com.epam.kamisarau.auth.exception.NoUserFound;
import com.epam.kamisarau.auth.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@PropertySource(value = {"classpath:mysql.jdbc.request.properties"})
public class JdbcAuthRepository implements AuthRepository {
    @Value("${select.username.from.user_token}")
    private String selectUsernameFromUserTokenSqlStatement;
    @Value("${select.tokenId.from.user_token}")
    private String selectTokenIdFromUserTokenSqlStatement;
    @Value("${column.name.username}")
    private String userNameColumnName;
    @Value("${column.name.tokenId}")
    private String tokenIdColumnName;
    @Value("${insert.token.to.user}")
    private String setTokenToUserSqlStatement;
    @Value("${delete.token.of.user}")
    private String deleteTokenOfUserSqlStatement;

    private Connection connection;

    @Autowired
    public JdbcAuthRepository(DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionFailedException();
        }
    }

    @Override
    public String getUserByTokenId(Long tokenId) throws NoUserFound{
        PreparedStatement selectUsernameStatement;
        ResultSet rs;
        Optional<String> username = Optional.empty();

        try {
            selectUsernameStatement = connection.prepareStatement(selectUsernameFromUserTokenSqlStatement);
            selectUsernameStatement.setLong(1, tokenId);
            rs = selectUsernameStatement.executeQuery();
            rs.next();
            username = Optional.of(rs.getString(userNameColumnName));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return username.orElseThrow(NoUserFound::new);
    }

    @Override
    public Long getTokenIdByUser(String username) throws NoTokenFound {
        PreparedStatement selectUserIdStatement;
        ResultSet rs;
        Optional<Long> tokenId = Optional.empty();
        try {
            selectUserIdStatement = connection.prepareStatement(selectTokenIdFromUserTokenSqlStatement);
            selectUserIdStatement.setString(1, username);
            rs = selectUserIdStatement.executeQuery();
            rs.next();
            tokenId = Optional.of(rs.getLong(tokenIdColumnName));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tokenId.orElseThrow(NoTokenFound::new);
    }

    @Override
    public Long setTokenToUser(String username, Long tokenId) {
        try {
            PreparedStatement statement = connection.prepareStatement(setTokenToUserSqlStatement);
            statement.setLong(1, tokenId);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tokenId;
    }

    @Override
    public void deleteTokenOfUser(String username) {
        try {
            PreparedStatement statement = connection.prepareStatement(deleteTokenOfUserSqlStatement);
            statement.setString(1, username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
