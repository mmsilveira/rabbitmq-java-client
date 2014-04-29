package br.com.msilveira.mq.core;

import java.io.IOException;

public class QueueProducer extends ClientRabbitMQ {

	public QueueProducer() throws IOException {
	}
	
	public void sendMessageWithRouting(String routingKey) throws IOException {
		//BasicProperties properties =  new BasicProperties(null, null, null, 2, 5, null, null, null, operacaoMQ.getMessageId(), new Date(), null, null, null, null);
		//channel.basicPublish(EXCHANGE_NAME, routingKey, properties, SerializationUtils.serialize(operacaoMQ));
	}

}