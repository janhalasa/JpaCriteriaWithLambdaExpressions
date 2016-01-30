package com.halasa.criterialambda.dao;

import com.halasa.criterialambda.domain.CarModel;
import javax.persistence.EntityManager;

/**
 *
 * @author janhalasa
 */
public class CarModelDao extends AbstractDao<CarModel> {
	
	public CarModelDao(EntityManager em) {
		super(CarModel.class, em);
	}
	
}
