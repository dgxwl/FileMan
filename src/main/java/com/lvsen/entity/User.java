package com.lvsen.entity;

import lombok.Data;

@Data
public class User {
	private Integer oid;
	private String username;
	private String password;
	private String phone;
	private String email;
	private String qq;
	private String secQuestion;
	private Integer otype;  //权限组
	private Boolean available;
}
