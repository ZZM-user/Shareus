package top.shareus.bot.annotation;

import top.shareus.common.core.eumn.GroupEnum;

import java.lang.annotation.*;

/**
 * 群组
 *
 * @author zhaojl
 * @date 2023/02/25
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface Group {
    long groupId() default 0L;
    
    GroupEnum[] groupList() default {};
}
