package rxjava.examples.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Income {
    boolean exists;
    int value;

    public static Income no() {
        return new Income().exists(false).value(0);
    }
}
