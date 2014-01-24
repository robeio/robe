package io.robe.hibernate.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User extends BaseEntity {

	@Column(name = "EMAIL", unique = true, length = 50)
	private String email;
	@Column(name = "NAME", length = 50, nullable = false)
	private String name;
	@Column(name = "SURNAME", length = 50, nullable = false)
	private String surname;
	@Column(name = "PASSWORD", length = 64, nullable = false)
	private String password;
	@Column(name = "STATUS", nullable = false)
	private boolean active;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ROLE_OID")
	private Role role;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
