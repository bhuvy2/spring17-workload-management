package edu.iu.sga.gateway.dao.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Experiment {

	@Id
	String experimentId;
	
	String experimentName;
	
	String dataStageDirectory;
	
	String outputFileName;
	
	String status;
	
	public Experiment() {
		experimentId = UUID.randomUUID().toString();
	}

	public String getExperimentId() {
		return experimentId;
	}

	public void setExperimentId(String experimentId) {
		this.experimentId = experimentId;
	}

	public String getExperimentName() {
		return experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

	public String getDataStageDirectory() {
		return dataStageDirectory;
	}

	public void setDataStageDirectory(String dataStageDirectory) {
		this.dataStageDirectory = dataStageDirectory;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Experiment [experimentId=" + experimentId + ", experimentName=" + experimentName
				+ ", dataStageDirectory=" + dataStageDirectory + ", outputFileName=" + outputFileName + ", status="
				+ status + "]";
	}
}
