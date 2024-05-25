package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.entity.Category;
import com.bartu.onlyhocams.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM document where post_id in :id ", nativeQuery = true)
    void bulkDeleteByPostIds(@Param("id")List<Long> id);

}