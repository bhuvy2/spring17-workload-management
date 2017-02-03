namespace java edu.iu.sga.gateway.api

typedef i32 int;

struct ExperimentDM {
	1: string experimentId,
	2: string experimentName,
	3: string status,
	4: string dataStageDirectory,
	5: string outputFileName
}

enum StatusCode {
	OK = 200,
	FAILED = 500,
	ACCEPTED = 202
}

struct GetExperimentListResponse {
	1: required list<ExperimentDM> experiments
}

struct GetExperimentResponse {
	1: required ExperimentDM experiment
}

struct ResponseDetail {
	1: string message
}

union Result {
	1: GetExperimentListResponse experimentListResponse,
	2: GetExperimentResponse experimentResponse
}

struct Response {
	1: StatusCode status,
	2: optional Result result,
	3: ResponseDetail details 
}

service GatewayService {
	Response submitExperiment (1: string experimentName)
	Response getExperimentList ()
	Response getExperimentDetails (1: string experimentId)
}