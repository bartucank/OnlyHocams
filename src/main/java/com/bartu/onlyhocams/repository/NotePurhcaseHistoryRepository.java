package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.entity.Note;
import com.bartu.onlyhocams.entity.NotePurhcaseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotePurhcaseHistoryRepository extends JpaRepository<NotePurhcaseHistory,Long> {

    @Query("select n from NotePurhcaseHistory n where n.user.id=:userId and n.note.id=:noteId")
    NotePurhcaseHistory existsByNoteAndUser(@Param("noteId") Long noteId,
                                @Param("userId") Long userId);
}