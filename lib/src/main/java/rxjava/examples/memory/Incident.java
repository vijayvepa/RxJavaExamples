package rxjava.examples.memory;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Incident {

    private final byte[] blob = new byte[128 * 1024];

    boolean isHighPriority;
}
