package jsonToRabbitmq;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;

import com.rabbitmq.client.AMQP.Exchange;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class JSONSend{

	private String EXCHANGE_NAME;
	private String TOPIC_NAME;
	private ConnectionFactory factory = null;
	private JSONObject obj;
	
	public JSONSend(JSONObject obj,String EXCHANGE_NAME,String TOPIC_NAME) {
		this.obj = obj;
		this.EXCHANGE_NAME = EXCHANGE_NAME;
		this.TOPIC_NAME=TOPIC_NAME;
	}

	public void run() {

		factory = new ConnectionFactory();

		try {
			factory.setUri("amqp://admin:admin@localhost:8088/ipr");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic");

			
			channel.basicPublish(EXCHANGE_NAME, TOPIC_NAME, null, obj.toJSONString().getBytes());
			System.out.println(" [x] Sent '" + obj.toJSONString() + "'");

			Thread.sleep(1000);
		} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException | IOException | TimeoutException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setObj(JSONObject obj) {
		this.obj=obj;
	}

}