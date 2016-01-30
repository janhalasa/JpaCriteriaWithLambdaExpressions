package com.halasa.criterialambda.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 *
 * @author janhalasa
 */
@Entity
public class Vendor extends EntityWithId {
	
	private String name;
	
	@OneToMany
	@JoinColumn(name="vendor_id", nullable=true)
	private List<CarModel> models;

	public Vendor() {
	}

	public Vendor(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CarModel> getModels() {
		return models;
	}

	public void setModels(List<CarModel> models) {
		this.models = models;
	}

	@Override
	public String toString() {
		return "Vendor{id=" + getId() + ", name=" + name + '}';
	}
	
}
