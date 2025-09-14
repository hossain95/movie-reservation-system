package com.mrs.repository;

import com.mrs.enumeration.AccountStatus;
import com.mrs.enumeration.AccountType;
import com.mrs.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    List<User> findAll();

    List<User> findAllByAccountType(AccountType accountType);

    Optional<User> findUserByEmailAndStatus(String email, AccountStatus accountStatus);

    Optional<User> findUserByEmailAndAccountType(String email, AccountType accountType);
}
