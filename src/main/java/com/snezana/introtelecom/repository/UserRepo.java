package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername (String username);

    @Query(
            "SELECT " +
                    "user "+
                    "FROM User user "+
                    "WHERE user.phoneNumber = :phoneNumber "
    )
    Optional<User> findByPhoneNumberOpt(@Param("phoneNumber") String phoneNumber);

    @Query(
            "SELECT " +
                    "user "+
                    "FROM User user "+
                    "WHERE user.username = :username "
    )
    Optional<User> findByUsernameOpt(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.role.roleType='ADMIN'")
    List<User> findAllAdminsUsers();

    @Query("SELECT u FROM User u WHERE u.role.roleType='CUSTOMER'")
    List<User> findAllCustomersUsers();

    @Modifying
    @Query("DELETE FROM User u where u.username = :username")
    void deleteByUsername (@Param("username") String username);
}
