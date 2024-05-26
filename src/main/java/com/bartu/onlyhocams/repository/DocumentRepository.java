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

    @Modifying
    @Transactional
    @Query(value = "UPDATE document set post_id = :post where id in :ids ", nativeQuery = true)
    void linkDocumentToPost(@Param("post")Long post,
                            @Param("ids")List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "UPDATE document set note_id = :note where id=:id ", nativeQuery = true)
    void linkDocumentToNote(@Param("note")Long note,
                            @Param("id")Long id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO document (file_name, file_type, data) VALUES (:filename, :filetype, :data ) RETURNING id", nativeQuery = true)
    int insertDocument(@Param("filename")String filename,
                        @Param("filetype")String filetype,
                        @Param("data")byte[] data
                        );


    @Query("select d from Document d where d.post.id=:id")
    List<Document> getDocumentsByPostId(@Param("id") Long id);
}