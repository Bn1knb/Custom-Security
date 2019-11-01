package com.epam.kamisarau.auth.repository.hibernate;

import com.epam.kamisarau.auth.exception.NoUserFound;
import com.epam.kamisarau.auth.exception.RegistrationFailedException;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbUserRepositoryImpl implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserModel getUser(Long userId) throws NoUserFound {
        Optional<UserModel> user;

        try {
            Query query = entityManager.createQuery("FROM UserModel WHERE id = :id");
            user = Optional.of(
                    (UserModel) query.setParameter("id", userId).getSingleResult()
            );
        } catch (Throwable e) {
            e.printStackTrace();
            throw new NoUserFound();
        }

        return user.orElseThrow(NoUserFound::new);
    }

    @Override
    public UserModel getUserByUsername(String username) throws NoUserFound {
        Optional<UserModel> user;

        try {
            Query query = entityManager.createQuery("FROM UserModel WHERE username = :username");
            user = Optional.of(
                    (UserModel) query.setParameter("username", username).getSingleResult()
            );
        } catch (Throwable e) {
            e.printStackTrace();
            throw new NoUserFound();
        }
        return user.orElseThrow(NoUserFound::new);
    }


    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public UserModel register(UserModel user) throws RegistrationFailedException {
        UserModel mergedUser;
        try {
            //TODO resolve problem
            mergedUser = entityManager.merge(user);
            entityManager.persist(mergedUser);
        } catch (Throwable e) {
            e.printStackTrace();
            //todo specify what fails
            throw new RegistrationFailedException();
        }

        return mergedUser;
    }

    @Override
    public UserModel updateUser(UserModel user) {
        entityManager.merge(user);
        entityManager.flush();
        entityManager.clear();
        return user;
    }

    @Override
    public UserModel getUserByTokenId(Long tokenId) throws NoUserFound {
        Optional<UserModel> user;

        try {
            Query query = entityManager.createQuery("FROM UserModel WHERE token.id = :id");
            user = Optional.of(
                    (UserModel) query.setParameter("id", tokenId).getSingleResult()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoUserFound();
        }
        return user.orElseThrow(NoUserFound::new);
    }
}
