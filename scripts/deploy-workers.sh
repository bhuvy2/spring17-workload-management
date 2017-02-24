#!/bin/bash

echo "** Installing git"
yum install -y git

echo "** Cloning the workload repository"
git clone -b develop git@github.com:airavata-courses/spring17-workload-management.git

echo "** Build the tasks"
cd JobSubmissionTask && mvn clean install -DskipTests
cd ../DataStaging && mvn clean install -DskipTests
echo "** Build complete!"

echo "** Starting both the workers"
cd ../JobSubmissionTask && java -jar JobSubmissionTask-0.0.1-SNAPSHOT.jar > ~/jobsubmission.log 2>&1 &
cd ../DataStaging && java -jar DataStaging-0.0.1-SNAPSHOT.jar > ~/datastaging.log 2>&1 &

echo "** Complete!"

