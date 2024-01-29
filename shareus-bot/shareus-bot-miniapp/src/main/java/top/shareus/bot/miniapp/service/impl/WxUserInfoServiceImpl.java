package top.shareus.bot.miniapp.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;
import top.shareus.bot.miniapp.config.exception.WxLoginException;
import top.shareus.bot.miniapp.constant.OperateLogEnum;
import top.shareus.bot.miniapp.entity.OperateLog;
import top.shareus.bot.miniapp.entity.WxUserInfo;
import top.shareus.bot.miniapp.mapper.WxUserInfoMapper;
import top.shareus.bot.miniapp.pojo.dto.UpdateUserInfoDTO;
import top.shareus.bot.miniapp.pojo.dto.WxLoginDTO;
import top.shareus.bot.miniapp.pojo.vo.WxUserInfoVO;
import top.shareus.bot.miniapp.service.MiniAppTokenService;
import top.shareus.bot.miniapp.service.OperateLogService;
import top.shareus.bot.miniapp.service.WxUserInfoService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Optional;

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
	@Resource
	private MiniAppTokenService miniAppTokenService;
	@Resource
	private OperateLogService operateLogService;
	
	@Override
	public WxUserInfoVO login(WxLoginDTO loginDTO) {
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
	
	private WxUserInfoVO saveOrUpdateUserInfo(WxMaJscode2SessionResult session) {
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
			
			operateLogService.saveForLogin(wxUserInfo, OperateLogEnum.ActionEnum.REGISTER);
		} else {
			log.info("老用户登录：{}", openid);
			
			if (wxUserInfo.getEnabled() == 1) {
				log.info("禁用用户登录：{}", openid);
				throw new WxLoginException("您的账户已被禁用，请联系管理员处理！");
			}
			
			// 获取上次登录时间
			Optional<OperateLog> operateLogOptional = operateLogService.lambdaQuery()
					.eq(OperateLog::getUserId, wxUserInfo.getId())
					.eq(OperateLog::getAction, OperateLogEnum.ActionEnum.LOGIN)
					.orderByDesc(OperateLog::getCreateTime)
					.last("limit 1")
					.oneOpt();
			if (operateLogOptional.isPresent()) {
				wxUserInfo.setLastLoginTime(operateLogOptional.get().getCreateTime());
			}
			updateById(wxUserInfo);
		}
		
		// 插入登录记录
		operateLogService.saveForLogin(wxUserInfo, OperateLogEnum.ActionEnum.LOGIN);
		
		WxUserInfoVO wxUserInfoVO = BeanUtil.copyProperties(wxUserInfo, WxUserInfoVO.class);
		// 生成token
		String token = miniAppTokenService.generateAndSaveToken(wxUserInfo.getId());
		wxUserInfoVO.setToken(token);
		
		return wxUserInfoVO;
	}
	
	private WxUserInfo getWxUserInfo(String openid) {
		return getOne(new QueryWrapper<WxUserInfo>().eq("openid", openid));
	}
	
	@Override
	public WxUserInfoVO updateUserInfo(UpdateUserInfoDTO updateUserInfoDTO) {
		Long currentUserId = miniAppTokenService.getCurrentUserId();
		
		WxUserInfo wxUserInfo = this.getById(currentUserId);
		wxUserInfo.setAvatarUrl(updateUserInfoDTO.avatarUrl());
		wxUserInfo.setNickName(updateUserInfoDTO.nickName());
		wxUserInfo.setUpdateTime(new Date());
		updateById(wxUserInfo);
		
		return BeanUtil.copyProperties(wxUserInfo, WxUserInfoVO.class);
	}
	
	@Override
	public WxUserInfoVO getInfo() {
		Long currentUserId = miniAppTokenService.getCurrentUserId();
		
		WxUserInfo wxUserInfo = this.getById(currentUserId);
		return BeanUtil.copyProperties(wxUserInfo, WxUserInfoVO.class);
	}
}




