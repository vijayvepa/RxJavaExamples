package springexample;

import java.text.MessageFormat;

public class Log {
    public void info(String messageFormat, Object... args){
        System.out.println("INFO: "  + MessageFormat.format(messageFormat, args));
    }
}
