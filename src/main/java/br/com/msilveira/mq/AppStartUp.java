package br.com.msilveira.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AppStartUp implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
		try {
			@SuppressWarnings("unused")
			WebApplicationContext applicationContext = createWebApplicationContext(servletContextEvent);
//			OperacaoService operacaoService = applicationContext.getBean("mq_service_operacaoService", OperacaoService.class);

            DeliveryConsumer deliveryConsumer = new DeliveryConsumer("1");
			deliveryConsumer.configureAllSystemForMessages();
			deliveryConsumer.start(DeliveryConsumer.QUEUE_NAME, DeliveryConsumer.AUTO_ACK, DeliveryConsumer.CONSUMER_TAG);
			
//			deliveryConsumer = new DeliveryConsumer("2");
//			deliveryConsumer.start(DeliveryConsumer.QUEUE_NAME, DeliveryConsumer.AUTO_ACK, DeliveryConsumer.CONSUMER_TAG);

//			deliveryConsumer = new DeliveryConsumer("3");
//			deliveryConsumer.start(DeliveryConsumer.QUEUE_NAME, DeliveryConsumer.AUTO_ACK, DeliveryConsumer.CONSUMER_TAG);

            System.out.println("QueueConsumer started!");
        } catch (IOException e) {
            System.out.println("Problema ao tentar inicializar o QueueConsumer!");
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
	}

	private WebApplicationContext createWebApplicationContext(ServletContextEvent servletContextEvent) {
		return WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
	}
	
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		System.out.println("Cleanup activity: QueueConsumer instance set to null");
	}

}
