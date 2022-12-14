package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {  //Account, Account의 ID 타입

    Account findByUsername(String username);
}