package io.robe.hibernate.entity;

import javax.persistence.*;

@Entity
@Table
public class Permission extends BaseEntity {

	public enum Type {
		SERVICE,
		MENU;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="pType")
	private Type type;

    @Column(name="restrictedItemOid",length = 32)
    private String restrictedItemOid;

	@Column(name="pLevel")
	private short pLevel;

    @ManyToOne
    @JoinColumn(name="roleOid",referencedColumnName = "oid")
    private Role role;

    @ManyToOne
    @JoinColumn(name="roleOid",nullable = false,referencedColumnName = "oid")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getRestrictedItemOid() {
		return restrictedItemOid;
	}

	public void setRestrictedItemOid(String restrictedItemOid) {
		this.restrictedItemOid = restrictedItemOid;
	}

	public short getpLevel() {
		return pLevel;
	}

	public void setpLevel(short pLevel) {
		this.pLevel = pLevel;
	}
}
