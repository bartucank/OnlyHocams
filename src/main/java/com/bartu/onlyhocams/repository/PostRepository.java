package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.entity.Note;
import com.bartu.onlyhocams.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO post (content,publish_date,category_id,user_id) VALUES (:content, :publishDate, :categoryId,:userId ) RETURNING id ", nativeQuery = true)
    Long insertPost(@Param("content")String content,
                    @Param("publishDate")LocalDateTime publishDate,
                    @Param("userId")Long userId,
                    @Param("categoryId")Long categoryId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post where category_id=:id ", nativeQuery = true)
    void deletePostByCategoryId(@Param("id")Long id);


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post where id=:id ", nativeQuery = true)
    void deleteById(@Param("id")Long id);



    @Query("select p.id from Post p where p.category.id=:id ")
    List<Long> getByCategoryId(@Param("id")Long id);

    @Query(value = "select * from post p where  p.category_id=:categoryId order by publish_date desc limit :lim offset :off", nativeQuery = true)
    List<Post> getPosts(@Param("lim") int lim, @Param("off") int off, @Param("categoryId") Long categoryId);
    @Query(value = "select * from post p where  p.category_id=:categoryId and lower(p.content) like :key order by publish_date desc limit :lim offset :off", nativeQuery = true)
    List<Post> getPostsByKeyword(@Param("lim") int lim, @Param("off") int off, @Param("categoryId") Long categoryId,@Param("key")String key);

    @Query("select p.id from Post p where  p.user.id=:userId")
    List<Long> getPostsByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM post where user_id=:id ", nativeQuery = true)
    void deletePostsByUserId(@Param("id") Long id);
}