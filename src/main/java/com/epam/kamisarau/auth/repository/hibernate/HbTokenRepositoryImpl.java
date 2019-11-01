package com.epam.kamisarau.auth.repository.hibernate;

import com.epam.kamisarau.auth.exception.NoTokenFound;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import com.epam.kamisarau.auth.repository.TokenRepository;
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
public class HbTokenRepositoryImpl implements TokenRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public TokenModel store(TokenModel token) {

        try {
            entityManager.persist(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return getToken(token.getId());
    }

    @Override
    public TokenModel getToken(Long tokenId) {
        Optional<TokenModel> tokenModel;

        try {
            Query query = entityManager.createQuery("FROM TokenModel WHERE id = :id");
            tokenModel = Optional.of(
                    (TokenModel) query.setParameter("id", tokenId).getSingleResult()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoTokenFound();
        }
        return tokenModel.orElseThrow(NoTokenFound::new);
    }

    @Override
    public TokenModel updateToken(Long tokenId, TokenModel newToken) {
        //TODO does that really necessary?*/
        return null;
    }

    @Override
    public TokenModel getTokenByValue(String tokenValue) {
        Optional<TokenModel> tokenModel;

        try {
            Query query = entityManager.createQuery("FROM TokenModel t WHERE t.tokenValue = :tokenValue");
            tokenModel = Optional.of(
                    (TokenModel) query.setParameter("tokenValue", tokenValue).getSingleResult()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoTokenFound();
        }
        return tokenModel.orElseThrow(NoTokenFound::new);
    }

    @Override
    public void deleteTokenById(Long tokenId) {

        try {
            TokenModel tokenModel = entityManager.find(TokenModel.class, tokenId);
            entityManager.remove(tokenModel);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteTokenOfUser(UserModel user) {
        Long tokenId = user.getToken().getId();
        user.setToken(null);
        deleteTokenById(tokenId);
        entityManager.flush();
        entityManager.clear();
    }
}
