package org.apache.airavata.sga.scheduler.messaging;

import org.apache.airavata.sga.commons.model.SchedulingRequest;
import org.apache.airavata.sga.messaging.service.core.MessageHandler;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.model.Message;
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.airavata.sga.scheduler.gossip.Worker;
import org.apache.airavata.sga.scheduler.gossip.WorkerAccessException;
import org.apache.airavata.sga.scheduler.gossip.WorkerClusterInformation;
import org.apache.airavata.sga.scheduler.graph.DiGraph;
import org.apache.airavata.sga.scheduler.graph.Edge;
import org.apache.airavata.sga.scheduler.graph.GraphFactory;
import org.apache.airavata.sga.scheduler.graph.Vertex;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Ajinkya on 2/24/17.
 */
public class SchedulerMessageHandler implements MessageHandler {

    /** The Constant logger. */
    private static final Logger logger = LogManager.getLogger(SchedulerResponseHandler.class);

    private static final SRBatchBuilder SRBatchBuilder = new SRBatchBuilder();

    @Override
    public void onMessage(MessageContext messageContext) {
        try{
            logger.info("onMessage() -> New message received. Message Id : " + messageContext.getMessageId());

//            messageBatchBuilder.processMessage(messageContext);
//            if(!messageBatchBuilder.shouldProcess()) {
//                logger.info("onMessage() -> Buffering message. Message Id : " + messageContext.getMessageId());
//                return;
//            }

            TBase<?, ?> event = messageContext.getEvent();
            byte[] bytes = ThriftUtils.serializeThriftObject(event);

            Message message = new Message();
            ThriftUtils.createThriftFromBytes(bytes, message);

            SchedulingRequest schedulingRequest = new SchedulingRequest();
            ThriftUtils.createThriftFromBytes(message.getEvent(), schedulingRequest);

            SRBatchBuilder.processRequest(schedulingRequest);
            if(!SRBatchBuilder.shouldProcess()) {
                logger.info("onMessage() -> Buffering request. Message Id : " + messageContext.getMessageId());
                return;
            }

            ArrayList<SchedulingRequest> requests = getRequests();

            for (SchedulingRequest request : requests) {
                //logger.debug("onMessage() -> Get publisher. Message Id : " + messageContext.getMessageId());
                Publisher publisher = SchedulerMessagingFactory.getPublisher(request.getTaskContext().getQueueName());

                logger.info("onMessage() -> Publishing task context. Queue name : " + request.getTaskContext().getQueueName()
                        + ", Experiment Id : " +  request.getTaskContext().getQueueName());

                publisher.publish(new MessageContext(schedulingRequest.getTaskContext(),
                        schedulingRequest.getTaskContext().getExperiment().getExperimentId()));

                logger.info("onMessage() -> Message published. Queue name : " + schedulingRequest.getTaskContext().getQueueName()
                        + ", Experiment Id : " +  schedulingRequest.getTaskContext().getExperiment().getExperimentId());
            }

        } catch (TException e) {
            logger.error("onMessage() -> Error processing message. Message Id : " + messageContext.getMessageId(), e);
        } catch (Exception e) {
            logger.error("onMessage() -> Error publishing message. Message Id : " + messageContext.getMessageId(), e);
        }
    }

    public ArrayList<SchedulingRequest> getRequests() {
        GraphFactory graphFactory = new GraphFactory();
        graphFactory.initSession(new WorkerClusterInformation() {
            @Override
            public ArrayList<Worker> getInformation() throws WorkerAccessException {
                // define plz guys



                return new ArrayList<Worker>();
            }
        });
        SRBatch batch = SRBatchBuilder.getBatch();
        DiGraph diGraph = graphFactory.getDiGraph(SRBatchBuilder.getBatch());

        // how to correctly call max flow

        ArrayList<Edge> graphEdges = diGraph.getEdges();
        Edge someEdge = graphEdges.get(new Random().nextInt(graphEdges.size()));

        return DiGraph.convertToRequests(diGraph.maxFlow(someEdge.getJob(),someEdge.getWorker()));
    }

}
