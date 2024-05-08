package vn.dev.na.thcs.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.dev.na.thcs.security.entitys.RoleGroup;

import java.util.List;

public interface RoleGroupRepository extends JpaRepository<RoleGroup, Long> {

    @Query(value = "SELECT role_id FROM #{#entityName} t where t.account_id = :accountId ", nativeQuery = true)
    List<String> findRoleByAccountId(Long accountId);
}
