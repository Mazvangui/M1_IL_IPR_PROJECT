package usine1;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.JSONObject;

import jsonToRabbitmq.JSONSend;

public class SimpleMqttCallBack implements MqttCallback {

	private String EXCHANGE_NAME;
	private String TOPIC_NAME;
	private JSONSend jsonSend;
	public SimpleMqttCallBack(String EXCHANGE_NAME,String TOPIC_NAME) {
		this.EXCHANGE_NAME=EXCHANGE_NAME;
		this.TOPIC_NAME=TOPIC_NAME;
		jsonSend = new JSONSend(null,EXCHANGE_NAME,TOPIC_NAME);
	}
  public void connectionLost(Throwable throwable) {
    System.out.println("Connection to MQTT broker lost!");
  }

  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
	  String response = new String(mqttMessage.getPayload());
	  System.out.println(TOPIC_NAME+" Message received:\t"+ response );
	  sendMsgToRabbitmq(response);
  }

  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
  }
  
  public void sendMsgToRabbitmq(String response) {
	  JSONObject obj = new JSONObject();
	  double value =Double.parseDouble(response);
	  obj.put("date", (new Date()).toString());
	  obj.put("value", value);
	  jsonSend.setObj(obj);
	  jsonSend.run();
  }
}
