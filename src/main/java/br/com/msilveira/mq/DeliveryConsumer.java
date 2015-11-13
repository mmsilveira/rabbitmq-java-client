package br.com.msilveira.mq;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.util.SerializationUtils;

import br.com.msilveira.mq.core.ClientSettings;
import br.com.msilveira.mq.core.QueueConsumer;
import br.com.msilveira.mq.dto.OrderMQ;
import br.com.msilveira.mq.dto.TypeItem;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

public class DeliveryConsumer extends QueueConsumer{

	private static final int PREFETCH_COUNT = 1;

	public static final String QUEUE_NAME = "queue_delivery";
	public static final String ROUTING_KEY_PIZZA = TypeItem.PIZZA.toString();
	public static final String ROUTING_KEY_HOTDOG = TypeItem.HOT_DOG.toString();

	private static final String DEAD_LETTER_QUEUE = "fault-queue";
	private static final String DEAD_LETTER_ROUTING_KEY = "fault-routing-key";

	private static final boolean QUEUE_AUTO_DELETE = false;
	private static final boolean QUEUE_EXCLUSIVE = false;
	private static final boolean QUEUE_DURABLE = true;
	
	protected static final boolean AUTO_ACK = false;
	
	public static final String CONSUMER_TAG = "delivery_consumer_tag";

	private String consumerId;
	//private QueueProducerRedelivery producerRedelivery;

	public DeliveryConsumer(String consumerId) {
		this.consumerId = consumerId;
	}

	public void configureAllSystemForMessages() throws IOException, TimeoutException {
		this.prepareClientRabbitMQ(ClientSettings.HOST, ClientSettings.USER_NAME, ClientSettings.PASSWORD)
				.createExchange(ClientSettings.EXCHANGE_NAME, ClientSettings.EXCHANGE_TYPE, ClientSettings.EXCHANGE_DURABLE, ClientSettings.EXCHANGE_AUTO_DELETE)
				.setMaxMessageDelivery(PREFETCH_COUNT);
		
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-dead-letter-exchange", ClientSettings.EXCHANGE_NAME);
		args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
		this.createQueue(QUEUE_NAME, QUEUE_DURABLE, QUEUE_EXCLUSIVE, QUEUE_AUTO_DELETE, args)
				.createRouting(QUEUE_NAME, ClientSettings.EXCHANGE_NAME, ROUTING_KEY_PIZZA)
				.createRouting(QUEUE_NAME, ClientSettings.EXCHANGE_NAME, ROUTING_KEY_HOTDOG)
				.createQueue(DEAD_LETTER_QUEUE, QUEUE_DURABLE, QUEUE_EXCLUSIVE, QUEUE_AUTO_DELETE)
				.createRouting(DEAD_LETTER_QUEUE, ClientSettings.EXCHANGE_NAME, DEAD_LETTER_ROUTING_KEY);
	}
	
	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties props, byte[] body) throws IOException {
		super.handleDelivery(consumerTag, envelope, props, body);
		boolean taskSuccessfully = false;
		String routingKey = envelope.getRoutingKey();
		long deliveryTag = envelope.getDeliveryTag();
		
		OrderMQ orderMQ = (OrderMQ) SerializationUtils.deserialize(body);
		
		System.out.println(new StringBuilder("---------- CONSUMER: ").append(this.consumerId).append(" - ORDER: ").append(orderMQ.toString()));
		
		if (routingKey.equals(TypeItem.HOT_DOG.toString())) {
			taskSuccessfully = makeHotdog(orderMQ);
		} else if (routingKey.equals(TypeItem.PIZZA.toString())) {
			taskSuccessfully = makePizza(orderMQ);
		}
		
		if (taskSuccessfully) {
			receivedMessage(deliveryTag);
		} else {
			if (envelope.isRedeliver()) {
				channel.basicAck(deliveryTag, false);
				System.out.println(new StringBuilder("---------- CONSUMER: ").append(this.consumerId)
						.append(" - ORDER: ").append(orderMQ.toString()).append(" - Order cancelado"));
			} else {
				channel.basicReject(deliveryTag, true);
				System.err.println(new StringBuilder("---------- CONSUMER: ").append(this.consumerId)
						.append(" - ORDER: ").append(orderMQ.toString()).append(" - Waiting biker"));
			}
		}
		threadSleep(2000);
	}
	
	private boolean makeHotdog(OrderMQ orderMQ) {
		try {
			System.out.println("Hotdog done.");
			System.out.println("Leaving for delivery.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("An error occurred while creating the hotdog.");
		}
		return false;
	}

	private boolean makePizza(OrderMQ orderMQ) {
		try {
			System.out.println("Hotdog done.");
			System.out.println("Leaving for delivery.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("An error occurred while creating the pizza.");
		}
		return false;
	}
	
	private static void threadSleep(long value) {
		try {
			Thread.sleep(value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
