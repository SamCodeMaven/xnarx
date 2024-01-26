package uz.xnarx.xnarx.configuration;


import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            String encodedSlash = "%2F";
            String encodedBackSlash = "%5C";
            connector.setProperty("relaxedPathChars", encodedSlash + encodedBackSlash);
            connector.setProperty("relaxedQueryChars", encodedSlash + encodedBackSlash);
        });
    }
}
