package io.robe.hibernate.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {


	private static final long serialVersionUID = 1914842698571907341L;

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column( length = 32)
	private String oid;

	@Version
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;


	public BaseEntity() {
	}

	public BaseEntity(String oid, Date lastUpdated) {
		this.oid = oid;
		this.lastUpdated = lastUpdated;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
