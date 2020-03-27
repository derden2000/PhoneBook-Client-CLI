package pro.antonshu.client.cli;

import com.google.gson.Gson;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import pro.antonshu.client.entities.Person;
import pro.antonshu.client.entities.Phone;
import pro.antonshu.client.utils.Packet;

import java.util.*;

/**
 * В этом классе представлены команды для реализации функций по работе с телефонным справочником.
 * Для получения справки введите в консоли: help.
 * Для отправки сообщений серверу используется тип JSON c экземпляром класса Packet внутри него.
 *
 * @see Packet
 * @see org.springframework.amqp.core.Message
 */

@ShellComponent
public class PhoneBookCli {

    private RabbitTemplate rabbitTemplate;
    private String rabbitmqTopicName;

    @Value("${rabbitmq.topicExchanger}")
    public void setRabbitmqTopicName(String rabbitmqTopicName) {
        this.rabbitmqTopicName = rabbitmqTopicName;
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @ShellMethod("Insert new Person with one or few phones. Insert few phones inside one block \"\"." +
            "Split different phones with sign \"-\". Have a similar options. For Help type \"help insert\" in console")
    public String insert(
            @ShellOption({"-NAME", "--name"}) String personName,
            @ShellOption({"-SURNAME", "--surname"}) String personSurname,
            @ShellOption({"-PHONES", "--phones"}) String personPhones
    ) {
        Person toSend = new Person(personName, personSurname);
        List<Phone> list = new ArrayList<>();
        Arrays.asList(personPhones.split("-")).forEach(s -> list.add(new Phone(s)));
        toSend.setPhoneList(list);
        Packet packet = new Packet("INSERT", null, new ArrayList<Person>(Collections.singleton(toSend)));
        sendPacket(packet);
        return "New Person for insert was sended";
    }

    @ShellMethod("Get one record from the PhoneBook by Person ID. For Help type \"help get-one\" in console")
    public String getOne(
            @ShellOption({"-ID", "--id"}) Long personId
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("ID", String.valueOf(personId));
        sendPacket(new Packet("GET-ONE", params, null));
        return "Request for one person was sended";
    }

    @ShellMethod("Get all records from the PhoneBook. Don't have any options. For Help type \"help get-all\" in console")
    public String getAll() {
        sendPacket(new Packet("GET-ALL", null, null));
        return "Sended request for all records";
    }

    @ShellMethod("Request for delete person from PhoneBook. Don't have any options. For Help type \"help delete\" in console")
    public String delete(
            @ShellOption({"-ID", "--id"}) Long personId
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("ID", String.valueOf(personId));
        sendPacket(new Packet("DELETE", params, null));
        return "Request for delete person was sended";
    }

    @ShellMethod("Get XML file with all records. Don't have any options. For Help type \"help get-xml\" in console")
    public String getXml() {
        sendPacket(new Packet("GET-XML", null, null));
        return "Sended request for XML file";
    }

    private void sendPacket(Packet packet) {
        Gson gson = new Gson();
        String json = gson.toJson(packet);
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        Message message = new Message(json.getBytes(), messageProperties);
        rabbitTemplate.send(rabbitmqTopicName, "server", message);
    }
}
