package pro.antonshu.client.services.rabbitmq;

import com.google.gson.Gson;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;
import pro.antonshu.client.utils.Packet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Для обмена сообщениями используется класс Message пакета org.springframework.amqp.core
 * Этот класс позволяет идентифицировать тип данных внутри сообщений. Клиент принимает сообщения
 * с данными типа JSON, XML и обычноый текст. Сообщения обрабатываются в зависмости от полученного типа.
 * Для передачи телефонного списка используется тип JSON. Сообщение обязательно должно содержать команду ANSWER.
 * Тип XML сохраняется в файл.
 * Тип обычного текста используется для информационных сообщений и просто распечатывается в консоли.
 */

@Service
public class RabbitMQListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        if (message.getMessageProperties().getContentType().equals(MessageProperties.CONTENT_TYPE_JSON)) {
            Gson gson = new Gson();
            String json = new String(message.getBody());
            Packet received = gson.fromJson(json, Packet.class);
            if ("ANSWER".equals(received.getCommandType())) {
                System.out.println("Answer received: " + received.getPersons());
            }
        }
        if (message.getMessageProperties().getContentType().equals(MessageProperties.CONTENT_TYPE_TEXT_PLAIN)) {
            System.out.println("Text received: " + new String(message.getBody()));
        }
        if (message.getMessageProperties().getContentType().equals(MessageProperties.CONTENT_TYPE_XML)) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_YYYY_HH_mm_ss");
            String filename = String.format("files/%s.xml", dateFormat.format(date));
            try (FileOutputStream fos = new FileOutputStream(filename)) {
                fos.write(message.getBody());
                System.out.println("Receiving new file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
