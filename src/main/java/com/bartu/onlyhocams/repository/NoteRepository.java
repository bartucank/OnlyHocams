package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.entity.LikeLog;
import com.bartu.onlyhocams.entity.Note;
import com.bartu.onlyhocams.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {

    @Query(value = "select * from note n order by publish_date desc limit :lim offset :off", nativeQuery = true)
    List<Note> getNotes(@Param("lim") int lim, @Param("off") int off);
}