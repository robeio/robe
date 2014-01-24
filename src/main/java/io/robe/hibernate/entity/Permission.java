package io.robe.hibernate.entity;

import javax.persistence.*;

@Entity
@Table(name = "PERMISSION")
public class Permission extends BaseEntity {

	@Column(name="P_READ")
	private boolean read;
	@Column(name="P_WRITE")
	private boolean write;
	@Column(name="P_DELETE")
	private boolean delete;
    @ManyToOne
    @JoinColumn(name="MENU_ID", referencedColumnName = "OID")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name="ROLE_ID",referencedColumnName = "OID")
    private Role role;

	public boolean isRead() {
		return read;
	}

    public void setRead(boolean read) {
        this.read = read;
    }
    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }
    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
    @ManyToOne
    @JoinColumn(name="ROLE_ID",nullable = false,referencedColumnName = "OID")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    @ManyToOne
    @JoinColumn(name="MENU_ID",nullable = false,referencedColumnName = "OID")
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
