package rxjava.examples.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Temperature {
    private float celsiius;
    private float fahrenheit;
}
