package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.Phone;
import com.snezana.introtelecom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername (String username);
    User findByPhoneNumber (String phoneNumber);
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
    public List<User> findAllAdminsUsers();

    @Query("SELECT u FROM User u WHERE u.role.roleType='CUSTOMER'")
    public List<User> findAllCustomersUsers();
}
