package be.abis.exercise;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class WebFluxXmlConfig implements WebFluxConfigurer {

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().jaxb2Encoder(new Jaxb2XmlEncoder());
        configurer.defaultCodecs().jaxb2Decoder(new Jaxb2XmlDecoder());
    }
}

