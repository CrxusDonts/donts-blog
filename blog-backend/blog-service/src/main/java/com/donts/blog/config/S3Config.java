package com.donts.blog.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * S3配置（兼容s3协议即可，不一定是AWS S3）
 * COS、OSS、Cloudflare R2等都兼容S3协议
 */
@Configuration
@Data
public class S3Config {
    @Value("${object.storage.serviceEndpoint}")
    private String serviceEndpoint;

    @Value("${object.storage.region}")
    private String region;

    @Value("${object.storage.accessKey}")
    private String accessKey;

    @Value("${object.storage.accessSecret}")
    private String accessSecret;

    @Value("${object.storage.bucketName}")
    private String bucketName;


    @Bean
    public AmazonS3 s3client() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, accessSecret);
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        serviceEndpoint,
                        region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
