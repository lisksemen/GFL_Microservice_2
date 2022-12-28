package group.executor.service.proxy.validator;

import group.executor.model.ProxyConfigHolder;
import group.executor.model.ProxyNetworkConfig;
import org.springframework.stereotype.Service;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.net.*;

@Service
public class DefaultProxyValidation implements ProxyValidation {
    @Override
    public boolean validateProxy(ProxyConfigHolder proxyConfigHolder) {
        if (proxyConfigHolder != null){
            try {
                OkHttpClient client = new OkHttpClient();
                ProxyNetworkConfig proxyNetworkConfig = proxyConfigHolder.getProxyNetworkConfig();
                URL url = new URL("https://2ip.io/ua/");
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Proxy proxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(proxyNetworkConfig.getHostName(),proxyNetworkConfig.getPort()));
                Call call = client.setProxy(proxy).newCall(request);
                if (call.execute().isSuccessful()){
                    return true;
                }
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(proxy);
//                boolean usingProxy = urlConnection.usingProxy();
//                if (urlConnection.getResponseCode() == 200 && usingProxy){
//                    return true;
//                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}