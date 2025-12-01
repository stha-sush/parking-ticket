// package com.parkingticket.vehicleservice.config;

// import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.integration.annotation.ServiceActivator;
// import org.springframework.integration.channel.DirectChannel;
// import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
// import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
// import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
// import org.springframework.messaging.MessageChannel;
// import org.springframework.messaging.MessageHandler;

// @Configuration
// public class MqttConfigSender {
// 	@Value("${mqtt.uri}")
// 	private String mqttUri;

// 	@Value("${mqtt.username}")
// 	private String mqttUserName;

// 	@Value("${mqtt.password}")
// 	private String mqttPassword;

// 	Logger logger = LoggerFactory.getLogger(getClass());

// 	@Bean
// 	MqttPahoClientFactory mqttClientFactory() {
// 		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
// 		MqttConnectOptions options = new MqttConnectOptions();

// 		options.setServerURIs(new String[] { mqttUri });
// 		options.setUserName(mqttUserName);
// 		options.setPassword(mqttPassword.toCharArray());
// 		options.setCleanSession(true);

// 		factory.setConnectionOptions(options);
// 		return factory;
// 	}

// 	@Bean
// 	MessageChannel mqttOutboundChannel() {
// 		return new DirectChannel();
// 	}

// 	@Bean
// 	@ServiceActivator(inputChannel = "mqttOutboundChannel")
// 	MessageHandler mqttOutbound() {
// 		// clientId is just an identifier for this sender
// 		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("vehicleMqttClientSender",
// 				mqttClientFactory());
// 		messageHandler.setAsync(true);
// 		messageHandler.setDefaultTopic("#");
// 		messageHandler.setDefaultRetained(false);
// 		return messageHandler;
// 	}

// }
