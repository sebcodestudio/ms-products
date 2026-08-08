package com.sebcode.msproducts.storage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class StorageConfig {

    @Bean
    public S3Client s3Client(
            @Value("${app.storage.endpoint}") String endpoint,
            @Value("${app.storage.access-key}") String accessKey,
            @Value("${app.storage.secret-key}") String secretKey,
            @Value("${app.storage.region}") String region) {

        // AwsBasicCredentials.create() lanza NullPointerException si el access
        // key viene vacío — pasa en la construcción del bean, así que un
        // MINIO_ACCESS_KEY sin configurar tumbaría el arranque (o, con
        // lazy-init, la primera request que use el bean) en vez de fallar
        // limpio recién cuando de verdad se intente subir un archivo.
        String safeAccessKey = accessKey == null || accessKey.isBlank() ? "not-configured" : accessKey;
        String safeSecretKey = secretKey == null || secretKey.isBlank() ? "not-configured" : secretKey;

        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(safeAccessKey, safeSecretKey)))
                // MinIO y R2 sirven en formato path-style (endpoint/bucket/key), no
                // virtual-hosted-style (bucket.endpoint/key).
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                // Cliente HTTP liviano (JDK puro) en vez del Apache HttpClient por
                // defecto — ver nota en pom.xml sobre el cold-start del free tier.
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build();
    }
}
