package graded.unit.lostmypetwebapp.client;

import graded.unit.lostmypetwebapp.config.WebClientProperties;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * Spring REST Client component used to connect and consume external REST Service
 */
@Component
public class WebClient {

    private final ClientHttpRequestFactory requestFactory;

    private final String serviceUrl;

    /**
     * Constructor used to inject properties to create HTTP connection management
     *
     * @param props custom properties of client connection
     */
    private WebClient(final WebClientProperties props) {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(props.getConnectTimeout())
                .setConnectionRequestTimeout(props.getConnectTimeout())
                .setSocketTimeout(props.getConnectTimeout())
                .build();
        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .build();

        // Instantiate HTTP client to create request
        this.requestFactory = new HttpComponentsClientHttpRequestFactory(client);

        this.serviceUrl = props.getServiceUrl();
    }

    /**
     * Method to call a Service
     *
     * @return {@link RestTemplate} object for client-side HTTP access
     */
    public RestTemplate getRestTemplate() {

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl));
        return restTemplate;
    }

}
