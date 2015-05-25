package org.baeldung.persistence.dao;

import java.util.List;

import org.baeldung.persistence.model.MySite;
import org.baeldung.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MySiteRepository extends JpaRepository<MySite, Long> {

    List<MySite> findByUser(User user);

}