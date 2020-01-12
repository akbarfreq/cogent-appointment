package com.cogent.cogentappointment.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
@Setter
@Getter
public class FileConfiguration {
    private String location = "cogent-uploads";

    public  String fileBasePath = "files/";
}