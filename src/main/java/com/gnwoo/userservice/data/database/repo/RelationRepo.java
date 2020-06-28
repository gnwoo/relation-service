package com.gnwoo.userservice.data.database.repo;

import com.gnwoo.userservice.data.database.table.Relation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RelationRepo extends CrudRepository<Relation, String> {
    @Query(value = "select rid, entityidb from relation where type='u' and entityida=?1 union all select rid, entityida from relation where type='u' and entityidb=?1", nativeQuery = true)
    List<Object[]> findUserRelationsOf(String uuid);

    @Query(value = "select rid, entityidb from relation where type='g' and entityida=?1 union all select rid, entityida from relation where type='g' and entityidb=?1", nativeQuery = true)
    List<Object[]> findGroupRelationsOf(String uuid);

    Optional<Relation> findByrID(String rID);
}
