package com.ss.rabbitmq;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.ss.util.EnumMaster;
import com.ss.util.EnumMaster.Queues;

@ManagedBean
@ApplicationScoped
public class RabbitMQConfig {
	private static final Logger logger = LogManager.getLogger(RabbitMQConfig.class);

	private final ConnectionFactory connectionFactory;
	private String exchange,exchangeType;
	
	public RabbitMQConfig() {
		Properties properties = new Properties();
		connectionFactory = new ConnectionFactory();
		try /*
			 * (InputStream input =
			 * ClassLoader.getSystemResourceAsStream("application.properites"))
			 */{
			/*
			 * properties.load(input);
			 * connectionFactory.setHost(properties.getProperty("rabbitmq.host"));
			 * connectionFactory.setPort(Integer.decode(properties.getProperty(
			 * "rabbitmq.port")));
			 * connectionFactory.setUsername(properties.getProperty("rabbitmq.username"));
			 * connectionFactory.setPassword(properties.getProperty("rabbitmq.password"));
			 * this.exchange=properties.getProperty("rabbitmq.exchange.name");
			 * this.exchangeType=properties.getProperty("rabbitmq.exchange.type");
			 */
			connectionFactory.setHost("localhost");
			connectionFactory.setPort(5672);
			connectionFactory.setUsername("guest");
			connectionFactory.setPassword("guest");
			this.exchange="Common_Exchange";
			this.exchangeType="direct";  
			logger.debug("Initialized RabbitMq connection...");

		} catch (Exception e) {
			logger.error("Exception occured when initializing RabbitMq connection exception:{}",e.getMessage());
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void initialize() {

		try (Connection connection = getConnectionFactory().newConnection();
				Channel channel = connection.createChannel();) {
			Stream.of(Queues.values()).forEach(q ->{
				try {
					createExchangeAndQueue(channel, q.getQueueName(), exchange, exchangeType, q.getRoutingKey());
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createExchangeAndQueue(Channel channel, String queueName, String exchangeName, String exchangeType,
			String routingKey) throws IOException {

		channel.queueDeclare(queueName, true, false, false, null);
		channel.exchangeDeclare(exchangeName, exchangeType, true);
		channel.queueBind(queueName, exchangeName, routingKey);

	}

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public String getExchange() {
		return exchange;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	
	

}
