package org.baeldung.persistence.dao;

import java.util.List;

import org.baeldung.persistence.model.RememberMeToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RememberMeTokenRepository extends JpaRepository<RememberMeToken, Long> {

    RememberMeToken findBySeries(String series);

    List<RememberMeToken> findByUsername(String username);

}
