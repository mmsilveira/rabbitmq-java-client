package br.com.msilveira.mq.core;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class ClientRabbitMQ {
	
	protected Channel channel;
	protected Connection connection;
	protected String[] routingKeys;
	protected String exchangeName;

	public ClientRabbitMQ prepareClientRabbitMQ(String host, String userName, String password) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
        factory.setPort(5672);
		factory.setUsername(userName);
		factory.setPassword(password);
//		factory.setAutomaticRecoveryEnabled(true);
		connection = factory.newConnection();
		channel = connection.createChannel();
		return this;
	}

	public ClientRabbitMQ createExchange(String name, String type, Boolean durable, Boolean autoDelete, Map<String, Object> args) throws IOException {
		this.exchangeName = name;
		channel.exchangeDeclare(name, type, durable, autoDelete, args);
		return this;
	}
	
	public ClientRabbitMQ createExchange(String name, String type, Boolean durable, Boolean autoDelete) throws IOException {
		this.exchangeName = name;
		channel.exchangeDeclare(name, type, durable, autoDelete, null);
		return this;
	}
	
	public ClientRabbitMQ createQueue(String name, Boolean durable, Boolean exclusive, Boolean autoDelete, Map<String, Object> args) throws IOException {
		channel.queueDeclare(name, durable, exclusive, autoDelete, args);
		return this;
	}
	
	public ClientRabbitMQ createQueue(String name, Boolean durable, Boolean exclusive, Boolean autoDelete) throws IOException{
		channel.queueDeclare(name, durable, exclusive, autoDelete, null);
		return this;
	}
	
	public ClientRabbitMQ createRouting(String queueName, String exchangeName, String routingKey) throws IOException{
		channel.queueBind(queueName, exchangeName, routingKey);
		return this;
	}
	
	public ClientRabbitMQ setMaxMessageDelivery(int prefetchCount) throws IOException {
		channel.basicQos(prefetchCount);
		return this;
	}
	
	public void closeChannelAndConnection() throws IOException, TimeoutException {
		this.channel.close();
		this.connection.close();
	}
}