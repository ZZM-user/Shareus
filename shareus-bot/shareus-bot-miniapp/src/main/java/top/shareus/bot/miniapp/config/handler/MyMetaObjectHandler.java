package top.shareus.bot.miniapp.config.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

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
		this.strictInsertFill(metaObject, "createTime", Date.class, date);
	}
	
	@Override
	public void updateFill(MetaObject metaObject) {
		this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
	}
}
