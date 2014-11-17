package io.robe.admin.dto;

import io.robe.admin.hibernate.entity.User;

/**
 * DTO class for User Entity. Used for transfer operations and converts role entity to roleOid .
 */
public class UserDTO extends User {


    private String roleOid;
    private String username;
    private String ticket;
    private String newPassword;

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
        setOid(entity.getOid());
        setLastUpdated(entity.getLastUpdated());
        setEmail(entity.getEmail());
        setName(entity.getName());
        setSurname(entity.getSurname());
        setPassword(entity.getPassword());
        setActive(entity.isActive());
        if (entity.getRole() != null) {
            setRoleOid(entity.getRole().getOid());
        }
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
