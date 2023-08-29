package com.snezana.introtelecom.repositories;

import com.snezana.introtelecom.entity.PackagePlan;
import com.snezana.introtelecom.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PackagePlanRepo extends JpaRepository<PackagePlan, Long> {
    PackagePlan findByPackageCode (String packageCode);
    @Query(
            "SELECT " +
                    "packagePlan "+
                    "FROM PackagePlan packagePlan "+
                    "WHERE packagePlan.packageCode = :packageCode "
    )
    Optional<PackagePlan> findByPackageCodeOpt(@Param("packageCode") String packageCode);
}
