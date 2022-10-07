package rxjava.examples.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Person {
    boolean hasActivity;
    int id;
    int age;
    String firstName;
}
