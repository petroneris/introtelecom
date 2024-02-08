package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    @Query(
            "SELECT " +
                    "role "+
                    "FROM Role role "+
                    "WHERE role.roleType = :roleType "
    )
    Optional<Role> findByRoleTypeOpt(@Param("roleType") String roleType);
}
