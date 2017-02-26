package org.apache.airavata.sga.orchestrator.messaging;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.airavata.db.CouchBaseDB;
import org.apache.airavata.db.JsonHelper;
import org.apache.airavata.sga.commons.model.ExperimentPriority;
import org.apache.airavata.sga.commons.model.Response;
import org.apache.airavata.sga.commons.model.SchedulingRequest;
import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.messaging.service.core.MessageHandler;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.model.Message;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.airavata.sga.orchestrator.messaging.OrchestratorMessagingFactory;
import org.apache.airavata.sga.scheduler.messaging.SchedulerMessagingFactory;
import org.apache.airavata.sga.scheduler.util.Constants;
import org.apache.airavata.sga.orchestrator.messaging.Orchestrator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.google.gson.reflect.TypeToken;


/**
 * Created by Ajinkya on 2/24/17.
 */
public class Orchestrator {
	private CouchBaseDB db;
	final String WAITING = "Waiting";
	final String SENT = "Sent";
	final String DONE = "Done";
	final String STATUS = "Status";
	public Orchestrator(){
		db = new CouchBaseDB();
	}
	
	
	public void getModelAndCreateJob(String modelID){
		JsonObject obj = db.getModel(modelID);
		JsonArray jobList = JsonArray.fromJson((String)obj.get("Dependencies"));

		List<Object> jobs = jobList.toList();
		HashMap<String, String> jobStatus = new HashMap<>();
		for(Object job : jobs){
			jobStatus.put((String)job, WAITING);
		}
		
		obj.put(STATUS, JsonHelper.mapStrToJson(jobStatus));
		db.upsertDocument(obj);
		String jsonString = (String)obj.get("Context");
		TaskContext cntx = new TaskContext();
		try {
			ThriftUtils.createThriftFromBytes(jsonString.getBytes(), cntx);
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/** Generate a message **/
		SchedulingRequest request = new SchedulingRequest();
		request.setTaskContext(cntx);
		request.setExperimentPriority(ExperimentPriority.NORMAL);
		request.setScheduleTime("2017-24-02");
		MessageContext mesgCntxt = new MessageContext(request, request.getTaskContext().getExperiment().getExperimentId());
		Publisher publisher = MessagingFactory.getPublisher(Constants.SCHEDULER_MESSAGE_RABBITMQ_PROPERTIES);;
		try {
			publisher.publish(mesgCntxt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void queueJobs(String modelID){
		
	}

}
