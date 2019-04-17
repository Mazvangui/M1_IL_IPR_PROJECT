package usine1;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class ThreadSend extends Thread{
	public static String MQTT_BROKER = "tcp://127.0.0.1:8084";
	public static String MQTT_CLIENT_ID = "mqtt.sensor";
	public static String MQTT_USERNAME = "casxgqpl";
	public static String MQTT_PASSWORD = "WCqNfUS1rskA";
	public static String EXCHANGE_NAME = "AbdelEX";
	private String TOPIC_NAME;
	private ThreadSend() {
		
	}
	
	public ThreadSend(String TOPIC_NAME) {
		this.TOPIC_NAME = TOPIC_NAME;
	}
	
	public void run(){
		System.out.println("== START SUBSCRIBER "+TOPIC_NAME+"==");

	    MqttClient client;
		try {
			System.out.println(MqttClient.generateClientId());
			client = new MqttClient(MQTT_BROKER, MqttClient.generateClientId());
			SimpleMqttCallBack tmp = new SimpleMqttCallBack(EXCHANGE_NAME,TOPIC_NAME);
			client.setCallback( tmp );
		    
		    MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setUserName(MQTT_USERNAME);
			connOpts.setPassword(MQTT_PASSWORD.toCharArray());
			client.connect(connOpts);
			

		    client.subscribe("/"+TOPIC_NAME);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
}
