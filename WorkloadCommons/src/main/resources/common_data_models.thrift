namespace java org.apache.airavata.sga.commons.model

typedef i32 integer
typedef i64 long

enum DataTransferProtocol {
	SFTP,
	SCP,
	FTP
}

enum DataType {
	FILE,
	STRING,
	URL,
	INTEGER,
	DOUBLE
}

enum MachineType {
	CLOUD,
	HPC,
	VM
}

enum Status {
	OK = 200,
	CREATED = 201,
	ACCEPTED = 202,
	NOT_FOUND = 404,
	BAD_REQUEST = 401,
	FAILED = 500
}

struct TargetMachine {
	1: required string hostname,
	2: required integer port,
	3: optional string scratchDir,
	4: required string loginId
	5: optional DataTransferProtocol dtProtocol,
	6: required MachineType machineType
}

struct LocalStorage {
	1: required string hostname,
	2: required integer port,
	3: optional string scratchDir,
	4: required string loginId
	5: optional DataTransferProtocol dtProtocol
}

struct Experiment {
	1: required string experimentId,
	2: optional string workingDir,
	3: required double numCPU = 0.2,
	4: required long diskMB = 10,
	5: required long ramMB = 128
}

struct Data {
	1: required DataType type,
	2: required string value,
	3: required string name
}

struct Application {
	1: required list<string> commands,
	2: optional list<Data> inputs,
	3: optional list<Data> outputs
}

struct TaskContext {
	1: required Experiment experiment,
	2: optional TargetMachine targetMachine,
	3: optional LocalStorage localStorage,
	4: optional Application application,
}

struct Response {
	1: required string experimentId,
	2: required Status status,
	3: optional string message
}

exception OperationFailedException {
	1: required string message
}

