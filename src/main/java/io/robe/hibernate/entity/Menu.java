package io.robe.hibernate.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MENU")
public class Menu extends BaseEntity {


	@Column(name = "CODE", length = 50, nullable = false)
	private String code;

	@Column(name = "NAME", length = 50, nullable = false)
	private String name;

	@OneToMany(mappedBy = "parentOid", fetch = FetchType.LAZY)
	private List<Menu> items = new LinkedList<Menu>();

	@Column(name = "PARENT_OID")
	private String parentOid;

	@OneToMany(mappedBy = "menu")
	private Set<Permission> permission = new HashSet<Permission>();


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Menu> getItems() {
		return items;
	}

	public void setItems(List<Menu> items) {
		this.items = items;
	}

	public String getParentOid() {
		return parentOid;
	}

	public void setParentOid(String parentOid) {
		this.parentOid = parentOid;
	}

	public Set<Permission> getPermission() {
		return permission;
	}

	public void setPermission(Set<Permission> permission) {
		this.permission = permission;
	}
}
