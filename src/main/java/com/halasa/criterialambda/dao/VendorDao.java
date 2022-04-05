package com.halasa.criterialambda.dao;

import com.halasa.criterialambda.domain.Vendor;
import com.halasa.criterialambda.domain.Vendor_;

import javax.persistence.EntityManager;

/**
 *
 * @author janhalasa
 */
public class VendorDao extends BasicRepositoryJpa<Vendor> {
	
	public VendorDao(EntityManager em) {
		super(em, VendorDao.class, Vendor.class, Vendor_.id);
	}
	
}
