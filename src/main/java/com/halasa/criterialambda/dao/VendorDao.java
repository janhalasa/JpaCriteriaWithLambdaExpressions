package com.halasa.criterialambda.dao;

import com.halasa.criterialambda.domain.Vendor;
import javax.persistence.EntityManager;

/**
 *
 * @author janhalasa
 */
public class VendorDao extends AbstractDao<Vendor> {
	
	public VendorDao(EntityManager em) {
		super(Vendor.class, em);
	}
	
}
