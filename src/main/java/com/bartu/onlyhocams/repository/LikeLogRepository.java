package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.dto.LikeLogDTO;
import com.bartu.onlyhocams.entity.Document;
import com.bartu.onlyhocams.entity.LikeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LikeLogRepository extends JpaRepository<LikeLog,Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM like_log where post_id in :id ", nativeQuery = true)
    void bulkDeleteByPostIds(@Param("id")List<Long> id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM like_log where id=:id ", nativeQuery = true)
    void deleteById(@Param("id")Long id);

    @Query("select l from LikeLog l where l.user.id=:user and l.post.id=:post ")
    LikeLog getLikeLogByPostAndUser(@Param("user")Long user,
                                           @Param("post")Long post);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO like_log (post_id,type,user_id) VALUES (:post,:type,:user)", nativeQuery = true)
    void likeDislikePost(@Param("user")Long user,
                         @Param("post")Long post,
                         @Param("type")String type);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO like_log (post_id,content,user_id) VALUES (:post,:content,:user)", nativeQuery = true)
    void addComment(@Param("user")Long user,
                         @Param("post")Long post,
                         @Param("content")String content);

    @Query("select l from LikeLog l where l.post.id=:id")
    List<LikeLog> getLikes(@Param("id") Long id);
}
