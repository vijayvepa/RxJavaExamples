package rxjava.examples;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Person {
    boolean hasActivity;
    int id;
}
