package com.lvsen.controller;

import javax.servlet.http.HttpSession;

import com.lvsen.entity.User;

public class BaseController {

	public static final String CURRENT_USER = "current_user";

	public static User getCurrentUser(HttpSession session) {
		User currentUser = (User) session.getAttribute(CURRENT_USER);
		if (currentUser == null) {
			return null;
		} else {
			return currentUser;
		}
	}
}
