package br.com.msilveira.mq;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.msilveira.mq.core.QueueConsumer;

public class AppStartUp implements ServletContextListener {

	private QueueConsumer consumer;
	
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
//			OperacaoService operacaoService = applicationContext.getBean("mq_service_operacaoService", OperacaoService.class);
			
			consumer = new QueueConsumer("1");
			consumer.startConsumingMessages();
//			
//			consumer = new QueueConsumer(operacaoService, "2");
//			consumer.startConsumingMessages();
			
//			consumer = new QueueConsumer(operacaoService, "3");
//			consumer.startConsumingMessages();
//			
//			consumer = new QueueConsumer(operacaoService, "4");
//			consumer.startConsumingMessages();
			
		} catch (IOException e) {
			System.out.println("Problema ao tentar inicializar o QueueConsumer!");
		}
		System.out.println("QueueConsumer iniciado!");
	}
	
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// Do cleanup operations here
		System.out.println("Cleanup activity: QueueConsumer instance set to null");
	}

}
