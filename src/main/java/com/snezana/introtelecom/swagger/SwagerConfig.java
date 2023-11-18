package com.snezana.introtelecom.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.context.annotation.Bean;

@Configuration
public class SwagerConfig {

    @Value("${application.title}")
    private String APP_TITLE;

    @Bean
    public OpenAPI customOpenAPI(){
        final String apiTitle = String.format("%s API", StringUtils.capitalize(APP_TITLE));
        return new OpenAPI()
                .info(new Info().title(apiTitle).version("1"));
    }
}


//        .description("![flaticon](https://cdn-icons-png.flaticon.com/512/5721/5721522.png)"));

// https://cdn.iconscout.com/icon/free/png-512/free-mobile-maintenance-2-1120072.png