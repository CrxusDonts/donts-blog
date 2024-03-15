package com.donts.blog.aop.anno;


import com.donts.enums.OptTypeEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {
    /**
     * @return 操作类型
     * @see OptTypeEnum
     */
    OptTypeEnum optType() default OptTypeEnum.OTHER;
}
