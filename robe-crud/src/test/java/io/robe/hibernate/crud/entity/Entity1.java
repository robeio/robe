package io.robe.hibernate.crud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Entity1 {
	
	@Column(unique=true)
	private String deneme;
	@Column(unique=false)
	private String deneme1;
	@Column
	private String deneme2;
	private String deneme3;

	public String getDeneme() {
		return deneme;
	}

	public void setDeneme(String deneme) {
		this.deneme = deneme;
	}

	public String getDeneme1() {
		return deneme1;
	}

	public void setDeneme1(String deneme1) {
		this.deneme1 = deneme1;
	}

	public String getDeneme2() {
		return deneme2;
	}

	public void setDeneme2(String deneme2) {
		this.deneme2 = deneme2;
	}

	public String getDeneme3() {
		return deneme3;
	}

	public void setDeneme3(String deneme3) {
		this.deneme3 = deneme3;
	}

}
