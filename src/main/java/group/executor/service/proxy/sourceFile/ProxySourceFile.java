package group.executor.service.proxy.sourceFile;

import java.io.File;
import java.io.IOException;

public interface ProxySourceFile {
    void setSourceFile(File file);

    void extractProxy() throws IOException;
}
