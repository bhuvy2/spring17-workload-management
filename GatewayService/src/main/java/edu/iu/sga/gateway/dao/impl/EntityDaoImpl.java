package edu.iu.sga.gateway.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import edu.iu.sga.gateway.dao.EntityDao;
import edu.iu.sga.gateway.dao.entity.Experiment;

public class EntityDaoImpl implements EntityDao {
	
	Logger logger = LogManager.getLogger(EntityDaoImpl.class);
	private static final String PERSISTENCE_UNIT = "gateway";

	@Override
	public void saveEntity(Object entity) throws Exception {
		try {
			logger.info("Saving entity in database. Entity: " + entity);
			// Connection details loaded from persistence.xml to create EntityManagerFactory.
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

			EntityManager em = emf.createEntityManager();

			// Creating a new transaction.
			EntityTransaction tx = em.getTransaction();

			tx.begin();

			// Persisting the entity object.
			em.merge(entity);

			// Committing transaction.
			tx.commit();
			
			logger.info("DB persist successful; closing connections now!");

			// Closing connection.
			em.close();
			emf.close();
		} catch (Exception ex) {
			logger.error("Error persisting entity in database. Error: " + ex.getMessage(), ex);
			throw ex;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Experiment> getExperiments() throws Exception {
		List<Experiment> experiments = null;
		
		try {
			// Connection details loaded from persistence.xml to create EntityManagerFactory.
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

			EntityManager em = emf.createEntityManager();

			// Creating a new transaction.
			EntityTransaction tx = em.getTransaction();

			tx.begin();

			Query query = em.createQuery("SELECT e FROM Experiment e");
			experiments = query.getResultList();

			// Committing transaction.
			tx.commit();

			// Closing connection.
			em.close();
			emf.close();
		} catch (Exception ex) {
			logger.error("Error getting experiments from database. Error: " + ex.getMessage(), ex);
			throw ex;
		}
		return experiments;
	}

	@Override
	public Experiment findExperimentByID(String experimentId) throws Exception {
		Experiment experiment = null;
		try {
			// Connection details loaded from persistence.xml to create EntityManagerFactory.
			EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);

			EntityManager em = emf.createEntityManager();

			// Creating a new transaction.
			EntityTransaction tx = em.getTransaction();

			tx.begin();

			experiment = em.find(Experiment.class, experimentId);

			// Committing transaction.
			tx.commit();

			// Closing connection.
			em.close();
			emf.close();
		} catch (Exception ex) {
			logger.error("Error finding experiment from database. Error: " + ex.getMessage(), ex);
			throw ex;
		}
		return experiment;
	}

}
