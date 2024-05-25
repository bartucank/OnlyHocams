package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO category (name) VALUES (:name )", nativeQuery = true)
    void insertCategory(@Param("name")String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE category set name = :name where id=:id ", nativeQuery = true)
    void updateCategory(@Param("name")String name,
                        @Param("id")Long id);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM category  where id=:id ", nativeQuery = true)
    void deleteCategory(@Param("id")Long id);
}