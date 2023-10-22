package top.shareus.bot.miniapp.contrroller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shareus.bot.miniapp.entity.WxUserInfo;
import top.shareus.bot.miniapp.pojo.dto.WxLoginDTO;
import top.shareus.bot.miniapp.service.WxUserInfoService;
import top.shareus.common.core.domain.R;

import javax.annotation.Resource;

@RequestMapping("/wx/user")
@RestController
public class WxUserController {
	
	@Resource
	private WxUserInfoService wxUserInfoService;
	
	@PostMapping("/login")
	public R<WxUserInfo> login(@RequestBody @Validated WxLoginDTO loginDTO) {
		WxUserInfo wxUserInfo = wxUserInfoService.login(loginDTO);
		return R.ok(wxUserInfo);
	}
}
