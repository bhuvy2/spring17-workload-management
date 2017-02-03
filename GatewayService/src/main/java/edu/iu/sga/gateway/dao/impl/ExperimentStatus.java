package edu.iu.sga.gateway.dao.impl;

public enum ExperimentStatus {
	
	CREATED ("CREATED"),
	
	STAGING ("STAGING"),
	
	RUNNING ("RUNNING"),
	
	DOWNLOAD_OUTPUT ("DOWNLOAD_OUTPUT"),
	
	COMPLETED ("COMPLETED");
	
	String status;
	
	private ExperimentStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return this.getStatus();
	}
}
