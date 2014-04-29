package br.com.msilveira.mq;

import java.io.IOException;

import br.com.msilveira.mq.core.ClientRabbitMQ;

public class QueueProducerRedelivery extends ClientRabbitMQ {

	public QueueProducerRedelivery() throws IOException {
		super();
	}
	
	public void sendMessageWithRouting(String routingKey) throws IOException {
		//BasicProperties properties =  new BasicProperties(null, null, null, 2, 5, null, null, null, operacaoMQ.getMessageId(), new Date(), null, null, null, null);
		//channel.basicPublish(EXCHANGE_NAME, routingKey, properties, SerializationUtils.serialize(operacaoMQ));
	}

}