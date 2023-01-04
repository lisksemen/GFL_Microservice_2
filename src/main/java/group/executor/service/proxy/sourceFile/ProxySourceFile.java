package group.executor.service.proxy.sourceFile;

import java.io.File;
import java.io.IOException;

public interface ProxySourceFile {
    void extractProxy(File source) throws IOException;
}
