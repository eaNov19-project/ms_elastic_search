package ea.sof.ms_elastic_search.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class ElasticConfig {

    @Value("${elasticsearch.host}")
    private String host;
    @Value("${elasticsearch.port}")
    private int port;

    @Bean
    Client client() {
        TransportClient client = null;
        try{
            Settings settings = Settings.builder()
                    .put("cluster.name", "elastic")
//                    .put("client.transport.sniff", false)
                    .build();
            client = new PreBuiltTransportClient(settings);
            client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));

            return client;
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}