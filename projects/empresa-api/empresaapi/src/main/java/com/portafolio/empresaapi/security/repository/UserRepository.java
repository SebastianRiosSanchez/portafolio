package com.portafolio.empresaapi.security.repository;

import com.portafolio.empresaapi.security.model.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserName(String userName);

    @Query(
            value = """
                    SELECT u FROM UserEntity u
                    WHERE 
                        (:name IS NULL OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :name, '%')))
                    AND (:email IS NULL OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :email, '%')))
                    AND (:isEnable IS NULL OR u.isEnable = :isEnable)
                    """,
            countQuery = """
                    SELECT COUNT(u) FROM UserEntity u
                    WHERE 
                        (:name IS NULL OR LOWER(u.firstname) LIKE LOWER(CONCAT('%', :name, '%')))
                    AND (:email IS NULL OR LOWER(u.userName) LIKE LOWER(CONCAT('%', :email, '%')))
                    AND (:isEnable IS NULL OR u.isEnable = :isEnable)
                    """
    )
    Page<UserEntity> findAllByFilters(
            @Param("name") String name,
            @Param("email") String email,
            @Param("isEnable") Boolean isEnable,
            Pageable pageable
    );

}
