package com.halasa.criterialambda;

import com.halasa.criterialambda.dao.CarDao;
import com.halasa.criterialambda.dao.CarModelDao;
import com.halasa.criterialambda.dao.DriverDao;
import com.halasa.criterialambda.dao.VendorDao;
import com.halasa.criterialambda.domain.Car;
import com.halasa.criterialambda.domain.Car.Colour;
import com.halasa.criterialambda.domain.CarModel;
import com.halasa.criterialambda.domain.Driver;
import com.halasa.criterialambda.domain.Vendor;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author janhalasa
 */
public class Main {
	
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
		EntityManager em = entityManagerFactory.createEntityManager();
		
		VendorDao vendorDao = new VendorDao(em);
		CarModelDao carModelDao = new CarModelDao(em);
		CarDao carDao = new CarDao(em);
		DriverDao driverDao = new DriverDao(em);
		
		em.getTransaction().begin();
		try {
			// Insert some data to test queries on.
			
			Vendor renault = new Vendor("Renault");
			vendorDao.persist(renault);

			CarModel megane = new CarModel("Megane", renault);
			carModelDao.persist(megane);

			Car redRenaultMegane = new Car(Colour.RED, megane);
			carDao.persist(redRenaultMegane);
			Car greenRenaultMegane = new Car(Colour.GREEN, megane);
			carDao.persist(greenRenaultMegane);

			Driver driver = new Driver("Pedro", "Vedro", redRenaultMegane, greenRenaultMegane);
			driverDao.persist(driver);

			// Query the database
			
			List<Car> driversCars = carDao.findByDriver(driver);
			LOGGER.log(Level.INFO, "Pedro Vedro can drive cars: {0}", driversCars);
			
			List<Car> renaultCars = carDao.findByVendor(renault);
			LOGGER.log(Level.INFO, "Renault cars: {0}", renaultCars);
			
			List<Driver> redMeganeDrivers = driverDao.findByCar(redRenaultMegane);
			LOGGER.log(Level.INFO, "Red Renault Megane drivers: {0}", redMeganeDrivers);

			List<Car> redCars = carDao.findByColour(Colour.RED);
			LOGGER.log(Level.INFO, "Red cars: {0}", redCars);
			
			List<Car> redRenaultCars = carDao.findByVendorAndColour(renault, Colour.RED);
			LOGGER.log(Level.INFO, "Red Renault cars: {0}", redRenaultCars);
			
			List<Car> pedrosCars = carDao.findByDriverFirstName(driver.getFirstName());
			LOGGER.log(Level.INFO, "Pedro Vedro can drive cars: {0}", pedrosCars);
			
			// Delete cars, just to make sure the method works

			carDao.deleteByModel(megane);

			List<Car> allCars = carDao.findAll();
			LOGGER.log(Level.INFO, "All cars: {0}", allCars);
			
			em.getTransaction().commit();
		}
		catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Database access error.", ex);
			em.getTransaction().rollback();
		}
		finally {
			em.close();
		}
	}
}
