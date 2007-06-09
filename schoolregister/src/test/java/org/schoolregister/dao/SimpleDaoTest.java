package org.schoolregister.dao;

import javax.persistence.*;

import junit.framework.*;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.schoolregister.bo.*;

public class SimpleDaoTest extends TestCase {
	public void testHibernateConfig() {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = cfg.buildSessionFactory();
	}

	public void testPersistenceConfig() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("schoolregister");

		EntityManager mgr = emf.createEntityManager();

		mgr.find(StudentImpl.class, 0L);

	}

}
