package org.baeldung.persistence.dao;

import java.util.List;

import org.baeldung.persistence.model.Site;
import org.baeldung.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Long> {

    List<Site> findByUser(final User user);

}