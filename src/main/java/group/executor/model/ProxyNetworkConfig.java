package group.executor.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyNetworkConfig {
    @JsonProperty("hostName")
    @JsonAlias("hostname")
    private String hostName;

    private Integer port;
}