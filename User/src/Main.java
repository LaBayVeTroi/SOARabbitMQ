import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {
    private final static String EXCHANGE_NAME = "crawl";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory fac = new ConnectionFactory();
        // change to IP address to connect to different machine
        System.out.println("rabbitmq-host");
        fac.setHost("rabbitmq-host");
        Scanner scan = new Scanner(System.in);
        Connection connection = fac.newConnection();
        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String link = delivery.getProperties().getCorrelationId();
            System.out.println("Title of website " + link + " is: " + message);
        };

        String reply_queue = channel.queueDeclare().getQueue();
        channel.queueBind(reply_queue, EXCHANGE_NAME, reply_queue);
        channel.basicConsume(reply_queue, true, deliverCallback, consumerTag -> {
        });

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        while (true) {
            String message = scan.nextLine();
            if (message.equalsIgnoreCase("exit")) {
                return;
            }
            AMQP.BasicProperties prop = new AMQP.BasicProperties().builder().replyTo(reply_queue).build();
            // can use StandardCharsets.UTF_8
            channel.basicPublish(EXCHANGE_NAME, "worker", prop, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
