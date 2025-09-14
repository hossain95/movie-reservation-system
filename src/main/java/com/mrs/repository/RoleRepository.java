package com.mrs.repository;

import com.mrs.model.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Integer> {
    Role save(Role role);

    List<Role> findAll();

    Optional<Role> findByName(String name);

    Optional<Role> findById(Integer id);
}
