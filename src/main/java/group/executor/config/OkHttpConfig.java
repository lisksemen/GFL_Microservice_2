package group.executor.config;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpConfig {
    @Bean
    @Qualifier("okHttpClientTestProxy")
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .callTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    @Qualifier("testProxyRequest")
    public Request request() throws MalformedURLException {
        URL url = new URL("https://public.freeproxyapi.com/api/Proxy/ProxyByType/0/3");
        return new Request.Builder()
                .get()
                .url(url)
                .build();
    }
}
