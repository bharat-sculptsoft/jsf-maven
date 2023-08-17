package com.ss.rabbitmq;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.ss.util.EnumMaster.Queues;

import lombok.Data;

@ManagedBean(name = "rabbitMQSender")
@RequestScoped
@Data
public class RabbitMQSender implements Serializable {

	private static Logger logger = LogManager.getLogger(RabbitMQSender.class);

	@ManagedProperty(value = "#{rabbitMQConfig}")
	private RabbitMQConfig rabbitMQConfig;


	public void publishMessage(Queues queue,String message) {
		try (Connection connection = rabbitMQConfig.getConnectionFactory().newConnection();
				Channel channel = connection.createChannel();) {
			logger.info("We've just greeted the user!");

			channel.basicPublish(rabbitMQConfig.getExchange(), queue.getRoutingKey(), null, message.getBytes());
			logger.info("Sent message in queue:{} ,message:{}",queue.getQueueName(),message);
		} catch (Exception e) {
			logger.error("Exception occurred when sending message to queue:{} ,exception:{}",queue.getQueueName(),e.getMessage());
			e.printStackTrace();
		}
	}

}
