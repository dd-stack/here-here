package com.example.Here.global.dataconfig;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() throws JsonProcessingException {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();


        String secretName = "rds!db-b0a3f295-0802-415a-9b27-7415796322d0";
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().build();
        GetSecretValueRequest request = new GetSecretValueRequest().withSecretId(secretName);
        GetSecretValueResult result = client.getSecretValue(request);

        String secretString = result.getSecretString();


        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(secretString);


        String endpoint = jsonNode.get("host").asText();
        String port = jsonNode.get("port").asText();
        String username = jsonNode.get("username").asText();
        String password = jsonNode.get("password").asText();
        String database = jsonNode.get("dbname").asText();

        dataSource.setUrl("jdbc:mysql://" + endpoint + ":" + port + "/" + database);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        return dataSource;
    }



}



