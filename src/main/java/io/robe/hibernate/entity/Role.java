package io.robe.hibernate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Role extends BaseEntity<Role> {

	@Column(length = 20)
	private String code;

	@Column(length = 50)
	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "role")
	private Set<Permission> permissions = new HashSet<Permission>();

	@JsonIgnore
	@OneToMany(mappedBy = "role")
	private Set<User> users = new HashSet<User>();

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;

	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}


}
