package com.gnwoo.userservice.data.database.repo;

import com.gnwoo.userservice.data.database.table.Invite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepo extends CrudRepository<Invite, Long> {
    @Query(value = "select id from invite where type='u' and entityida=?1 and entityidb=?2", nativeQuery = true)
    Optional<Long> findUserInviteFromTo(String entityIDA, String entityIDB);

    @Query(value = "select id from invite where type='g' and entityida=?1 and entityidb=?2", nativeQuery = true)
    Optional<Long> findGroupInviteFromTo(String entityIDA, String entityIDB);

    @Query(value = "select id, entityidb from invite where type='u' and entityida=?1", nativeQuery = true)
    List<Object[]> findUserInviteFrom(String entityID);

    @Query(value = "select id, entityidb from invite where type='g' and entityida=?1", nativeQuery = true)
    List<Object[]> findGroupInviteFrom(String entityID);
}
