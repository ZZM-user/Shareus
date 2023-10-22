package top.shareus.bot.miniapp.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import top.shareus.bot.miniapp.config.exception.WxLoginException;
import top.shareus.bot.miniapp.entity.WxUserInfo;
import top.shareus.bot.miniapp.mapper.WxUserInfoMapper;
import top.shareus.bot.miniapp.pojo.dto.WxLoginDTO;
import top.shareus.bot.miniapp.service.WxUserInfoService;

import javax.annotation.Resource;

/**
 * @author 17602
 * @description 针对表【wx_user_info】的数据库操作Service实现
 * @createDate 2023-10-21 16:20:48
 */
@Slf4j
@Service
public class WxUserInfoServiceImpl extends ServiceImpl<WxUserInfoMapper, WxUserInfo>
		implements WxUserInfoService {
	
	@Resource
	private WxMaService wxMaService;
	
	@Override
	public WxUserInfo login(WxLoginDTO loginDTO) {
		try {
			WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(loginDTO.getCode());
			return saveOrUpdateUserInfo(session);
		} catch (WxErrorException e) {
			log.error(loginDTO.getCode(), e.getMessage(), e);
			throw new WxLoginException("登录失败");
		} finally {
			// 清理ThreadLocal
			WxMaConfigHolder.remove();
		}
	}
	
	private WxUserInfo saveOrUpdateUserInfo(WxMaJscode2SessionResult session) {
		String openid = session.getOpenid();
		WxUserInfo wxUserInfo = getWxUserInfo(openid);
		if (wxUserInfo == null) {
			log.info("新用户注册：{}", openid);
			wxUserInfo = new WxUserInfo();
			wxUserInfo.setOpenid(openid);
			wxUserInfo.setUnionid(session.getUnionid());
			wxUserInfo.setNickName("微信用户" + RandomUtil.randomNumbers(10));
			wxUserInfo.setAvatarUrl("https://imgs.shareus.top/i/2023/01/26/63d21b7d15d10.jpg");
			save(wxUserInfo);
		} else {
			log.info("老用户登录：{}", openid);
			updateById(wxUserInfo);
		}
		
		return wxUserInfo;
	}
	
	private WxUserInfo getWxUserInfo(String openid) {
		return getOne(new QueryWrapper<WxUserInfo>().eq("openid", openid));
	}
}




