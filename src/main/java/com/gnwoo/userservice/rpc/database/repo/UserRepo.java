package com.gnwoo.userservice.rpc.database.repo;

import com.gnwoo.userservice.rpc.database.table.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
