package com.halasa.criterialambda.dao;

import com.halasa.criterialambda.domain.Car;
import com.halasa.criterialambda.domain.Driver;
import com.halasa.criterialambda.domain.Driver_;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author janhalasa
 */
public class DriverDao extends BasicRepositoryJpa<Driver> {
	
	public DriverDao(EntityManager em) {
		super(em, DriverDao.class, Driver.class, Driver_.id);
	}
	
	public List<Driver> findByCar(Car car) {
		return findWhere((criteriaBuilder, root) -> {
			root.fetch(Driver_.cars);
			return criteriaBuilder.isMember(car, root.get(Driver_.cars));
		});
	}
}
