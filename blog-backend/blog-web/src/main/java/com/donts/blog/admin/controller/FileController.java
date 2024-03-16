package com.donts.blog.admin.controller;

import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.donts.blog.system.FileService;
import com.donts.response.UnifiedResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Tag(name = "FileController", description = "文件模块")
@RequestMapping("/admin/api/file")
public class FileController {

    @Resource
    private FileService fileService;

    @Operation(summary = "生成预签名URL")
    @GetMapping("/generate-url")
    public UnifiedResp<String> generatePreSignedUrl(@RequestParam("objectName") String objectName) {
        return fileService.generatePreSignedUrlForFE(objectName);
    }

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public UnifiedResp<String> uploadFile(@RequestParam("url") String url, @RequestParam("file") MultipartFile file) throws IOException {
        return fileService.uploadFileForFE(url, file);
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/delete")
    public UnifiedResp<String> deleteFile(@RequestParam("url") String url) {
        return fileService.deleteFileForFE(url);
    }

    @Operation(summary = "获取文件")
    @GetMapping("/get")
    public UnifiedResp<S3Object> getFile(@RequestParam("url") String url) {
        //TODO：不对，这里应该返回文件的URL，而不是S3Object对象
        return fileService.getFileForFE(url);
    }

    @Operation(summary = "分页列出S3存储桶中的对象")
    @GetMapping("/page-objects")
    public UnifiedResp<ListObjectsV2Result> pageObjects(@RequestParam("limit") int limit, @RequestParam(value =
            "continuationToken", required = false) String continuationToken) {
        //TODO：不对，这里应该返回文件的URL，而不是S3Object对象
        return fileService.pageObjectsForFE(limit, continuationToken);
    }

    @Operation(summary = "更新文件")
    @PostMapping("/update")
    public UnifiedResp<String> updateFile(@RequestParam("url") String url,
                                          @RequestParam("newFile") MultipartFile newFile) throws IOException {
        return fileService.updateFileForFE(url, newFile);
    }
}