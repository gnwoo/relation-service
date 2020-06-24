package com.gnwoo.userservice.data.repo;

import com.gnwoo.userservice.data.table.Invite;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InviteRepo extends CrudRepository<Invite, Long> {
    @Query(value = "select id, uuidb from invite where uuida=?1", nativeQuery = true)
    List<Object[]> findInviteFrom(Long uuid);

//    @Query(value = "select id, uuida from contact_request where uuidb=?1", nativeQuery = true)
//    List<Long> findInviteTo(Long uuid);
}
