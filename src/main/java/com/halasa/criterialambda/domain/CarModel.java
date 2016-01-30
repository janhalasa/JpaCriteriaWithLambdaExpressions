package com.halasa.criterialambda.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author janhalasa
 */
@Entity
public class CarModel extends EntityWithId {
	
	private String name;
	
	@ManyToOne()
	private Vendor vendor;
	
	@OneToMany
	@JoinColumn(name="carmodel_id", nullable=true)
	private List<Car> cars;

	public CarModel() {
	}

	public CarModel(String name, Vendor vendor) {
		this.name = name;
		this.vendor = vendor;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	@Override
	public String toString() {
		return "CarModel{id=" + getId() + ", name=" + name + ", vendor=" + vendor + '}';
	}
	
}
