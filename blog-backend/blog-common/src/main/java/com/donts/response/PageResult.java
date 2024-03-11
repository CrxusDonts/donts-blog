package com.donts.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult<T extends Serializable> implements Serializable {

    private List<T> records;

    private Long count;

    private Long totalPage;

    private Long currentPage;

    private Long pageSize;

    public static <T extends Serializable> PageResult<T> of(List<T> records, Long count, Long totalPage, Long currentPage, Long pageSize) {
        return PageResult.<T>builder()
                .records(records)
                .count(count)
                .totalPage(totalPage)
                .currentPage(currentPage)
                .pageSize(pageSize)
                .build();
    }

    /**
     * convert from Page
     */
    public static <T extends Serializable> PageResult<T> of(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        return PageResult.<T>builder()
                .records(page.getRecords())
                .count(page.getTotal())
                .totalPage(page.getPages())
                .currentPage(page.getCurrent())
                .pageSize(page.getSize())
                .build();
    }
}
