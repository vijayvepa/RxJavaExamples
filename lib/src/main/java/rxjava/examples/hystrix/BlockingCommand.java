package rxjava.examples.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BlockingCommand extends HystrixCommand<String> {

    public BlockingCommand() {
        super(HystrixCommandGroupKey.Factory.asKey("SomeGroup"));
    }

    protected BlockingCommand(HystrixCommandGroupKey group) {
        super(group);
    }

    @Override
    protected String run() throws Exception {
        final URL url = new URL("http://www.example.com");
        try (InputStream stream = url.openStream()) {
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        }
    }
}
