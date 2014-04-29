package br.com.msilveira.mq;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

	private static final int PREFETCH_COUNT = 1;
	private static final String USER_NAME = "admin";
	private static final String PASSWORD = "admin";
	private static final String HOST = "192.168.13.15";
	
	private static final String EXCHANGE_NAME = "exchange_main";
	private static final String EXCHANGE_TYPE = "direct";
	private static final boolean EXCHANGE_AUTO_DELETE = false;
	private static final boolean EXCHANGE_AUTO_DURABLE = true;

	private static final String QUEUE_NAME = "queue_main";
	private static final boolean QUEUE_AUTO_DELETE = false;
	private static final boolean QUEUE_EXCLUSIVE = false;
	private static final boolean QUEUE_DURABLE = true;
	
	private static final String ROUTING_KEY_NAME = "routing_key_main";

	public static void main(String[] argv) throws java.io.IOException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		factory.setUsername(USER_NAME);
		factory.setPassword(PASSWORD);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, EXCHANGE_AUTO_DURABLE, EXCHANGE_AUTO_DELETE, null);
		channel.queueDeclare(QUEUE_NAME, QUEUE_DURABLE, QUEUE_EXCLUSIVE, QUEUE_AUTO_DELETE, null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_NAME);
		channel.basicQos(PREFETCH_COUNT);
		
		String message;

		BasicProperties basicProperties;
		for (Integer i = 0; i < 10; i++) {
			message = "Message-" + i;
			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put("count", 0);
			basicProperties = new BasicProperties(null, null, headers, 2, null, null, null, null, i.toString(), new Date(), null, null, null, null);
			channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_NAME, basicProperties, message.getBytes());
			System.out.println(" [x] Sent '" + ROUTING_KEY_NAME + "':'" + message + "'");
		}

		channel.close();
		connection.close();
	}

}
