package com.codegym.mos.module4projectmos.repository;

import com.codegym.mos.module4projectmos.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}
