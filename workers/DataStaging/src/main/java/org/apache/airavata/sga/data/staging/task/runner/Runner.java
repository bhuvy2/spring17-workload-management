package org.apache.airavata.sga.data.staging.task.runner;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Runner {
	
	/** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(Runner.class);
	
	/**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        try {
            Runnable dataStaging = new Runnable() {
                @Override
                public void run() {
                    DataStagingTaskRunner dataStagingTaskRunner = new DataStagingTaskRunner();
                    dataStagingTaskRunner.startDataStagingTaskRunner();
                }
            };
            
            Runnable envSetup = new Runnable() {
                @Override
                public void run() {
                	EnvironmentSetupRunner environmentSetupRunner = new EnvironmentSetupRunner();
                    environmentSetupRunner.startEnvironmentSetupTaskRunner();
                }
            };

            // start the worker thread
            logger.info("main() -> Starting the DataStaging worker.");
            new Thread(dataStaging).start();
            logger.info("main() -> Starting the EnvironmentSetupTask worker.");
            new Thread(envSetup).start();
        } catch (Exception ex) {
            logger.error("main() -> Something went wrong with the runner. Error: " + ex, ex);
        }
    }

}
