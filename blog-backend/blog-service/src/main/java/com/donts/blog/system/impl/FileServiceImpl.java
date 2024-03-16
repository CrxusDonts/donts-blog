package com.donts.blog.system.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.donts.blog.config.S3Config;
import com.donts.blog.system.FileService;
import com.donts.response.UnifiedResp;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private AmazonS3 s3Client;

    private String bucketName;

    @Resource
    private S3Config s3Config;

    @PostConstruct
    public void init() {
        bucketName = s3Config.getBucketName();
    }

    private String extractObjectKeyFromUrl(String url) {
        // 从URL中提取对象键。例如：https://bucketName.s3.region.amazonaws.com/bucketName/yy/mm/dd/objectKey
        //从bucketName/后面就是对象的键
        return url.substring(url.indexOf(bucketName) + bucketName.length() + 1);
    }

    @Override
    public UnifiedResp<String> generatePreSignedUrlForFE(String objectName) {
        String objectKey = String.format("%tF", new Date()) + "/" + UUID.randomUUID() + objectName;
        log.info("objectKey: {}", objectKey);
        String preSignedUrl = generatePreSignedUrl(objectKey);
        return UnifiedResp.success(preSignedUrl);
    }

    @Override
    public UnifiedResp<String> uploadFileForFE(String url, MultipartFile file) throws IOException {
        String objectKey = extractObjectKeyFromUrl(url);
        uploadFile(objectKey, file);
        return UnifiedResp.success();
    }

    @Override
    public UnifiedResp<String> deleteFileForFE(String url) {
        String objectKey = extractObjectKeyFromUrl(url);
        deleteFile(objectKey);
        return UnifiedResp.success();
    }

    @Override
    public UnifiedResp<S3Object> getFileForFE(String url) {
        String objectKey = extractObjectKeyFromUrl(url);
        S3Object file = getFile(objectKey);
        return UnifiedResp.success(file);
    }

    @Override
    public UnifiedResp<ListObjectsV2Result> pageObjectsForFE(int limit, String continuationToken) {
        ListObjectsV2Result result = pageObjects(limit, continuationToken);
        return UnifiedResp.success(result);
    }

    @Override
    public UnifiedResp<String> updateFileForFE(String url, MultipartFile newFile) throws IOException {
        String objectKey = extractObjectKeyFromUrl(url);
        updateFile(objectKey, newFile);
        return UnifiedResp.success();
    }

    /**
     * 生成预签名URL
     *
     * @param objectKey 对象键
     * @return 预签名URL
     */
    private String generatePreSignedUrl(String objectKey) {
        // 设置URL过期时间为30分钟
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 30;
        expiration.setTime(expTimeMillis);

        // 生成预签名URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, objectKey)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    /**
     * 上传文件到S3
     *
     * @param objectKey 对象键，即文件名，如：image.png
     * @param file      文件
     */
    private void uploadFile(String objectKey, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(bucketName, objectKey, inputStream, metadata);
        }
    }

    /**
     * 从S3删除文件
     *
     * @param objectKey 对象键，即文件名，如：image.png
     */
    private void deleteFile(String objectKey) {
        s3Client.deleteObject(bucketName, objectKey);
    }

    /**
     * 从S3获取文件
     *
     * @param objectKey 对象键，即文件名，如：image.png
     */
    private S3Object getFile(String objectKey) {
        return s3Client.getObject(bucketName, objectKey);
    }


    /**
     * 分页列出S3存储桶中的对象
     * <p>
     * 这个方法需要一个continuationToken参数。这个参数是一个分页标记，你可以使用这个标记来获取下一页的对象。如果这是你第一次获取对象，你可以将这个参数设置为null
     *
     * @param limit             每页的对象数量
     * @param continuationToken 分页标记
     * @return 对象列表和下一个分页标记
     */
    private ListObjectsV2Result pageObjects(int limit, String continuationToken) {
        ListObjectsV2Request req =
                new ListObjectsV2Request().withBucketName(bucketName).withMaxKeys(limit).withContinuationToken(continuationToken);
        return s3Client.listObjectsV2(req);
    }

    /**
     * 更新S3中的文件
     *
     * @param objectKey 对象键，即文件名，如：image.png
     * @param newFile   新文件
     */
    private void updateFile(String objectKey, MultipartFile newFile) throws IOException {
        // 删除旧文件
        deleteFile(objectKey);

        // 上传新文件
        uploadFile(objectKey, newFile);
    }
}