package top.shareus.bot.robot.service;

import top.shareus.bot.common.pojo.dto.ResetMetaPasswordDTO;

import java.io.File;

/**
 * Alist服务
 *
 * @author 17602
 * @date 2023/05/21
 */
public interface AlistService {
	
	String uploadFile(File file);
	
	void resetMetaPassword(ResetMetaPasswordDTO dto);
}
