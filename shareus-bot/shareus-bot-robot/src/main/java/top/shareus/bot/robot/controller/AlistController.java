package top.shareus.bot.robot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shareus.bot.common.pojo.dto.ResetMetaPasswordDTO;
import top.shareus.bot.robot.service.AlistService;
import top.shareus.common.core.domain.R;

import javax.annotation.Resource;

/**
 * alist Controller
 *
 * @author zhaojl
 * @date 2024-02-20
 */
@RestController
@RequestMapping("/alist")
public class AlistController {
	
	@Resource
	private AlistService alistService;
	
	/**
	 * 重置 群文件密码
	 */
	@PostMapping("/resetMetaPwd")
	public R resetMetaPassword(@RequestBody ResetMetaPasswordDTO dto) {
		alistService.resetMetaPassword(dto);
		return R.ok();
	}
}
