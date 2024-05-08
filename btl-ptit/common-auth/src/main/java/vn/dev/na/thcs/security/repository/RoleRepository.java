package vn.dev.na.thcs.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import vn.dev.na.thcs.security.entitys.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
