package jsonToRabbitmq;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class JSONRecv extends Thread
{
 private String EXCHANGE_NAME;
 private String TOPIC_NAME;
 private ConnectionFactory factory = null;
 private JSONParser parser;
 private String[] parts;
 private InfluxDB influxDB;
 
 public JSONRecv(String EXCHANGE_NAME,String TOPIC_NAME) 
 {
  parser = new JSONParser();
  this.EXCHANGE_NAME = EXCHANGE_NAME;
  this.TOPIC_NAME = TOPIC_NAME;
  this.parts = TOPIC_NAME.split("/");
  
  influxDB = InfluxDBFactory.connect("http://localhost:8086");
	String dbName = "AbdelDB";
	influxDB.createDatabase(dbName);
	influxDB.setDatabase(dbName);
	String rpName = "aRetentionPolicy";
	influxDB.createRetentionPolicy(rpName, dbName, "30d", "30m", 2, true);
	influxDB.setRetentionPolicy(rpName);

	influxDB.enableBatch(BatchOptions.DEFAULTS);
 }

 public void run ()
 {	
	 factory = new ConnectionFactory();

     try {
		factory.setUri("amqp://admin:admin@localhost:8088/ipr");
		Connection connection = factory.newConnection();
	     Channel channel = connection.createChannel();

	     channel.exchangeDeclare(EXCHANGE_NAME, "topic");
	     String QUEUE_NAME = channel.queueDeclare().getQueue();
	     
	     channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, TOPIC_NAME);

	     System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	     DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	         String message = new String(delivery.getBody(), "UTF-8");
	         JSONObject obj=null;
				try {
					obj = (JSONObject) parser.parse(message);
					double value = Double.parseDouble(obj.get("value").toString());
					System.out.println("value ="+value);
					addToInfluxDB(value);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         System.out.println(" [x] Received '" + obj.toJSONString() + "'");
	     };
	     channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
	} catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException | IOException | TimeoutException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
     
     
 }
 
 public void addToInfluxDB(double value) {
	 	
	 	System.out.println("parts[1] ="+parts[1]);
		influxDB.write(Point.measurement(parts[1])
		    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
		    .addField("core_name", parts[0])
		    .addField("value", value)
		    .build());
		
		influxDB.close();
 }
 
}  