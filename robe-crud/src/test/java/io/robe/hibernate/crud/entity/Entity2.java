package io.robe.hibernate.crud.entity;

import javax.persistence.Entity;

@Entity
public class Entity2 {

	private String one;
	private Integer two;

	public String getOne() {
		return one;
	}

	public void setOne(String one) {
		this.one = one;
	}

	public Integer getTwo() {
		return two;
	}

	public void setTwo(Integer two) {
		this.two = two;
	}
}
