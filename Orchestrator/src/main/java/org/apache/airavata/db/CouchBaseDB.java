package org.apache.airavata.db;

import java.util.ArrayList;
import java.util.Map;

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
	public void createModel(String name, ArrayList<String> jobList, Map<String, ArrayList<String>> dependencies){
		JsonObject newModel = JsonObject.create();
		JsonLongDocument ID = b_model.counter("id", 1);
		newModel.put("ID", ID);
		newModel.put("Name", name);
		newModel.put("Job List", JsonArray.from(jobList));
		newModel.put("Dependencies", JsonHelper.mapSSToJson(dependencies));
		b_model.upsert(JsonDocument.create(ID.toString(), newModel)); // Assuming ID.toString() will give you the id directly
	}
	public Model getModel(String modelID){
		/* 
		 * Needs to figure out a way to get the model from the bucket.
		 * Then, create a model object based on the json
		 */
		JsonObject model = b_model.get(modelID).content();
		return null;
	}
	public void disconnect(){
		b_model.close();
		b_collection.close();
		cluster.disconnect();
	}
}
