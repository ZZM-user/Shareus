package top.shareus.bot.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.shareus.common.security.utils.SecurityUtils;

import java.util.Date;

/**
 * Mybatis Plus自动注入
 *
 * @author 17602
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		Date date = new Date();
		String username = SecurityUtils.getUsername();
		this.strictInsertFill(metaObject, "createBy", String.class, username);
		this.strictInsertFill(metaObject, "createTime", Date.class, date);
		this.strictInsertFill(metaObject, "updateBy", String.class, username);
		this.strictInsertFill(metaObject, "updateTime", Date.class, date);
	}
	
	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, "updateBy", String.class, SecurityUtils.getUsername());
		this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
	}
}
