package top.shareus.bot.miniapp.contrroller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.shareus.bot.miniapp.config.aspect.TokenAuth;
import top.shareus.bot.miniapp.pojo.dto.UpdateUserInfoDTO;
import top.shareus.bot.miniapp.pojo.dto.WxLoginDTO;
import top.shareus.bot.miniapp.pojo.vo.WxUserInfoVO;
import top.shareus.bot.miniapp.service.WxUserInfoService;
import top.shareus.common.core.domain.R;

import javax.annotation.Resource;

@RequestMapping("/wx/user")
@RestController
public class WxUserController {
	
	@Resource
	private WxUserInfoService wxUserInfoService;
	
	@PostMapping("/login")
	public R<WxUserInfoVO> login(@RequestBody @Validated WxLoginDTO loginDTO) {
		WxUserInfoVO wxUserInfo = wxUserInfoService.login(loginDTO);
		return R.ok(wxUserInfo);
	}
	
	@TokenAuth
	@GetMapping("/getUserInfo")
	public R<WxUserInfoVO> getInfo() {
		WxUserInfoVO wxUserInfo = wxUserInfoService.getInfo();
		return R.ok(wxUserInfo);
	}
	
	@TokenAuth
	@PostMapping("/update")
	public R<WxUserInfoVO> updateUserInfo(@RequestBody @Validated UpdateUserInfoDTO updateUserInfoDTO) {
		WxUserInfoVO wxUserInfo = wxUserInfoService.updateUserInfo(updateUserInfoDTO);
		return R.ok(wxUserInfo);
	}
}
