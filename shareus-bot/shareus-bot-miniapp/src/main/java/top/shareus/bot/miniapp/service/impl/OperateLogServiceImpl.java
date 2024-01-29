package top.shareus.bot.miniapp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.shareus.bot.miniapp.constant.OperateLogEnum;
import top.shareus.bot.miniapp.entity.OperateLog;
import top.shareus.bot.miniapp.entity.WxUserInfo;
import top.shareus.bot.miniapp.mapper.OperateLogMapper;
import top.shareus.bot.miniapp.service.OperateLogService;

/**
 * @author 17602
 * @description 针对表【operate_log】的数据库操作Service实现
 * @createDate 2024-01-28 14:35:59
 */
@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog>
		implements OperateLogService {
	
	@Override
	public OperateLog saveForLogin(WxUserInfo userInfo, OperateLogEnum.ActionEnum action) {
		OperateLog operateLog = OperateLog.builder()
				.action(action.getValue())
				.userId(userInfo.getId())
				.build();
		
		save(operateLog);
		return operateLog;
	}
}




