package io.robe.dto;

import io.robe.hibernate.entity.User;

/**
 * DTO class for User Entity. Used for transfer operations and converts role entity to roleOid .
 */
public class UserDTO extends io.robe.hibernate.entity.User {


	private String roleOid;

	/**
	 * Default constructor.
	 */
	public UserDTO() {
		super();
	}

	/**
	 * Constructor with Entity parameter.
	 *
	 * @param entity Entity to convert DTO
	 */
	public UserDTO(User entity) {
		setEmail(entity.getEmail());
		setName(entity.getName());
		setSurname(entity.getSurname());
		setPassword(entity.getPassword());
		setActive(entity.isActive());
		if (entity.getRole() != null)
			setRoleOid(entity.getRole().getOid());
	}

	/**
	 * @return Current roleOid
	 */
	public String getRoleOid() {
		return roleOid;
	}

	/**
	 * @param roleOid roleOid to set
	 */
	public void setRoleOid(String roleOid) {
		this.roleOid = roleOid;
	}
}
