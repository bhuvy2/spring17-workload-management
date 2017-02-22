package org.apache.airavata.cloud.aurora.sample;

import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.airavata.cloud.aurora.client.AuroraSchedulerClientFactory;
import org.apache.airavata.cloud.aurora.client.AuroraThriftClient;
import org.apache.airavata.cloud.aurora.client.bean.IdentityBean;
import org.apache.airavata.cloud.aurora.client.bean.JobConfigBean;
import org.apache.airavata.cloud.aurora.client.bean.JobKeyBean;
import org.apache.airavata.cloud.aurora.client.bean.ProcessBean;
import org.apache.airavata.cloud.aurora.client.bean.ResourceBean;
import org.apache.airavata.cloud.aurora.client.bean.ResponseBean;
import org.apache.airavata.cloud.aurora.client.bean.TaskConfigBean;
import org.apache.airavata.cloud.aurora.client.sdk.ExecutorConfig;
import org.apache.airavata.cloud.aurora.client.sdk.GetJobsResult;
import org.apache.airavata.cloud.aurora.client.sdk.Identity;
import org.apache.airavata.cloud.aurora.client.sdk.JobConfiguration;
import org.apache.airavata.cloud.aurora.client.sdk.JobKey;
import org.apache.airavata.cloud.aurora.client.sdk.ReadOnlyScheduler;
import org.apache.airavata.cloud.aurora.client.sdk.Response;
import org.apache.airavata.cloud.aurora.client.sdk.TaskConfig;
import org.apache.airavata.cloud.aurora.util.AuroraThriftClientUtil;
import org.apache.airavata.cloud.aurora.util.Constants;
import org.apache.thrift.TException;

// TODO: Auto-generated Javadoc
/**
 * The Class AuroraClientSample.
 */
public class AuroraClientSample {
	
	/** The aurora scheduler client. */
	private static ReadOnlyScheduler.Client auroraSchedulerClient;
	
	/** The properties. */
	private static Properties properties = new Properties();
	
	/**
	 * Gets the job summary.
	 *
	 * @param client the client
	 * @return the job summary
	 */
	public static void getJobSummary(ReadOnlyScheduler.Client client) {
		try {
			Response response = client.getJobs("centos");
			System.out.println("Response status: " + response.getResponseCode().name());
			if(response.getResult().isSetGetJobsResult()) {
				GetJobsResult result = response.getResult().getGetJobsResult();
				System.out.println(result);
				Set<JobConfiguration> jobConfigs = result.getConfigs();
				for(JobConfiguration jobConfig : jobConfigs) {
					System.out.println(jobConfig);
					JobKey jobKey = jobConfig.getKey();
					Identity owner = jobConfig.getOwner();
					TaskConfig taskConfig = jobConfig.getTaskConfig();
					ExecutorConfig exeConfig = taskConfig.getExecutorConfig();
					
					System.out.println("\n**** JOB CONFIG ****");
						System.out.println("\t # instanceCount: " + jobConfig.getInstanceCount());
						
						System.out.println("\t >> Job Key <<");
							System.out.println("\t\t # name: " + jobKey.getName());
							System.out.println("\t\t # role: " + jobKey.getRole());
							System.out.println("\t\t # environment: " + jobKey.getEnvironment());
							
						System.out.println("\t >> Identity <<");
							System.out.println("\t\t # owner: " + owner.getUser());
							
						System.out.println("\t >> Task Config <<");
							System.out.println("\t\t # numCPUs: " + taskConfig.getNumCpus());
							System.out.println("\t\t # diskMb: " + taskConfig.getDiskMb());
							System.out.println("\t\t # ramMb: " + taskConfig.getRamMb());
							System.out.println("\t\t # priority: " + taskConfig.getPriority());
							
							
						System.out.println("\t >> Executor Config <<");
							System.out.println("\t\t # name: " + exeConfig.getName());
							System.out.println("\t\t # data: " + exeConfig.getData());
				}
				
			}
		} catch (TException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the job.
	 *
	 * @throws Exception the exception
	 */
	public static void createJob() throws Exception {
		JobKeyBean jobKey = new JobKeyBean("devel", "centos", "test_job3");
		IdentityBean owner = new IdentityBean("centos");
		
		ProcessBean proc1 = new ProcessBean("process_1", "ping -c 4 sga-mesos-slave", false);
		ProcessBean proc2 = new ProcessBean("process_2", "ping -c 4 sga-mesos-master", false);
		
		Set<ProcessBean> processes = new LinkedHashSet<>();
		processes.add(proc1);
		processes.add(proc2);
		
		ResourceBean resources = new ResourceBean(1, 8, 1);
		
		TaskConfigBean taskConfig = new TaskConfigBean("task_hello_world", processes, resources);
		JobConfigBean jobConfig = new JobConfigBean(jobKey, owner, taskConfig, "example");
		
		String executorConfigJson = AuroraThriftClientUtil.getExecutorConfigJson(jobConfig);
		System.out.println(executorConfigJson);
		
		AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
		ResponseBean response = client.createJob(jobConfig);
		System.out.println(response);
	}
	
	/**
	 * Creates the gromacs MPI.
	 *
	 * @throws Exception the exception
	 */
	public static void createGromacsMPI() throws Exception {
		JobKeyBean jobKey = new JobKeyBean("devel", "centos", "gromacs_mpi");
		IdentityBean owner = new IdentityBean("centos");
		
		String working_dir = "/home/centos/efs-mount-point/gromacs_job_" + ThreadLocalRandom.current().nextInt(1, 101) + "/";
		String gromacs_path = "/home/centos/efs-mount-point/gromacs/gromacs_bin/bin/gmx_mpi mdrun";
		String openmp_path = "/home/centos/efs-mount-point/openmpi_run/bin/mpirun";
		String gromacs_files_path = "/home/centos/efs-mount-point/gromacs/gromacs_install/gromacs-2016.1/build/tests/regressiontests-2016.1/simple/angles1";
		String tpr_file = "reference_d.tpr";
		String gro_file = "conf.gro";
		
		ProcessBean proc1 = new ProcessBean("ceate_workdir", "mkdir " + working_dir, false);
		ProcessBean proc2 = new ProcessBean("copy_gromacs_ip_files", "cp " + gromacs_files_path + "/" + gro_file + " " + gromacs_files_path + "/" + tpr_file + " " + working_dir, false);
		ProcessBean proc3 = new ProcessBean("run_gromacs_mpi", "cd " + working_dir 
				+ " && " + openmp_path + " -np 8 " + gromacs_path 
				+ " -s " + tpr_file + " -c " + gro_file 
				+ " -g " + tpr_file + ".log -e " + gro_file + ".edr", false);
		
		Set<ProcessBean> processes = new LinkedHashSet<>();
		processes.add(proc1);		
		processes.add(proc2);
		processes.add(proc3);
		
		ResourceBean resources = new ResourceBean(1.5, 125, 512);
		
		TaskConfigBean taskConfig = new TaskConfigBean("gromacs_mpi_task", processes, resources);
		JobConfigBean jobConfig = new JobConfigBean(jobKey, owner, taskConfig, "example");
		
		String executorConfigJson = AuroraThriftClientUtil.getExecutorConfigJson(jobConfig);
		System.out.println(executorConfigJson);
		
		AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
		ResponseBean response = client.createJob(jobConfig);
		System.out.println(response);
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		 try {
			properties.load(AuroraClientSample.class.getClassLoader().getResourceAsStream(Constants.AURORA_SCHEDULER_PROP_FILE));
			String auroraHost = properties.getProperty(Constants.AURORA_SCHEDULER_HOST);
			String auroraPort = properties.getProperty(Constants.AURORA_SCHEDULER_PORT);
			auroraSchedulerClient = AuroraSchedulerClientFactory.createReadOnlySchedulerClient(MessageFormat.format(Constants.AURORA_SCHEDULER_CONNECTION_URL, auroraHost, auroraPort));
			
			// get jobs summary
//			AuroraClientSample.getJobSummary(auroraSchedulerClient);
			
			// create sample job
//			AuroraClientSample.createJob();
			AuroraClientSample.createGromacsMPI();
			
//			AuroraThriftClient client = AuroraThriftClient.getAuroraThriftClient(Constants.AURORA_SCHEDULER_PROP_FILE);
//			ResponseBean response = client.getPendingReasonForJob(new JobKeyBean("devel", "centos", "hello_pending"));
			
//			ResponseBean response = client.getJobDetails(new JobKeyBean("devel", "centos", "hello_thrift"));
//			System.out.println(response);
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}

}
