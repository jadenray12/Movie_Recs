package com.movie.backend.repository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movie.backend.entity.Account;

import java.util.Optional;

@Repository
@ComponentScan("com.movie.entity")
public interface AccountRepository extends JpaRepository<Account, Long> {
    
	@Query("SELECT a FROM Account a WHERE a.user = :username AND a.pass = :password")
    Optional<Account> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
