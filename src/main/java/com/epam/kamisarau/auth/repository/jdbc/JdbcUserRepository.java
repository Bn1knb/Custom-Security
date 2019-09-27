package com.epam.kamisarau.auth.repository.jdbc;

import com.epam.kamisarau.auth.exception.DatabaseConnectionFailedException;
import com.epam.kamisarau.auth.exception.NoUserFound;
import com.epam.kamisarau.auth.exception.RegistrationFailedException;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {
    private Connection connection;

    @Value("${insert.into.user_storage}")
    private String insertSqlStatement;
    @Value("${select.by.userId}")
    private String selectByIdSqlStatement;
    @Value("${select.by.username}")
    private String selectByUsernameSqlStatement;

    private static final String REGISTERED_AT_COLUMN_LABEL = "REGISTERED_AT";
    private static final String USERNAME_COLUMN_LABEL = "USERNAME";
    private static final String PASSWORD_COLUMN_LABEL = "PASSWORD";
    private static final String NAME_COLUMN_LABEL = "NAME";
    private static final String SURNAME_COLUMN_LABEL = "SURNAME";
    private static final String IS_ACTIVE_COLUMN_LABEL = "IS_ACTIVE";
    private static final String ID_COLUMN_LABEL = "ID";

    @Autowired
    public JdbcUserRepository(DataSource dataSource) {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionFailedException();
        }
    }

    @Override
    public UserModel getUserByTokenId(Long tokenId) {
        return null;
    }

    @Override
    public UserModel getUser(Long userId) {
        Optional<UserModel> user = Optional.empty();
        try {
            PreparedStatement statement = connection.prepareStatement(selectByIdSqlStatement);
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            Date registeredAt = rs.getDate(REGISTERED_AT_COLUMN_LABEL);
            String username = rs.getString(USERNAME_COLUMN_LABEL);
            String password = rs.getString(PASSWORD_COLUMN_LABEL);
            String name = rs.getString(NAME_COLUMN_LABEL);
            String surname = rs.getString(SURNAME_COLUMN_LABEL);
            boolean isActive = rs.getBoolean(IS_ACTIVE_COLUMN_LABEL);
            user = Optional
                    .of(
                            new UserModel()
                                    .setId(userId)
                                    .setUsername(username)
                                    .setPassword(password)
                                    .setRegisteredAt(registeredAt)
                                    .setName(name)
                                    .setSurname(surname)
                                    .setActive(isActive)

                    );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user.orElseThrow(NoUserFound::new);
    }

    @Override
    public UserModel getUserByUsername(String username) {
        Optional<UserModel> user = Optional.empty();
        try {
            PreparedStatement statement = connection.prepareStatement(selectByUsernameSqlStatement);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            rs.next();
            Long id = rs.getLong(ID_COLUMN_LABEL);
            Date registeredAt = rs.getDate(REGISTERED_AT_COLUMN_LABEL);
            String password = rs.getString(PASSWORD_COLUMN_LABEL);
            String name = rs.getString(NAME_COLUMN_LABEL);
            String surname = rs.getString(SURNAME_COLUMN_LABEL);
            boolean isActive = rs.getBoolean(IS_ACTIVE_COLUMN_LABEL);
            user = Optional
                    .of(
                            new UserModel()
                                    .setId(id)
                                    .setUsername(username)
                                    .setPassword(password)
                                    .setRegisteredAt(registeredAt)
                                    .setName(name)
                                    .setSurname(surname)
                                    .setActive(isActive)

                    );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user.orElseThrow(NoUserFound::new);
    }

    @Override
    public UserModel register(UserModel user) {
        Optional<UserModel> registeredUser;
        Timestamp timestamp = new Timestamp(user.getRegisteredAt().getTime());

        try {
            PreparedStatement statement = connection.prepareStatement(insertSqlStatement);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setTimestamp(3, timestamp);
            statement.setString(4, user.getName());
            statement.setString(5, user.getSurname());
            statement.setBoolean(6, user.isActive());
            statement.executeUpdate();
            registeredUser = Optional.of(
                    user//TODO change
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RegistrationFailedException();
        }
        return registeredUser.orElseThrow(NoUserFound::new);
    }
}
