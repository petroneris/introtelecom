package com.snezana.introtelecom.repository;

import com.snezana.introtelecom.entity.PackagePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query("SELECT p FROM PackagePlan p WHERE p.packageCode != '00'")
    List<PackagePlan> findAllCustomersPackagePlans();
}
