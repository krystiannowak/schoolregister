package org.schoolregister.dao;

import junit.framework.*;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.schoolregister.bo.*;

public class SimpleDaoTest extends TestCase {
	public void test() {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		cfg.configure("hibernate-cfg.xml");
		cfg.addAnnotatedClass(StudentImpl.class);
		SessionFactory sessionFactory = cfg.buildSessionFactory();

	}

}
