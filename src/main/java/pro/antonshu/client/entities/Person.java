package pro.antonshu.client.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Сущность контакта телефоннов книги достаточно легковесна, и повторяет такой же класс на стороне сервера.
 */

@NoArgsConstructor
@Data
public class Person {

    private Long id;

    private String name;

    private String surname;

    private List<Phone> phoneList;

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }
}
