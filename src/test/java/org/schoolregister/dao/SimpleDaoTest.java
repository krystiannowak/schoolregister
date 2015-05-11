package org.schoolregister.dao;

import javax.persistence.*;

import junit.framework.*;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.schoolregister.bo.*;

/**
 * 
 * @author krystian
 * 
 */
public class SimpleDaoTest extends TestCase {
	public void testHibernateConfig() {
		AnnotationConfiguration cfg = new AnnotationConfiguration();
		cfg.configure("hibernate.cfg.xml");
		SessionFactory sessionFactory = cfg.buildSessionFactory();

		Session session = sessionFactory.openSession();

		session.get(StudentImpl.class, 0L);

		session.flush();
		session.close();

	}

	public void testPersistenceConfig() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("schoolregister");

		EntityManager mgr = emf.createEntityManager();

		mgr.find(StudentImpl.class, 0L);

		// mgr.flush();
		mgr.close();

	}

}
