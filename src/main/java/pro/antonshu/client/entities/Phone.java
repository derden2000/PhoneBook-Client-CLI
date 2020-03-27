package pro.antonshu.client.entities;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class Phone {

    private Long id;

    private String number;

    public Phone(String number) {
        this.number = number;
    }
}
