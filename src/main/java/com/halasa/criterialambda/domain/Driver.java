package com.halasa.criterialambda.domain;

import java.util.Arrays;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 *
 * @author janhalasa
 */
@Entity
public class Driver extends EntityWithId {
	
	private String firstName;
	
	private String lastName;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "CAR_DRIVER",
		joinColumns = {@JoinColumn(name = "DRIVER_ID", referencedColumnName = "id")},
		inverseJoinColumns = {@JoinColumn(name = "CAR_ID", referencedColumnName = "id")})
	private List<Car> cars;

	public Driver() {
	}

	public Driver(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Driver(String firstName, String lastName, Car... cars) {
		this(firstName, lastName);
		this.cars = Arrays.asList(cars);
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	@Override
	public String toString() {
		return "Driver{id=" + getId() + ", firstName=" + firstName + ", lastName=" + lastName + ", cars=" + cars + '}';
	}
}
