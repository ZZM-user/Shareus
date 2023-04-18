package top.shareus.bot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.shareus.bot.mapper.mapper.BlackListMapper;
import top.shareus.bot.service.BlackListService;
import top.shareus.domain.BlackList;

/**
 * @author 17602
 * @description 针对表【black_list(黑名单)】的数据库操作Service实现
 * @createDate 2023-04-18 21:12:58
 */
@Service
public class BlackListServiceImpl extends ServiceImpl<BlackListMapper, BlackList>
		implements BlackListService {
	
}




