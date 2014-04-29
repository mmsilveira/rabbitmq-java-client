package br.com.msilveira.mq.producer;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.msilveira.mq.core.QueueProducer;

import com.rabbitmq.client.AMQP.BasicProperties;

public class ProducerStandalone {
	
	private static final int PREFETCH_COUNT = 1;
	private static final String USER_NAME = "admin";
	private static final String PASSWORD = "admin";
	private static final String HOST = "192.168.13.15";
	
	private static final String EXCHANGE_NAME = "exchange_main";
	private static final String EXCHANGE_TYPE = "direct";
	private static final boolean EXCHANGE_AUTO_DELETE = false;
	private static final boolean EXCHANGE_DURABLE = true;

	private static final String QUEUE_NAME = "queue_main";
	public static final String ROUTING_KEY_NOVAS = "novas_operacoes";
	public static final String ROUTING_KEY_REVISADAS = "operacoes_revisadas";

	private static final String DEAD_LETTER_QUEUE = "fault-operacao-queue";
	private static final String DEAD_LETTER_ROUTING_KEY = "fault-operacao-routing-key";

	private static final boolean QUEUE_AUTO_DELETE = false;
	private static final boolean QUEUE_EXCLUSIVE = false;
	private static final boolean QUEUE_DURABLE = true;
	
	protected static final boolean AUTO_ACK = false;

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
					.createRouting(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_NOVAS);
			
			String message;
			BasicProperties basicProperties;
			for (Integer i = 0; i < 10; i++) {
				message = "Message-" + i;
				Map<String, Object> headers = new HashMap<String, Object>();
				headers.put("count", 0);
				basicProperties = new BasicProperties(null, null, headers, 2, null, null, null, null, i.toString(), new Date(), null, null, null, null);
	//			channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_NAME, basicProperties, message.getBytes());
	//			System.out.println(" [x] Sent '" + ROUTING_KEY_NAME + "':'" + message + "'");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			producer.closeChannelAndConnection();
		}
	}
	
}
