package pro.antonshu.client.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import pro.antonshu.client.entities.Person;

import java.util.List;
import java.util.Map;

/**
 * Класс описывает единицу передачи информации о записях телефонного справочника.
 * Для коммуникации сервера и клиента используются команды. Для удобства использования команд
 * предусмотрены параметры. В пакете также может быть передан список контактов.
 */

@Data
@NoArgsConstructor
public class Packet {

    private String commandType;

    private Map<String, String> commandParams;

    private List<Person> persons;

    public Packet(String commandType, Map<String, String> commandParams, List<Person> persons) {
        this.commandType = commandType;
        this.commandParams = commandParams;
        this.persons = persons;
    }
}
