package com.example.m2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.m1.dao.AccountDao;
import com.example.m1.model.Account;

@RestController
public class AccountController {
	
	@Autowired
	AccountDao dao;
	
	@GetMapping("/get")
	List<Account> all(){
		return dao.getAll();
	}

}
