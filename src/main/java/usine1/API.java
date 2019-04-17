package usine1;

import org.eclipse.paho.client.mqttv3.MqttException;

import jsonToRabbitmq.JSONRecv;

public class API {
	
	private static final String EXCHANGE_NAME="AbdelEX";
	
  public static void main(String[] args) throws MqttException, InterruptedException {

	  for (int j = 1; j <= 5; j++) {
		  ThreadSend view1 = new ThreadSend("core"+j+"/temperature");
	      view1.start();
	      Thread.sleep(3000);
	      ThreadSend view2 = new ThreadSend("core"+j+"/radiation");
	      view2.start();
	      Thread.sleep(3000);
	      ThreadSend view3 = new ThreadSend("core"+j+"/alert");
	      view3.start();
	      Thread.sleep(3000);
	      
	      JSONRecv jsonRecv1 = new JSONRecv(EXCHANGE_NAME, "core"+j+"/temperature");
	      jsonRecv1.start();
	      Thread.sleep(3000);
	      JSONRecv jsonRecv2 = new JSONRecv(EXCHANGE_NAME, "core"+j+"/radiation");
	      jsonRecv2.start();
	      Thread.sleep(3000);
	      JSONRecv jsonRecv3 = new JSONRecv(EXCHANGE_NAME, "core"+j+"/alert");
	      jsonRecv3.start();
	      Thread.sleep(3000);
	      
	  }
      
      System.out.println("all element are start");
  }

}
