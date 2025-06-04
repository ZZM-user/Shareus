package top.shareus.bot.robot.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.shareus.bot.common.pojo.dto.ResetMetaPasswordDTO;
import top.shareus.bot.robot.service.AlistService;

import javax.annotation.Resource;

@Slf4j
@Component
public class RestGroupMetaPwdTask {
	@Resource
	private AlistService alistService;
	
	// @Scheduled(cron = "0 0 17 * * ?")
	public void run() {
		log.info("开始重置云盘群文件元数据密码……");
		ResetMetaPasswordDTO metaPasswordDTO = new ResetMetaPasswordDTO();
		metaPasswordDTO.setCancelPassword(false);
		metaPasswordDTO.setNotifyAdminGroup(true);
		metaPasswordDTO.setNotifyResourceGroup(true);
		
		alistService.resetMetaPassword(metaPasswordDTO);
		log.info("重置云盘群文件元数据密码结束");
	}
	
}
