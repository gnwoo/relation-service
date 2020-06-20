package com.gnwoo.userservice.data.repo;

import com.gnwoo.userservice.data.table.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Long> {
    List<User> findByUsername(String username);
}
