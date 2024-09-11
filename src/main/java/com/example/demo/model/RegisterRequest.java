package com.example.demo.model;

public class RegisterRequest {
	private String email;
	private String password;
	private String fullName;

	public String getEmail() {
		return email;
	}

	public RegisterRequest setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public RegisterRequest setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getFullName() {
		return fullName;
	}

	public RegisterRequest setFullName(String fullName) {
		this.fullName = fullName;
		return this;
	}

	@Override
	public String toString() {
		return "RegisterUserDto{" +
				"email='" + email + '\'' +
				", password='" + password + '\'' +
				", fullName='" + fullName + '\'' +
				'}';
	}
}