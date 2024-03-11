package com.donts.enums;

import lombok.Getter;

@Getter
public enum ArticleStatusEnum {
    /**
     * 状态值 1公开 2私密 3草稿
     */
    DEFAULT(0, "默认"),
    PUBLIC(1, "公开"),
    PRIVATE(2, "私密"),
    DRAFT(3, "草稿");

    private final Integer code;
    private final String desc;

    ArticleStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ArticleStatusEnum getEnumByCode(Integer code) {
        for (ArticleStatusEnum articleStatusEnum : ArticleStatusEnum.values()) {
            if (articleStatusEnum.getCode().equals(code)) {
                return articleStatusEnum;
            }
        }
        return DEFAULT;
    }

    public static ArticleStatusEnum getEnumByDesc(String desc) {
        for (ArticleStatusEnum articleStatusEnum : ArticleStatusEnum.values()) {
            if (articleStatusEnum.getDesc().equals(desc)) {
                return articleStatusEnum;
            }
        }
        return DEFAULT;
    }

}
