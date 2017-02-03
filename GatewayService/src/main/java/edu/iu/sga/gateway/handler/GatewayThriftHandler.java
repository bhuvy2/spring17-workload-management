package edu.iu.sga.gateway.handler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;

import edu.iu.sga.gateway.api.GatewayService;
import edu.iu.sga.gateway.api.Response;
import edu.iu.sga.gateway.api.ResponseDetail;
import edu.iu.sga.gateway.api.StatusCode;
import edu.iu.sga.gateway.dao.EntityDao;
import edu.iu.sga.gateway.dao.entity.Experiment;
import edu.iu.sga.gateway.dao.impl.EntityDaoImpl;
import edu.iu.sga.gateway.dao.impl.ExperimentStatus;

public class GatewayThriftHandler implements GatewayService.Iface {

	Logger logger = LogManager.getLogger(GatewayThriftHandler.class);
	
	private static final EntityDao dao = new EntityDaoImpl();
	
	@Override
	public Response submitExperiment(String experimentName) throws TException {
		logger.info("Received request to submit job, experiment name: " + experimentName);
		Response response = new Response();
		
		try {
			Experiment experiment = new Experiment();
			experiment.setExperimentName(experimentName);
			experiment.setStatus(ExperimentStatus.CREATED.getStatus());
			
			// stage directory = {exp_name}_{exp_id}
			experiment.setDataStageDirectory(experimentName + "_" + experiment.getExperimentId());
			experiment.setOutputFileName("stdout.out");
			
			// persist experiment in db
			dao.saveEntity(experiment);
			response.setStatus(StatusCode.ACCEPTED);
			response.setDetails(new ResponseDetail("Accepted request to submit a new experiment."));
			
		} catch (Exception ex) {
			response.setStatus(StatusCode.FAILED);
			response.setDetails(new ResponseDetail(ex.getMessage()));
		}
		
		return response;
	}

	@Override
	public Response getExperimentList() throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getExperimentDetails(String experimentId) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

}
