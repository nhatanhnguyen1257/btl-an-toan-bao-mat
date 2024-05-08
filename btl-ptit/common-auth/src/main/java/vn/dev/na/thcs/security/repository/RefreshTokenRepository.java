package vn.dev.na.thcs.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.dev.na.thcs.security.entitys.Account;
import vn.dev.na.thcs.security.entitys.RefreshToken;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query(value = "SELECT * FROM #{#entityName} WHERE refresh_token = :token", nativeQuery = true)
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query(value = "DELETE #{#entityName} WHERE user_id = :userid", nativeQuery = true)
    void deleteByUser(Long userid);
}
