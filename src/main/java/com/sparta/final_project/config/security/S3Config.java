package com.sparta.final_project.config.security;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule()) // Support for LocalDateTime
                .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
    }
    // AWS 액세스 키 주입
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    // AWS 시크릿 키 주입
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;
    // AWS 리전 주입
    @Value("${cloud.aws.region.static}")
    private String region;

    // AmazonS3 클라이언트 빈 생성
    @Bean
    public AmazonS3 amazonS3Client() {
        // 액세스 키와 시크릿 키로 인증 자격 증명 생성
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        // Amazon S3 클라이언트 빌더로 클라이언트 설정
        return AmazonS3ClientBuilder.standard()
                .withRegion(region) // AWS 리전 설정
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)) // 인증 자격 증명 설정
                .build();
    }
}

