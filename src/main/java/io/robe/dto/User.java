package io.robe.dto;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class User extends io.robe.hibernate.entity.User {

	private String roleOid;

	public User() {
		super();
	}

	public User(io.robe.hibernate.entity.User entity) throws InvocationTargetException, IllegalAccessException {
		BeanUtils.copyProperties(this, entity);
		if (entity.getRole() != null)
			setRoleOid(entity.getRole().getOid());
	}


	public String getRoleOid() {
		return roleOid;
	}

	public void setRoleOid(String roleOid) {
		this.roleOid = roleOid;
	}
}
