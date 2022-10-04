package rxjava.examples.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeleData {
    private long timestamp;
    private double value;
}
