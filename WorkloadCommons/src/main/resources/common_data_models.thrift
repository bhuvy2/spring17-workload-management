namespace java org.apache.airavata.sga.commons.model

typedef i32 integer

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

enum Status {
	OK = 200,
	CREATED = 201,
	ACCEPTED = 202,
	NOT_FOUND = 404,
	BAD_REQUEST = 401
}

struct TargetMachine {
	1: required string hostname,
	2: required integer port,
	3: required string scratchDir,
	4: required string loginId
	5: required DataTransferProtocol dtProtocol
}

struct LocalStorage {
	1: required string hostname,
	2: required integer port,
	3: required string scratchDir,
	4: required string loginId
	5: required DataTransferProtocol dtProtocol
}

struct Experiment {
	1: required string experimentId,
	2: required string workingDir
}

struct Data {
	1: required DataType type,
	2: required string value,
	3: required string name
}

struct Application {
	1: required list<string> commands,
	2: required list<Data> inputs,
	3: optional list<Data> outputs
}

struct TaskContext {
	1: required Experiment experiment,
	2: optional TargetMachine targetMachine,
	3: optional LocalStorage localStorage,
	4: optional Application application,
}

struct Response {
	1: required Status status,
	2: optional string message
}

exception OperationFailedException {
	1: string message
}

