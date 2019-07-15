package com.bn1knb.newsblog.dao;

import com.bn1knb.newsblog.model.Role;
import com.bn1knb.newsblog.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    List<User> findAllByRole(Role role, Pageable pageable);

    Optional<User> findOneByUsername(String username);

    Optional<User> findByEmail(String email);
}
