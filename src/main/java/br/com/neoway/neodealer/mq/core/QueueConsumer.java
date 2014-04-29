package br.com.neoway.neodealer.mq.core;

import java.io.IOException;
import java.util.Date;

import br.com.msilveira.mq.QueueProducerRedelivery;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

public class QueueConsumer extends ClientRabbitMQ implements Consumer {
	
	private static final String OPERACAO_CONSUMER_TAG = "operacao_consumer_tag";
	private String numThread;
	private QueueProducerRedelivery producerRedelivery;

	public QueueConsumer(String numThread) throws IOException {
		super();
		this.numThread = numThread;
		this.producerRedelivery = new QueueProducerRedelivery();
	}

	public void startConsumingMessages() throws IOException {
		// channel.basicConsume(QUEUE_NAME, AUTO_ACK, OPERACAO_CONSUMER_TAG, this);
	}
	
	/**
	 * Called when consumer is registered.
	 */
	@Override
	public void handleConsumeOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + " registered");
	}

	/**
	 * Called when new message is available.
	 */
	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties props, byte[] body) throws IOException {
		boolean tarefaExecutadaComSucesso = false;
		String routingKey = envelope.getRoutingKey();
		long deliveryTag = envelope.getDeliveryTag();
		Date dataHoraQueOperacaoFoiParaFila = props.getTimestamp();
		//OperacaoMQ operacaoMQ = (OperacaoMQ) SerializationUtils.deserialize(body);
		
		System.out.println("---------- THREAD: " + this.numThread);
		//Operacao operacao = new Gson().fromJson(operacaoMQ.getOperacaoJson(), Operacao.class);
		//System.out.println("---------- THREAD: " + this.numThread + " - OPERACAO: " + operacao.getNroProposta());
		
		//List<MovimentoDTO> movimentos =  new ArrayList<MovimentoDTO>();
		//movimentos.add(new MovimentoDTO(dataHoraQueOperacaoFoiParaFila, StatusOperacao.OPERACAO_CADASTRADA_AGUARDANDO_ENVIO_ARS));
		
//		if (routingKey.equals(ClientRabbitMQ.ROUTING_KEY_NOVAS)) {
//			//tarefaExecutadaComSucesso = cadastrarNovaOperacao(operacao, movimentos);
//		} else if (routingKey.equals(ClientRabbitMQ.ROUTING_KEY_REVISADAS)) {
//			//tarefaExecutadaComSucesso = atualizarOperacao(operacao, movimentos);
//		}
		
		if (tarefaExecutadaComSucesso) {
			channel.basicAck(deliveryTag, false);
		} else {
//			if (processarOperacaoNovamente(operacaoMQ.getCountRedelivery())) {
//				this.imcrementaCountRedelivery(operacaoMQ);
//				this.producerRedelivery.sendMessageWithRouting(operacaoMQ, routingKey);
//				channel.basicAck(deliveryTag, false);
//			} else {
//				System.out.println("---------- THREAD: " + this.numThread + " - OPERACAO: " + operacao.getNroProposta() + " - DEAD-LETTER");
//				channel.basicReject(deliveryTag, false);
//			}
		}
	}

//	private void imcrementaCountRedelivery(OperacaoMQ operacaoMQ) {
//		operacaoMQ.setCountRedelivery(operacaoMQ.getCountRedelivery() + 1);
//	}

	private boolean processarOperacaoNovamente(Integer countRedelivery) {
		return countRedelivery < 3;
	}

//	private boolean cadastrarNovaOperacao(Operacao operacao, List<MovimentoDTO>movimentos) {
//		try {
//			this.operacaoService.executarOCRs(operacao, movimentos);
//			return this.operacaoService.cadastrarOperacao(operacao, movimentos);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Ocorreu um erro durante o processamento da operação.");
//		}
//		return false;
//	}
	
//	private boolean atualizarOperacao(Operacao operacao, List<MovimentoDTO>movimentos) {
//		try {
//			this.operacaoService.executarOCRs(operacao, movimentos);
//			return this.operacaoService.atualizarOperacao(operacao, movimentos);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Ocorreu um erro durante o processamento da operação.");
//		}
//		return false;
//	}

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
