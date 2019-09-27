package com.epam.kamisarau.auth.repository.hibernate;

import com.epam.kamisarau.auth.exception.NoTokenFound;
import com.epam.kamisarau.auth.exception.TokenNotStored;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.repository.TokenRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class HbTokenRepositoryImpl implements TokenRepository {
    private SessionFactory sessionFactory;
    private Transaction transaction;

    @Autowired
    public HbTokenRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public TokenModel store(TokenModel token) {
        Long tokenId = null;
        TokenModel tokenModel;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            tokenId = (Long) session.save(token);
            transaction.commit();
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        try {
            tokenModel = getToken(tokenId);
        } catch (NoTokenFound e) {
            throw new TokenNotStored(e.getMessage());
        }

        return tokenModel;
    }

    @Override
    public TokenModel getToken(Long tokenId) {
        Optional<TokenModel> tokenModel = Optional.empty();

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            tokenModel = Optional.of(
                    (TokenModel) session.createQuery("FROM token_storage t WHERE t.id = " + tokenId).getSingleResult()
            );
            transaction.commit();
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return tokenModel.orElseThrow(NoTokenFound::new);
    }

    @Override
    public TokenModel updateToken(Long tokenId, TokenModel newToken) {
        /*try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } //TODO does this really necessary?*/
        return null;
    }

    @Override
    public TokenModel getTokenByValue(Long tokenValue) {
        Optional<TokenModel> tokenModel = Optional.empty();

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            tokenModel = Optional.of(
                    (TokenModel) session.createQuery("FROM token_storage t WHERE t.tokenValue = " + tokenValue).getSingleResult()
            );
            transaction.commit();
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return tokenModel.orElseThrow(NoTokenFound::new);
    }

    @Override
    public void deleteTokenById(Long tokenId) {
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            TokenModel tokenModel = session.load(TokenModel.class, tokenId);
            session.delete(tokenModel);
            transaction.commit();
        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }
}
