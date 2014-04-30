package br.com.msilveira.mq.core;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class QueueConsumer extends ClientRabbitMQ implements Consumer {
	
	public void start(String queueName, Boolean autoAck, String consumerTag) throws IOException {
		channel.basicConsume(queueName, autoAck, consumerTag, this);
		System.out.println(new StringBuilder(" [*] Consumer for Queue ").append(queueName).append(" started. Waiting for messages."));
	}
	
	public void receivedMessage(long deliveryTag) throws IOException {
		channel.basicAck(deliveryTag, false);
	}
	
	@Override
	public void handleConsumeOk(String consumerTag) {
		System.out.println(new StringBuilder("Consumer ").append(consumerTag).append(" registered"));
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties props, byte[] body) throws IOException {
		System.out.println("HANDLEDELIVERY");
	}

	@Override
	public void handleCancel(String consumerTag) throws IOException {
		System.out.println("HANDLECANCEL");
	}

	@Override
	public void handleCancelOk(String consumerTag) {
		System.out.println("HANDLECANCELOK");
	}

	@Override
	public void handleRecoverOk(String consumerTag) {
		System.out.println("HANDLERECOVEROK");
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
		System.out.println("HANDLESHUTDOWNSIGNAL");
	}

}
