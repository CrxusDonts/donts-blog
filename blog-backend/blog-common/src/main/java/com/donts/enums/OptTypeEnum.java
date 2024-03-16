package com.donts.enums;

import lombok.Getter;

@Getter
public enum OptTypeEnum {
    SAVE_OR_UPDATE("新增或修改"),
    SAVE("新增"),
    UPDATE("修改"),
    DELETE("删除"),
    UPLOAD_FILE("上传文件"),
    EXPORT("导出"),
    OTHER("其他");

    private final String description;

    OptTypeEnum(String description) {
        this.description = description;
    }

}
