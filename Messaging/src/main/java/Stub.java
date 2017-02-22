/**
 * Created by Ajinkya on 2/2/17.
 */
import org.apache.airavata.sga.messaging.service.util.MessageContext;
import org.apache.airavata.sga.messaging.service.core.MessageHandler;
import org.apache.airavata.sga.messaging.service.core.MessagingFactory;
import org.apache.airavata.sga.messaging.service.core.Publisher;
import org.apache.airavata.sga.messaging.service.core.Subscriber;
import org.apache.airavata.sga.messaging.service.model.Customer;
import org.apache.airavata.sga.messaging.service.model.Orders;
import org.apache.airavata.sga.messaging.service.util.ThriftUtils;
import org.apache.airavata.sga.messaging.service.util.Type;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Stub {
    public Stub() throws Exception{

    	/**
    	 * Commented out because of compilation errors - 
    	 * 		as I have removed Customer & Order from RabbitMQ
    	 */
//        Publisher publisher = MessagingFactory.getPublisher(Type.CUSTOMER);
//        List<String> routingKeys = new ArrayList<>();
//        routingKeys.add("rk.cutomer");
//        Subscriber subscriber = MessagingFactory.getSubscriber(new OrderMessageHandler(), routingKeys, Type.CUSTOMER);
//        Customer cus = new Customer();
//        cus.setCustomerName("customer2");
//        cus.setCreditLimit(234);
//        Orders orders = new Orders();
//        orders.setOrderAmount(200);
//        orders.setCustomer(cus);
//        orders.setStatus("pending");
//        MessageContext mctx = new MessageContext(orders, "order-1");
//        publisher.publish(mctx);
    }

    /**
     * @param args
     * @throws SQLException
     * @throws IOException
     */
    public static void main(String[] args) throws Exception{
        new Stub();
    }
}