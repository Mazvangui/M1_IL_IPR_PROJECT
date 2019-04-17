package usine1;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class App {

	public static String MQTT_BROKER = "tcp://127.0.0.1:8084";
	public static String MQTT_CLIENT_ID = "mqtt.sensor";
	public static String MQTT_USERNAME = "casxgqpl";
	public static String MQTT_PASSWORD = "WCqNfUS1rskA";

	public static String[] QUOTES = new String[] { "i am hungry", "DOH !",
			"I believe that children are our future. Unless we stop them now.", "Operator! Give me the number for 911!",
			"Donuts. Is there anything they can't do?", "Just because I don't care doesn't mean I'm not listening.",
			"Trying is just the first step toward failure.",
			"Without TV, it is hard to know when one day ends and another begins.",
			"It takes two to lie. One to lie and one to listen.",
			"Now Bart, since you broke Grandpa's teeth, he gets to break yours." };

	public static class Core implements Runnable {
		String name;
		double temperature;
		double radiation;
		MqttClient client;

		public Core(String name, MqttClient client) {
			super();
			this.name = name;
			this.client = client;
			temperature = 300 + Math.random();
			radiation = 0 + Math.random();
		}

		public void run() {
			try {
				temperature = Math.max(230,temperature+ Math.random() - 0.5);
				radiation = Math.max(0, radiation+Math.random() - 0.5);

				if (Math.random() > 0.999) {
					System.out.println("BOUM !");
					temperature += 100;
					radiation += 500;
				}
				
				if(temperature > 400 || radiation > 400) {
					MqttMessage message = new MqttMessage("DANGER DANGER DANGER !".getBytes());
					client.publish("/" + name + "/alert", message);
				}

				MqttMessage message = new MqttMessage(Double.toString(temperature).getBytes());
				client.publish("/" + name + "/temperature", message);
				
				message = new MqttMessage(Double.toString(radiation).getBytes());
				client.publish("/" + name + "/radiation", message);

			} catch (Exception e) {
				System.err.println("CORE MELT DOWN !!!!!!! Operator! Give me the number for 911!");
				e.printStackTrace();
			}

		}

	}

	public static void main(String[] args) throws MqttException {
		System.out.println("======================");

		
		MqttClient client = new MqttClient(MQTT_BROKER, MQTT_CLIENT_ID);
		System.out.println(MQTT_BROKER);
		System.out.println(MQTT_CLIENT_ID);

		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setUserName(MQTT_USERNAME);
		connOpts.setPassword(MQTT_PASSWORD.toCharArray());
		client.connect(connOpts);
		Random R = new Random();
		

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
		executor.scheduleAtFixedRate(() -> {
			MqttMessage message = new MqttMessage(QUOTES[R.nextInt(QUOTES.length)].getBytes());
			message.setQos(0);
			try {
				System.out.println(" donuts ... Z Z  Z  ... R  R  R ..");
				client.publish("/homer", message);
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}, 0, 30, TimeUnit.SECONDS);

		for (int j = 1; j <= 5; j++) {
			System.out.println(" Z Z Z Z Z Z Z Z Z Z Z , RRRRRRRR,  Z Z Z Z Z Z Z Z Z Z Z , RRRRRRRR");
			executor.scheduleAtFixedRate(new Core("core"+j, client), 0, 4, TimeUnit.SECONDS);
		}
		
		while (true)
			;
	}
}
