package top.shareus.bot.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.shareus.bot.miniapp.constant.OperateLogEnum;
import top.shareus.bot.miniapp.entity.OperateLog;
import top.shareus.bot.miniapp.entity.WxUserInfo;

/**
 * @author 17602
 * @description 针对表【operate_log】的数据库操作Service
 * @createDate 2024-01-28 14:35:59
 */
public interface OperateLogService extends IService<OperateLog> {
	
	OperateLog saveForLogin(WxUserInfo userInfo, OperateLogEnum.ActionEnum action);
}
