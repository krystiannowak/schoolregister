package org.schoolregister.dao;

import javax.persistence.*;

import junit.framework.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

public class SimpleDaoTest extends TestCase {
	public void testHibernateConfig() {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = cfg.buildSessionFactory();
	}

	public void testPersistenceConfig() {
		System.out.println("a");
		//EntityManagerFactory emf = Persistence
		//		.createEntityManagerFactory("schoolregister");
		//emf.close();
	}

}
