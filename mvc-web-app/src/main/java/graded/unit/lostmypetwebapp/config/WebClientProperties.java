package graded.unit.lostmypetwebapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * POJO class of the custom configuration properties to automatically inject
 * properties into the class used to access to the external REST Service.
 */
@Component
@ConfigurationProperties("rest") // configuration prefix
public class WebClientProperties {

    /**
     * Service connect timeout - how long stay connected
     */
    private int connectTimeout;

    /**
     * Service base url address
     */
    private String serviceUrl;


    // Getters and setters

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
