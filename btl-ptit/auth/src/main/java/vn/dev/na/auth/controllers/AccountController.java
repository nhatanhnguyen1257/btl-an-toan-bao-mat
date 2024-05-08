package vn.dev.na.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.dev.na.auth.service.AccountService;
import vn.dev.na.thcs.security.JwtUtils;
import vn.dev.na.thcs.security.TokenRefreshException;
import vn.dev.na.thcs.security.entitys.Account;
import vn.dev.na.thcs.security.entitys.RefreshToken;
import vn.dev.na.thcs.security.entitys.Role;
import vn.dev.na.thcs.security.payload.LoginRequest;
import vn.dev.na.thcs.security.payload.TokenRefreshRequest;
import vn.dev.na.thcs.security.request.SignupRequest;
import vn.dev.na.thcs.security.response.JwtResponse;
import vn.dev.na.thcs.security.response.MessageResponse;
import vn.dev.na.thcs.security.response.TokenRefreshResponse;
import vn.dev.na.thcs.security.service.RefreshTokenService;
import vn.dev.na.thcs.security.service.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.print.Book;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/auth")
@Validated
@Tag(name = "Quản lý tài khoản", description = "Sử dụng cho đăng nhập, Đăng xuất, đang ký, refest token")
public class AccountController {

	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
    private JwtUtils jwtUtils;
	@Autowired
    private RefreshTokenService refreshTokenService;
	@Autowired
	private AccountService accountService;

    @Operation(summary = "API đăng nhập tài khoản")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Đăng nhập thành công",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JwtResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Dữ liệu nhập không đúng",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Sai thông tin đăng nhập",
                    content = @Content) })
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@Parameter @Valid @RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateTokenFromUsername(userDetails.getId().toString());

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), jwt, httpServletRequest);

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getRefreshToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));

    }


    @Operation(summary = "Đăng ký tài khoản")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Đăng ký tài khoản thành công",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = JwtResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Dữ liệu nhập không đúng",
					content = @Content),
			@ApiResponse(responseCode = "400", description = "Tài khoản đã được sử dụng!",
					content = @Content),
			@ApiResponse(responseCode = "400", description = "Email đã được sử dụng",
					content = @Content)})
    @PostMapping("/register")
	public ResponseEntity<?> registerUser(@Parameter @Valid @RequestBody SignupRequest signUpRequest) throws NoSuchAlgorithmException {
		if (accountService.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản đã tồn tại!"));
		}

		if (accountService.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Email đã được sử dụng!"));
		}

		// Create new user's account
		accountService.save(signUpRequest);
		return ResponseEntity.ok(new MessageResponse("Đăng ký tài khoản thành công!"));
	}

	@Operation(summary = "Tạo token mới khi token cũ hết hạn")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Đăng ký tài khoản thành công",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = TokenRefreshResponse.class)) }),
			@ApiResponse(responseCode = "400", description = "Dữ liệu nhập không đúng",
					content = @Content),
			@ApiResponse(responseCode = "403", description = "refreshtoken hết hạn",
					content = @Content)})
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
        String token = jwtUtils.generateTokenFromUsername(refreshToken.getUserId().toString());
        return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
    }


	@Operation(summary = "Đăng xuất")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Đăng xuất thành công",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = JwtResponse.class)) }),
			@ApiResponse(responseCode = "403", description = "Token hết hạn",
					content = @Content)})
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        refreshTokenService.deleteByUserId(userDetails.getId());
        return ResponseEntity.ok(new MessageResponse("Log out successful!"));
    }

	@Operation(summary = "Update quyền cho tài khoản")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Update role thành công",
					content = { @Content(mediaType = "application/json",
							schema = @Schema(implementation = String.class)) }),
			@ApiResponse(responseCode = "200", description = "Role không tồn tại",
					content = @Content),
			@ApiResponse(responseCode = "403", description = "Token hết hạn",
					content = @Content)})
	@PostMapping("/role")
	public ResponseEntity<?> role() {
		UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		refreshTokenService.deleteByUserId(userDetails.getId());
		return ResponseEntity.ok(new MessageResponse("Log out successful!"));
	}
}
