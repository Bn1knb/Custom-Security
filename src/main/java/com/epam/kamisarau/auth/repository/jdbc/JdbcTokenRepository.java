package com.epam.kamisarau.auth.repository.jdbc;

import com.epam.kamisarau.auth.exception.DatabaseConnectionFailedException;
import com.epam.kamisarau.auth.exception.NoTokenFound;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;
import java.util.Optional;

@Repository
@PropertySource(value = {"classpath:mysql.jdbc.request.properties"})
public class JdbcTokenRepository implements TokenRepository {
    private Connection connection;

    @Value("${insert.into.token_storage}")
    private String insertSqlStatement;
    @Value("${select.by.tokenId}")
    private String selectByIdSqlStatement;
    @Value("${select.by.tokenValue}")
    private String selectTokenByValueSqlStatement;
    @Value("${delete.by.tokenValue}")
    private String deleteTokenByValueSqlStatement;
    @Value("${delete.by.tokenId}")
    private String deleteTokenByIdSqlStatement;
    @Value("${update.by.tokenId}")
    private String updateTokenByIdSqlStatement;

    private static final String TOKEN_COLUMN_LABEL = "TOKEN";
    private static final String EXPIRATION_DATE_COLUMN_LABEL = "ExpirationDate";
    private static final String ID_COLUMN_LABEL = "ID";


    @Autowired
    public JdbcTokenRepository(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionFailedException();
        }
    }

    @Override
    public TokenModel store(TokenModel token) {
        Timestamp timestamp = new Timestamp(token.getExpirationDate().getTime());
        Optional<TokenModel> storedToken = Optional.empty();
        try {
            PreparedStatement statement = connection.prepareStatement(insertSqlStatement);
            statement.setLong(1, token.getTokenValue());
            statement.setTimestamp(2, timestamp);
            statement.executeUpdate();
            storedToken = Optional.of(getTokenByValue(token.getTokenValue()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return storedToken.orElseThrow(NoTokenFound::new);
    }

    @Override
    public TokenModel getToken(Long tokenId) {
        Optional<TokenModel> token = Optional.empty();
        try {
            PreparedStatement statement = connection.prepareStatement(selectByIdSqlStatement);
            statement.setLong(1, tokenId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            Long tokenValue = rs.getLong(TOKEN_COLUMN_LABEL);
            Date expirationDate = rs.getDate(EXPIRATION_DATE_COLUMN_LABEL);
            Long id = rs.getLong(ID_COLUMN_LABEL);
            token = Optional
                    .of(
                            new TokenModel()
                                    .setTokenValue(tokenValue)
                                    .setExpirationDate(expirationDate)
                                    .setId(id)

                    );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token.orElseThrow(NoTokenFound::new);
    }

    @Override
    public TokenModel updateToken(Long tokenId, TokenModel newToken) {
        Optional<TokenModel> token = Optional.empty();
        Timestamp timestamp = new Timestamp(newToken.getExpirationDate().getTime());
        try {
            PreparedStatement statement = connection.prepareStatement(updateTokenByIdSqlStatement);
            statement.setLong(1, newToken.getTokenValue());
            statement.setTimestamp(2, timestamp);
            statement.executeUpdate();
            token = Optional.of(getToken(tokenId));
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO ad new exception
        }
        return token.orElseThrow(NoTokenFound::new);
    }

    @Override
    public TokenModel getTokenByValue(Long tokenValue) throws NoTokenFound {
        Optional<TokenModel> token = Optional.empty();
        try {
            PreparedStatement statement = connection.prepareStatement(selectTokenByValueSqlStatement);
            statement.setLong(1, tokenValue);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                throw new NoTokenFound();
            }

            Long storedTokenValue = rs.getLong(TOKEN_COLUMN_LABEL);
            Date expirationDate = rs.getDate(EXPIRATION_DATE_COLUMN_LABEL);
            Long id = rs.getLong(ID_COLUMN_LABEL);
            token = Optional
                    .of(
                            new TokenModel()
                                    .setTokenValue(storedTokenValue)
                                    .setExpirationDate(expirationDate)
                                    .setId(id)
                    );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return token.orElseThrow(NoTokenFound::new);
    }

    @Override
    public void deleteTokenById(Long tokenId) {
        try {
            PreparedStatement statement = connection.prepareStatement(deleteTokenByIdSqlStatement);
            statement.setLong(1, tokenId);
            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
