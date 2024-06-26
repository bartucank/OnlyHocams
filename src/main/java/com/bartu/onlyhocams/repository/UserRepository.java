package com.bartu.onlyhocams.repository;

import com.bartu.onlyhocams.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.id=:id")
    User getById(@Param("id") Long id);

    @Query("select u from User u where u.username=:username")
    User findByUsername(@Param("username")String username);

    @Query("select u from User u where u.email=:email")
    User findByEmail(@Param("email")String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO \"user\" (name, username, email, password, role, credit) VALUES (:name, :username, :email, :password, :role, :credit)", nativeQuery = true)
    void insertUser(@Param("name") String name,
                    @Param("username") String username,
                    @Param("email") String email,
                    @Param("password") String password,
                    @Param("role") String role,
                    @Param("credit") BigDecimal credit);

    @Modifying
    @Transactional
    @Query(value = "UPDATE \"user\" SET credit = :finalPrice WHERE id = :id", nativeQuery = true)
    void updateBalance(@Param("id") Long id,
                       @Param("finalPrice") BigDecimal finalPrice);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM \"user\" where id=:id ", nativeQuery = true)
    void deleteById(@Param("id") Long id);
    
    @Query("select u from User u where u.role='USER' ")
    List<User> findAllUsers();
}