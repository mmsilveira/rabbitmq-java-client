package br.com.msilveira.mq.core;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;

public class QueueProducer extends ClientRabbitMQ {

	public void sendMessageWithRouting(byte[] message, String routingKey) throws IOException {
		BasicProperties properties = new BasicProperties().builder().deliveryMode(2).build();
		channel.basicPublish(exchangeName, routingKey, properties, message);
	}

}