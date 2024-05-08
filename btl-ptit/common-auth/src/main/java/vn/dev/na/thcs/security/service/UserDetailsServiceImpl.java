package vn.dev.na.thcs.security.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.dev.na.thcs.security.dto.AccountDTO;
import vn.dev.na.thcs.security.repository.AccountRepository;
import vn.dev.na.thcs.security.repository.RoleGroupRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountRepository userRepository;

	@Autowired
	private RoleGroupRepository roleGroupRepository;

	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AccountDTO user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User Not Found with username: " + username);
		}



		return UserDetailsImpl.build(user, new ArrayList<String>());
	}

}
