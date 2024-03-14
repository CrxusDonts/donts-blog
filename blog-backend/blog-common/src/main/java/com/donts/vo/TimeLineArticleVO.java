package com.donts.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeLineArticleVO implements Serializable {

    private String time;

    private List<ArticleCardVO> articles;
}

