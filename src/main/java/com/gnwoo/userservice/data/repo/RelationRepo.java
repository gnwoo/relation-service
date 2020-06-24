package com.gnwoo.userservice.data.repo;

import com.gnwoo.userservice.data.table.Relation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RelationRepo extends CrudRepository<Relation, Long> {
    @Query(value = "select rid, uuidb from relation where uuida=?1 union all select rid, uuida from relation where uuidb=?1", nativeQuery = true)
    List<Object[]> findRelationsOf(Long uuid);

    List<Relation> findByrID(Long rID);
    void deleteRelationByrID(Long rID);
}
