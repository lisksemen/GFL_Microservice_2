package group.executor.service.publisher;

import com.solacesystems.jcsmp.JCSMPException;

public interface VPNPublisher {

    void publish() throws JCSMPException;
}
