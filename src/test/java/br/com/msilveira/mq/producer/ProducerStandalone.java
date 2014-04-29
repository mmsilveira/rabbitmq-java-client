package br.com.msilveira.mq.producer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.SerializationUtils;

import br.com.msilveira.mq.core.QueueProducer;
import br.com.msilveira.mq.dto.OrderMQ;
import br.com.msilveira.mq.dto.TypeItem;

public class ProducerStandalone {
	
	private static final int PREFETCH_COUNT = 1;
	private static final String USER_NAME = "admin";
	private static final String PASSWORD = "admin";
	private static final String HOST = "192.168.13.15";
	
	private static final String EXCHANGE_NAME = "exchange_delivery";
	private static final String EXCHANGE_TYPE = "direct";
	private static final boolean EXCHANGE_AUTO_DELETE = false;
	private static final boolean EXCHANGE_DURABLE = true;

	private static final String QUEUE_NAME = "queue_delivery";
	public static final String ROUTING_KEY_PIZZA = TypeItem.PIZZA.toString();
	public static final String ROUTING_KEY_HOTDOG = TypeItem.HOT_DOG.toString();

	private static final String DEAD_LETTER_QUEUE = "fault-queue";
	private static final String DEAD_LETTER_ROUTING_KEY = "fault-routing-key";

	private static final boolean QUEUE_AUTO_DELETE = false;
	private static final boolean QUEUE_EXCLUSIVE = false;
	private static final boolean QUEUE_DURABLE = true;
	
	protected static final boolean AUTO_ACK = false;
	
	private static OrderMQ[] orderMQs = new OrderMQ[]{
		new OrderMQ("Gude", "111111111", "Crazi cats street", "Tuna Pizza", TypeItem.PIZZA),
		new OrderMQ("Preta", "222222222", "Without dogs street", "Complete Hotdog", TypeItem.HOT_DOG)
	};

	public static void main(String[] argv) throws IOException {
		QueueProducer producer = new QueueProducer();
		try {
			producer.prepareClientRabbitMQ(HOST, USER_NAME, PASSWORD)
					.createExchange(EXCHANGE_NAME, EXCHANGE_TYPE, EXCHANGE_DURABLE, EXCHANGE_AUTO_DELETE)
					.setMaxMessageDelivery(PREFETCH_COUNT);
			
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("x-dead-letter-exchange", EXCHANGE_NAME);
			args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
			producer.createQueue(QUEUE_NAME, QUEUE_DURABLE, QUEUE_EXCLUSIVE, QUEUE_AUTO_DELETE, args)
					.createRouting(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_PIZZA)
					.createRouting(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_HOTDOG)
					.createQueue(DEAD_LETTER_QUEUE, QUEUE_DURABLE, QUEUE_EXCLUSIVE, QUEUE_AUTO_DELETE)
					.createRouting(DEAD_LETTER_QUEUE, EXCHANGE_NAME, DEAD_LETTER_ROUTING_KEY);
			
			OrderMQ orderMQ;
			for (Integer i = 0; i < orderMQs.length; i++) {
				orderMQ = getMessage(i);
				producer.sendMessageWithRouting(SerializationUtils.serialize(orderMQ), orderMQ.getTypeItem().toString());
				System.out.println(" [x] Sent '" + orderMQ.getTypeItem().toString() + "':'" + orderMQ.toString() + "'");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			producer.closeChannelAndConnection();
		}
	}
	
	public static OrderMQ getMessage(int i) {
		return orderMQs[i];
	}
	
}
