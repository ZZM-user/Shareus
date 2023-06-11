package top.shareus.bot.robot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.shareus.bot.common.domain.BlackList;
import top.shareus.bot.common.pojo.dto.BlackListSaveDTO;

/**
 * 黑名单服务
 *
 * @author 17602
 * @createDate 2023-04-18 21:12:58
 * @date 2023/06/11
 */
public interface BlackListService extends IService<BlackList> {
	
	public BlackList saveBlack(BlackListSaveDTO dto);
}
