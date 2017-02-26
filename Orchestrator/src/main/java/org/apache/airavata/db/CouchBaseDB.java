package org.apache.airavata.db;

import java.util.ArrayList;
import java.util.Map;

import org.apache.airavata.sga.commons.model.TaskContext;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.thrift.TException;

import com.couchbase.client.java.*;
import com.couchbase.client.java.document.*;
import com.couchbase.client.java.document.json.*;
import com.couchbase.client.java.query.*;

public class CouchBaseDB {
	private Bucket b_model, b_collection;
	private Cluster cluster;

	private final String B_MO = "Models";
	private final String B_CO = "Collections";

	public CouchBaseDB(){
		cluster = CouchbaseCluster.create("localhost");// Change from localhost to another ip
		b_model = cluster.openBucket(B_MO);
		b_collection = cluster.openBucket(B_CO);
	}
	
	public void createModel(String name, ArrayList<String> jobList, 
			Map<String, ArrayList<String>> dependencies, TaskContext tsk){
		JsonObject newModel = JsonObject.create();
		JsonLongDocument ID = b_model.counter("id", 1);
		newModel.put("ID", ID);
		newModel.put("Name", name);
		newModel.put("Job List", JsonArray.from(jobList));
		newModel.put("Dependencies", JsonHelper.mapSSToJson(dependencies));
		try {
			byte[] arry = ThriftUtils.serializeThriftObject(tsk);
			newModel.put("Context", new String(arry));
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		b_model.upsert(JsonDocument.create(ID.toString(), newModel)); // Assuming ID.toString() will give you the id directly
	}
	
	public JsonObject getModel(String modelID){
		/* 
		 * Needs to figure out a way to get the model from the bucket.
		 * Then, create a model object based on the json
		 */
		JsonObject model = b_model.get(modelID).content();
		return model;
	}
	
	public void upsertDocument(JsonObject obj){
		JsonLongDocument ID = b_collection.counter("id", 1);
		b_collection.upsert(JsonDocument.create(ID.toString(), obj));
	}
	
	public void disconnect(){
		b_model.close();
		b_collection.close();
		cluster.disconnect();
	}
}
