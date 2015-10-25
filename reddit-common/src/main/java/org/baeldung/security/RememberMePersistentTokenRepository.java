package org.baeldung.security;

import java.util.Date;
import java.util.List;

import org.baeldung.persistence.dao.RememberMeTokenRepository;
import org.baeldung.persistence.model.RememberMeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class RememberMePersistentTokenRepository implements PersistentTokenRepository {

    @Autowired
    private RememberMeTokenRepository rememberMeTokenRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        System.out.println("creating new token");
        final RememberMeToken newToken = new RememberMeToken(token);
        rememberMeTokenRepository.save(newToken);
        System.out.println(rememberMeTokenRepository.count() + " = count");
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        System.out.println("reading token");
        final RememberMeToken token = this.rememberMeTokenRepository.findBySeries(series);
        return new PersistentRememberMeToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
    }

    @Override
    public void removeUserTokens(String username) {
        System.out.println("removing token");
        final List<RememberMeToken> tokens = this.rememberMeTokenRepository.findByUsername(username);
        rememberMeTokenRepository.delete(tokens);
    }

    @Override
    public void updateToken(String series, String value, Date lastUsed) {
        final RememberMeToken token = this.rememberMeTokenRepository.findBySeries(series);
        if (token != null) {
            token.setTokenValue(value);
            token.setDate(lastUsed);
            this.rememberMeTokenRepository.save(token);
        }

    }

}
