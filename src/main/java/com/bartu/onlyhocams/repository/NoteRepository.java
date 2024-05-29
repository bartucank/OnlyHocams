package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.entity.LikeLog;
import com.bartu.onlyhocams.entity.Note;
import com.bartu.onlyhocams.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long> {

    @Query(value = "select * from note n where status = 'APPROVED' order by publish_date desc limit :lim offset :off", nativeQuery = true)
    List<Note> getNotes(@Param("lim") int lim, @Param("off") int off);

    @Modifying
    @Transactional
    @Query(value = "update note set status = 'APPROVED' where id = :id" , nativeQuery= true)
    void approveNoteById(@Param("id") Long id);
    @Query(value = "select * from note n where status = 'APPROVED' and lower(n.title) like :keyword order by publish_date desc limit :lim offset :off", nativeQuery = true)
    List<Note> getNotesByKeyword(@Param("lim") int lim,
                                 @Param("off") int off,
                                 @Param("keyword") String keyword);


    @Query(value = "select * from note n where status = 'WAITING_APPROVE' order by publish_date desc limit :lim offset :off", nativeQuery = true)
    List<Note> getWaitingNotes(@Param("lim") int lim,
                               @Param("off") int off);

    @Query("select n.id from Note n where n.user.id=:id")
    List<Long> getNotesByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM note where user_id=:id ", nativeQuery = true)
    void deleteNotesByUserId(@Param("id") Long id);
    @Query(value = "select * from note n left join note_purhcase_history nph on nph.note_id =n.id  where nph.user_id =:userId order by publish_date desc limit :lim offset :off", nativeQuery = true)
    List<Note> getOwnNotes(@Param("lim") int lim,
                               @Param("off") int off,
                           @Param("userId")Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM note where id=:id ", nativeQuery = true)
    void deleteById(@Param("id")Long id);
}