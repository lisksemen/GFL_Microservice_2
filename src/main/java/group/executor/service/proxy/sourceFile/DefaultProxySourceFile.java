package group.executor.service.proxy.sourceFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.executor.model.ProxyConfigHolder;
import group.executor.service.handler.ProxySourceQueueHandler;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Service
@PropertySource("classpath:application.properties")
public class DefaultProxySourceFile implements ProxySourceFile {
    private static final String PROPERTY_KEY_SOURCE_FILE_PATH = "service.proxy.sourceFile";
    private static final String DEFAULT_SOURCE_FILE_PATH = "proxy.json";

    private final ProxySourceQueueHandler queueHandler;
    private final ObjectMapper mapper;
    private final Environment environment;

    private File source;

    public DefaultProxySourceFile(ProxySourceQueueHandler queueHandler, ObjectMapper mapper, Environment environment) {
        this.queueHandler = queueHandler;
        this.mapper = mapper;
        this.environment = environment;
    }

    @Override
    public void setSourceFile(File source) {
        this.source = source;
    }

    @Override
    public void extractProxy() throws IOException {
        if (source == null) {
            String filePath = environment.getProperty(PROPERTY_KEY_SOURCE_FILE_PATH, DEFAULT_SOURCE_FILE_PATH);
            source = new File(filePath);
        }

        queueHandler.addProxy(readProxy(source).toArray(new ProxyConfigHolder[0]));
    }

    private Collection<ProxyConfigHolder> readProxy(File file) throws IOException {
        return mapper.readValue(
                file,
                mapper.getTypeFactory().constructCollectionType(Collection.class, ProxyConfigHolder.class)
        );
    }
}
