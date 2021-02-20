package com.example.m2.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.m1.dao.AccountDao;
import com.example.m2.request.LoginRequest;
import com.example.m2.response.JwtResponse;
import com.example.m4.jwt.JwtUtils;
import com.example.m4.services.UserDetailsImpl;



@RestController
@RequestMapping("/api/auth")
public class AuthController {
	

	AuthenticationManager authenticationManager;
	JwtUtils jwtUtils;
	AccountDao accountDao;
	
	@Autowired
	public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, AccountDao accountDao) {
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.accountDao = accountDao;
	}



	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
		
		accountDao.refreshLastLogin(userDetails.getId());
		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), 
				userDetails.getUsername(),  userDetails.getEmail(), roles));
						
			
	}


}

