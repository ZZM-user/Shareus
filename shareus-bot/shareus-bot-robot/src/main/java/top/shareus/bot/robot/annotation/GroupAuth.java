package top.shareus.bot.robot.annotation;


import top.shareus.bot.common.eumn.bot.GroupEnum;

import java.lang.annotation.*;

/**
 * 群组
 *
 * @author zhaojl
 * @date 2023/02/25
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface GroupAuth {
	GroupEnum[] allowGroupList() default {};
	
}
