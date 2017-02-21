package org.apache.airavata.sga.commons.task;

import org.apache.airavata.sga.commons.model.OperationFailedException;
import org.apache.airavata.sga.commons.model.Response;
import org.apache.airavata.sga.commons.model.TaskContext;

public interface CommonTask {

	public void init() throws OperationFailedException;
	
	public Response execute(TaskContext taskContext) throws OperationFailedException;
	
	public void postExecute() throws OperationFailedException;
}
