package rxjava.examples.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Wind {
    private int speed;
    private int angleFromEast;
}
