package top.shareus.bot.robot.controller.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shareus.bot.robot.job.querylog.Feedback;
import top.shareus.bot.robot.job.querylog.Polling;
import top.shareus.common.core.domain.R;
import top.shareus.common.core.web.controller.BaseController;
import top.shareus.common.log.annotation.Log;
import top.shareus.common.log.enums.BusinessType;

/**
 * 求文日志Controller
 *
 * @author zhaojl
 * @date 2023-01-09
 */
@RestController
@RequestMapping("/job/query_log")
public class QueryLogJobController extends BaseController {
	
	@Autowired
	private Polling polling;
	
	@Autowired
	private Feedback feedback;
	
	
	/**
	 * 轮询求文日志结果
	 */
	@PostMapping("/polling")
	@Log(title = "轮询求文日志结果", businessType = BusinessType.OTHER)
	public R polling() {
		polling.execute();
		return R.ok();
	}
	
	/**
	 * 反馈求文日志结果
	 */
	@PostMapping("/feedback")
	@Log(title = "轮询求文日志结果", businessType = BusinessType.OTHER)
	public R feedback() {
		feedback.execute();
		return R.ok();
	}
	
}
