package com.codegym.mos.module4projectmos.repository;

import com.codegym.mos.module4projectmos.model.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    //    @Query(value = "SELECT * FROM privilege WHERE BINARY name=:name", nativeQuery = true)
    Privilege findByName(String name);
}
