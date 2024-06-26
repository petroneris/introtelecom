package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.AddOn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AddOnRepo extends JpaRepository<AddOn, Long> {

    AddOn findByAddonCode (String addonCode);

    @Query(
            "SELECT " +
                    "addOn "+
                    "FROM AddOn addOn "+
                    "WHERE addOn.addonCode = :addonCode "
    )
    Optional<AddOn> findByAddonCodeOpt(@Param("addonCode") String addonCode);
}
