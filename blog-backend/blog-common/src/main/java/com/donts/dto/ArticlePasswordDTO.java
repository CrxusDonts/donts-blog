package com.donts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文章密码")
public class ArticlePasswordDTO implements java.io.Serializable {
    @Schema(name = "articleId", description = "文章id", type = "Long")
    private Long articleId;
    @Schema(name = "articlePassword", description = "文章密码", type = "String")
    private String articlePassword;
}