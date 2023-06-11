package top.shareus.bot.robot.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.shareus.bot.common.domain.BlackList;
import top.shareus.bot.common.pojo.dto.BlackListSaveDTO;
import top.shareus.bot.robot.mapper.BlackListMapper;
import top.shareus.bot.robot.service.BlackListService;

import java.util.Date;
import java.util.List;

/**
 * 黑名单服务impl
 *
 * @author 17602
 * @description 针对表【black_list(黑名单)】的数据库操作Service实现
 * @createDate 2023-04-18 21:12:58
 * @date 2023/06/11
 */
@Slf4j
@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList>
		implements BlackListService {
	
	@Override
	public BlackList saveBlack(BlackListSaveDTO dto) {
		if (ObjectUtil.isNull(dto)) {
			return null;
		}
		
		// 已经存在不加
		List<BlackList> hasBlackList = lambdaQuery().eq(BlackList::getQqId, dto.getQqId()).list();
		if (! hasBlackList.isEmpty()) {
			log.info("已经在黑名单了");
			return null;
		}
		
		BlackList blackList = new BlackList();
		BeanUtils.copyProperties(dto, blackList);
		blackList.setCreateTime(new Date());
		
		save(blackList);
		
		log.info("已加入黑名单：{} -> {}", blackList.getNickName(), blackList.getCreateBy());
		return blackList;
	}
}




