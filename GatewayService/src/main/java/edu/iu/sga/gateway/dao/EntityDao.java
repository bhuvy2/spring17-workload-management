package edu.iu.sga.gateway.dao;

import java.util.List;

import edu.iu.sga.gateway.dao.entity.Experiment;

public interface EntityDao {

	public void saveEntity(Object entity) throws Exception;
	
	public List<Experiment> getExperiments() throws Exception;
	
	public Experiment findExperimentByID(String experimentId) throws Exception;
}
