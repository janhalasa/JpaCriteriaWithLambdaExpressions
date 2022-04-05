package com.halasa.criterialambda.dao;

import com.halasa.criterialambda.domain.CarModel;
import com.halasa.criterialambda.domain.CarModel_;

import javax.persistence.EntityManager;

/**
 *
 * @author janhalasa
 */
public class CarModelDao extends BasicRepositoryJpa<CarModel> {
	
	public CarModelDao(EntityManager em) {
		super(em, CarModelDao.class, CarModel.class, CarModel_.id);
	}
	
}
