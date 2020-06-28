package com.gnwoo.userservice.data.database.repo;

import com.gnwoo.userservice.data.database.table.entity.GroupEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GroupRepo extends CrudRepository<GroupEntity, Long> {
    Optional<GroupEntity> findByEntityID(Long entityID);
}
