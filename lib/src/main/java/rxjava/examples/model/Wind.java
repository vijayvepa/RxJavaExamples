package rxjava.examples.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.math3.stat.descriptive.moment.SemiVariance;

@Data
@Builder
@ToString
public class Wind {
    private int speed;
    private int angleFromEast;
}
