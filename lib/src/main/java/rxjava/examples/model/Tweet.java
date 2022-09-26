package rxjava.examples.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@ToString
public class Tweet {
    BigInteger sequenceId;
    String user;
    String message;
    List<String> mentions;
}
