package com.donts.blog.system;

import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.donts.response.UnifiedResp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    UnifiedResp<String> generatePreSignedUrlForFE(String objectName);

    UnifiedResp<String> uploadFileForFE(String url, MultipartFile file) throws IOException;

    UnifiedResp<String> deleteFileForFE(String url);

    UnifiedResp<S3Object> getFileForFE(String url);

    UnifiedResp<ListObjectsV2Result> pageObjectsForFE(int limit, String continuationToken);

    UnifiedResp<String> updateFileForFE(String url, MultipartFile newFile) throws IOException;
}
