package org.schoolregister.dao;

import junit.framework.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class SimpleDaoTest extends TestCase {
	public void test() {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = cfg.buildSessionFactory();

	}

}
