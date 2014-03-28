package test.com.robe.hibernate.crud.entity;

import javax.persistence.Entity;

@Entity
public class Entity3 {

	private double d;
	private String a;

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}
}
