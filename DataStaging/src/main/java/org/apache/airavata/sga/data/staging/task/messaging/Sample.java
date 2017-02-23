package org.apache.airavata.sga.data.staging.task.messaging;

import org.apache.airavata.sga.commons.model.*;
import org.apache.airavata.sga.data.staging.task.util.Constants;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.core.Subscriber;
import org.apache.airavata.sga.messaging.service.util.MessageContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Ajinkya on 2/22/17.
 */
public class Sample {

    private Publisher publisher;
    private Subscriber environmentSetupSubscriber;
    private Subscriber dataStagingubscriber;

    public Sample(){
        publisher = MessagingFactory.getPublisher(Constants.DATA_STAGING_RABBITMQ_PROPERTIES);
        environmentSetupSubscriber = MessagingFactory.getSubscriber(new EnvironmentSetupMessageHandler(), Constants.ENVIRONMENT_SETUP_RABBITMQ_PROPERTIES);
        dataStagingubscriber = MessagingFactory.getSubscriber(new DataStagingMessageHandler(), Constants.DATA_STAGING_RABBITMQ_PROPERTIES);
    }
    public static void main(String... args){
        Sample sample = new Sample();
        sample.test();
    }

    void test(){
        try {

            Experiment exp = new Experiment();
            exp.setExperimentId("experiment-" + ThreadLocalRandom.current().nextInt(5000));
            exp.setDiskMB(10);
            exp.setRamMB(128);
            exp.setNumCPU(0.1);
            exp.setWorkingDir("wordir");

            // create application
            Application app = new Application();
            List<String> commands = new ArrayList<>();
            commands.add("ping sga-mesos-master -c 4");
            commands.add("ping sga-mesos-slave -c 4");
            app.setCommands(commands);

            Data data = new Data();
            data.setName("name1");
            data.setValue("value1");
            data.setType(DataType.STRING);
            List<Data> inputs = new ArrayList<>();
            inputs.add(data);

            app.setInputs(inputs);
            app.setOutputs(inputs);


            // create target machine
            TargetMachine target = new TargetMachine();
            target.setHostname("sga-mesos-master");
            target.setPort(8081);
            target.setLoginId("centos");
            target.setMachineType(MachineType.CLOUD);
            target.setScratchDir("scratch");
            target.setDtProtocol(DataTransferProtocol.SCP);
            // create taskcontext
            TaskContext taskContext = new TaskContext();
            taskContext.setApplication(app);
            taskContext.setExperiment(exp);
            taskContext.setTargetMachine(target);

            MessageContext message = new MessageContext(taskContext, "asdfafd");
            publisher.publish(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Constants.DATA_STAGING_RABBITMQ_PROPERTIES);
    }
}
