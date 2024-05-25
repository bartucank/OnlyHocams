package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.entity.LikeLog;
import com.bartu.onlyhocams.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {

}