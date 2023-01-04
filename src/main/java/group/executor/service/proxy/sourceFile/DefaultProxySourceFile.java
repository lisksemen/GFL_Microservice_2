package group.executor.service.proxy.sourceFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import group.executor.model.ProxyConfigHolder;
import group.executor.service.handler.ProxySourceQueueHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Service
public class DefaultProxySourceFile implements ProxySourceFile {

    private final ProxySourceQueueHandler queueHandler;
    private final ObjectMapper mapper;

    @Autowired
    public DefaultProxySourceFile(ProxySourceQueueHandler queueHandler, ObjectMapper mapper) {
        this.queueHandler = queueHandler;
        this.mapper = mapper;
    }

    private Collection<ProxyConfigHolder> readProxy(File file) throws IOException {
        return mapper.readValue(
                file,
                mapper.getTypeFactory().constructCollectionType(Collection.class, ProxyConfigHolder.class)
        );
    }

    @Override
    public void extractProxy(File source) throws IOException {
        queueHandler.addProxy(readProxy(source).toArray(new ProxyConfigHolder[0]));
    }
}
