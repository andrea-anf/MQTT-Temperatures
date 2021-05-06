import org.eclipse.paho.client.mqttv3.*;

import java.util.Random;
import java.util.Scanner;

public class Publisher {

    public static void main(String[] args) throws InterruptedException {
        Scanner command = new Scanner(System.in);
        MqttClient client;
        String broker = "tcp://localhost:1883";
        String clientId = MqttClient.generateClientId();
        String topic = "home/sensors/temp";
        int sensorMin = 18;
        int sensorMax = 22;
        int qos = 2;


        boolean repeat = true;
        try {
            client = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            // Connect the client
            System.out.println(clientId + " Connecting Broker " + broker);
            client.connect(connOpts);
            System.out.println(clientId + " Connected");

            String payload = String.valueOf(Math.floor(Math.random()*(sensorMax-sensorMin+1)+sensorMin)); // create a random number between 0 and 10
            MqttMessage message = new MqttMessage(payload.getBytes());

            Runnable sender = new PublisherSender(client, message, topic, clientId);

            Thread thread = new Thread(sender);
            thread.start();
            // Set the QoS on the Message

            if(client.isConnected()){
                command.hasNextLine();
                thread.interrupt();
                client.disconnect();
                System.out.println("Publisher " + clientId + " disconnected");
            }



        } catch (MqttException me ) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

}

