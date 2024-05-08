package vn.dev.na.thcs.security.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.dev.na.thcs.security.dto.AccountDTO;
import vn.dev.na.thcs.security.entitys.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value=
            "SELECT id, " +
                    "user_name username," +
                    "password password," +
                    "email email," +
                    "full_name fullName," +
                    "status statusId," +
                    "create_date createDate " +
                    " FROM #{#entityName} where user_name =:username ", nativeQuery = true)
    AccountDTO findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
