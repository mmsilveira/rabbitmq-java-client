package br.com.msilveira.mq.core;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP.BasicProperties;

public class Consumer {

	private static final boolean AUTO_ACK = false;
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

	public static void main(String[] argv) throws java.io.IOException, java.lang.InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		factory.setUsername(USER_NAME);
		factory.setPassword(PASSWORD);
		Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, EXCHANGE_AUTO_DURABLE, EXCHANGE_AUTO_DELETE, null);
		channel.queueDeclare(QUEUE_NAME, QUEUE_DURABLE, QUEUE_EXCLUSIVE, QUEUE_AUTO_DELETE, null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_NAME);

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		QueueingConsumer consumer = new QueueingConsumer(channel){
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
				super.handleDelivery(consumerTag, envelope, properties, body);
				long deliveryTag = envelope.getDeliveryTag();
				
				threadSleep(3000);
				
				String message = new String(body);
				String routingKey = envelope.getRoutingKey();
				String messageId = properties.getMessageId();
				Date timestamp = properties.getTimestamp();
				
				int count = 0;
				if (message.equals("Message-3")) {
					count = 1;
					Map<String, Object> headers;
					headers = properties.getHeaders();
					if (headers == null) {
						headers = new HashMap<String, Object>();
					} else {
						if (headers.containsKey("count")) {
							count = (Integer) headers.get("count");
							count = count + 1;
						}
					}
					headers.put("count", count);
					channel.basicReject(deliveryTag, true);
					properties = new BasicProperties(null, null, headers, 2, null, null, null, null, messageId, new Date(), null, null, null, null);
				} else {
					channel.basicAck(deliveryTag, false);
				}
				System.out.println(" [x] Received '" + routingKey + "':'" + message + "'" + " - " + count + " - " + timestamp);
			}
			@Override
			public void handleCancel(String consumerTag) throws IOException {
				super.handleCancel(consumerTag);
				System.out.println("handleCancel");
			}
			@Override
			public void handleCancelOk(String consumerTag) {
				super.handleCancelOk(consumerTag);
				System.out.println("handleCancelOk");
			}
			@Override
			public void handleConsumeOk(String consumerTag) {
				super.handleConsumeOk(consumerTag);
				System.out.println("handleConsumeOk");
			}
			@Override
			public void handleRecoverOk(String consumerTag) {
				super.handleRecoverOk(consumerTag);
				System.out.println("handleRecoverOk");
			}
			@Override
			public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
				super.handleShutdownSignal(consumerTag, sig);
				System.out.println("handleShutdownSignal");
			}
		};
		
		channel.basicConsume(QUEUE_NAME, AUTO_ACK, consumer);
	}
	
	private static void threadSleep(long value) {
		try {
			Thread.sleep(value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
