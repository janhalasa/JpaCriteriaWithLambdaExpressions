package com.halasa.criterialambda.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author janhalasa
 */
@Entity
public class Car extends EntityWithId {
	
	public enum Colour {
		RED, GREEN, BLUE
	}

	@Enumerated(EnumType.STRING)
	private Colour colour;

	@ManyToOne
	private CarModel model;

	@ManyToMany(mappedBy="cars", cascade = CascadeType.ALL)
	private List<Driver> drivers;

	public Car() {
	}

	public Car(Colour colour, CarModel model) {
		this.colour = colour;
		this.model = model;
	}

	public Car(Colour colour, CarModel model, List<Driver> drivers) {
		this(colour, model);
		this.drivers = drivers;
	}
	
	public Colour getColour() {
		return colour;
	}

	public void setColour(Colour colour) {
		this.colour = colour;
	}
	
	public CarModel getModel() {
		return model;
	}

	public void setModel(CarModel model) {
		this.model = model;
	}

	public List<Driver> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<Driver> drivers) {
		this.drivers = drivers;
	}

	@Override
	public String toString() {
		return "Car{id=" + getId() + ", colour=" + colour + ", model=" + model + ", drivers=" + drivers + '}';
	}

	

}
