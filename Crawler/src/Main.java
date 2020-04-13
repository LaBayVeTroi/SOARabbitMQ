import com.rabbitmq.client.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.charset.StandardCharsets;

public class Main {
    private final static String QUEUE_NAME = "worker";
    private final static String EXCHANGE_NAME = "crawl";

    public static void main(String[] args) throws Exception {
        ConnectionFactory fac = new ConnectionFactory();
        fac.setHost("rabbitmq-host");
        Connection connection = fac.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queuePurge(QUEUE_NAME);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, QUEUE_NAME);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received message: " + message);
            Document doc = null;
            String reply = "";
            try {
                doc = Jsoup.connect(message).get();
                reply = doc.title();
            } catch (Exception e) {
                reply = "\nError: " + e.getMessage();
            }
            AMQP.BasicProperties prop = new AMQP.BasicProperties().builder()
                    .correlationId(message)
                    .replyTo(delivery.getProperties().getReplyTo())
                    .build();
            channel.basicPublish(EXCHANGE_NAME, delivery.getProperties().getReplyTo(), prop, reply.getBytes());

        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}
