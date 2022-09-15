package rxjava.examples.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class EmailOrFailed {

    Email email;
    Ticket failed;
}
