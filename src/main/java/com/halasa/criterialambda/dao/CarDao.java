package com.halasa.criterialambda.dao;

import com.halasa.criterialambda.domain.Car;
import com.halasa.criterialambda.domain.CarModel;
import com.halasa.criterialambda.domain.CarModel_;
import com.halasa.criterialambda.domain.Car_;
import com.halasa.criterialambda.domain.Driver;
import com.halasa.criterialambda.domain.Driver_;
import com.halasa.criterialambda.domain.Vendor;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author janhalasa
 */
public class CarDao extends BasicRepositoryJpa<Car> {
	
	public CarDao(EntityManager em) {
		super(em, CarDao.class, Car.class, Car_.id);
	}
	
	public List<Car> findByDriver(Driver driver) {
		return findWhere((criteriaBuilder, root) -> (criteriaBuilder.isMember(driver, root.get(Car_.drivers))));
	}
	
	public List<Car> findByVendor(Vendor vendor) {
		return findWhere((criteriaBuilder, root) -> (criteriaBuilder.equal(root.get(Car_.model).get(CarModel_.vendor), vendor)));
	}
	
	public List<Car> findByVendorAndColour(Vendor vendor, Car.Colour colour) {
		// These criteria could also be written as criteriaBuilder.and(Predicate...), but having it as two parameters is probably easier to read.
		return findWhere(
			(criteriaBuilder, root) ->
				criteriaBuilder.and(
						criteriaBuilder.equal(root.get(Car_.model).get(CarModel_.vendor), vendor),
						criteriaBuilder.equal(root.get(Car_.colour), colour)
				)
		);
	}
	
	public List<Car> findByDriverFirstName(String firstName) {
		return findWhere((criteriaBuilder, root) -> (criteriaBuilder.equal(root.join(Car_.drivers).get(Driver_.firstName), firstName)));
	}
	
	public List<Car> findByColour(Car.Colour colour) {
		return find((cb, root, query) -> (query.where(cb.equal(root.get(Car_.colour), colour)).orderBy(cb.desc(root.get(Car_.id)))));
	}
	
	public void deleteByModel(CarModel carModel) {
		deleteWhere((criteriaBuilder, root) -> criteriaBuilder.equal(root.get(Car_.model), carModel));
	}
}
