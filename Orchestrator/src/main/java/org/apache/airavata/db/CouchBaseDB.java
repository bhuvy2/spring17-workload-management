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
		newModel.put("ID", b_model.counter("id", 1));
		newModel.put("Name", name);
		newModel.put("Job List", JsonArray.from(jobList));
		newModel.put("Dependencies", JsonHelper.mapSSToJson(dependencies));
		b_model.upsert(JsonDocument.create(name+newModel.get("ID"), newModel));
	}
	public Model getModel(String modelID){
		N1qlQueryResult result = b_model.query(
				N1qlQuery.parameterized("SELECT * FROM "+B_MO+ " WHERE $1 IN ID", JsonArray.from(modelID))
				);
		for(N1qlQueryRow values : result){
			
			
		}
		return null;
	}
	public void disconnect(){
		b_model.close();
		b_collection.close();
		cluster.disconnect();
	}
}
