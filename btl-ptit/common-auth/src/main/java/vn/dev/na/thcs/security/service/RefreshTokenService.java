package vn.dev.na.thcs.security.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.dev.na.thcs.security.TokenRefreshException;
import vn.dev.na.thcs.security.entitys.Account;
import vn.dev.na.thcs.security.entitys.RefreshToken;
import vn.dev.na.thcs.security.repository.AccountRepository;
import vn.dev.na.thcs.security.repository.RefreshTokenRepository;


@Service
public class RefreshTokenService {
	
  @Value("${bezkoder.app.jwtExpirationMs}")
  private Long refreshTokenDurationMs;

  @Autowired
  private AccountRepository userRepository;
  
  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId, String token, HttpServletRequest request) {
    RefreshToken refreshToken = new RefreshToken();
    Optional<Account> account = userRepository.findById(userId);
    if (account.isEmpty()) {
      throw new ArithmeticException("Refresh token was expired. Please make a new signin request");
    }
    refreshToken.setUserId(account.get().getId());
    refreshToken.setIpAddress(request.getRemoteAddr());
    refreshToken.setToken(token);
    refreshToken.setUserAgent(request.getHeader("User-Agent"));
    refreshToken = refreshTokenRepository.save(refreshToken);
    
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() > LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public void deleteByUserId(Long userId) {
      Account account = userRepository.findById(userId).get();
      refreshTokenRepository.deleteByUser(account.getId());

  }
}
