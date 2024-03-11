package com.donts.enums;

import lombok.Getter;

@Getter
public enum ArticleTypeEnum {
    //文章类型 1原创 2转载 3翻译
    DEFAULT(0, "默认"),
    ORIGINAL(1, "原创"),
    REPRINT(2, "转载"),
    TRANSLATION(3, "翻译");

    private final Integer code;
    private final String desc;

    ArticleTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ArticleTypeEnum getEnumByCode(Integer code) {
        for (ArticleTypeEnum articleTypeEnum : ArticleTypeEnum.values()) {
            if (articleTypeEnum.getCode().equals(code)) {
                return articleTypeEnum;
            }
        }
        return DEFAULT;
    }

    public static ArticleTypeEnum getEnumByDesc(String desc) {
        for (ArticleTypeEnum articleTypeEnum : ArticleTypeEnum.values()) {
            if (articleTypeEnum.getDesc().equals(desc)) {
                return articleTypeEnum;
            }
        }
        return DEFAULT;
    }
}
