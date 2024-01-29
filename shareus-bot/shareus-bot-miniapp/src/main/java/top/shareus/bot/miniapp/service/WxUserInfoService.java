package top.shareus.bot.miniapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.shareus.bot.miniapp.entity.WxUserInfo;
import top.shareus.bot.miniapp.pojo.dto.UpdateUserInfoDTO;
import top.shareus.bot.miniapp.pojo.dto.WxLoginDTO;
import top.shareus.bot.miniapp.pojo.vo.WxUserInfoVO;

/**
 * @author 17602
 * @description 针对表【wx_user_info】的数据库操作Service
 * @createDate 2023-10-21 16:20:48
 */
public interface WxUserInfoService extends IService<WxUserInfo> {
	
	WxUserInfoVO login(WxLoginDTO loginDTO);
	
	WxUserInfoVO updateUserInfo(UpdateUserInfoDTO updateUserInfoDTO);
	
	WxUserInfoVO getInfo();
	
}
