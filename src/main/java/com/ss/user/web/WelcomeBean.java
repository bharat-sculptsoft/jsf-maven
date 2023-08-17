package com.ss.user.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import com.ss.rabbitmq.RabbitMQSender;
import com.ss.util.EnumMaster.Queues;

import lombok.Data;

@ManagedBean
@RequestScoped
@Data
public class WelcomeBean {

	@ManagedProperty(value = "#{rabbitMQSender}")
	private RabbitMQSender rabbitMQSender;

	private String message;

	public void publishProductMessage() {
		try {
			rabbitMQSender.publishMessage(Queues.PRODUCT_SAVE_QUEUE, message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void publishEmployeeMessage() {
		try {
			rabbitMQSender.publishMessage(Queues.EMPLOYEE_SAVE_QUEUE, message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
