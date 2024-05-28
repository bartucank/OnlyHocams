package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.entity.Note;
import com.bartu.onlyhocams.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select r from Review r where r.note.id=:id")
    List<Review> getByNoteId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM review where note.id=:id ", nativeQuery = true)
    void bulkDeleteByNoteIds(@Param("id")List<Long> id);


}