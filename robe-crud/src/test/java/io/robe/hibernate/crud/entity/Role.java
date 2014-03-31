package io.robe.hibernate.crud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class Role extends BaseEntity<Role> {

	@Column(length = 20, unique = true)
	private String unique1;

	@Column(length = 50)
	private String name1;

	@Column(unique = true)
	private String unique2;

	@Column(length = 50)
	private String name2;

	@Column(length = 50)
	private String name3;

	@Column(length = 50)
	private String name4;

	public String getUnique1() {
		return unique1;
	}

	public void setUnique1(String unique1) {
		this.unique1 = unique1;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getUnique2() {
		return unique2;
	}

	public void setUnique2(String unique2) {
		this.unique2 = unique2;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getName4() {
		return name4;
	}

	public void setName4(String name4) {
		this.name4 = name4;
	}
}
