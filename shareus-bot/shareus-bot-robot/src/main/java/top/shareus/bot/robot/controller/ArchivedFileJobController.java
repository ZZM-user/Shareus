package top.shareus.bot.robot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.shareus.bot.robot.job.archived.Day;
import top.shareus.bot.robot.job.archived.Month;
import top.shareus.bot.robot.job.archived.Week;
import top.shareus.common.core.domain.R;
import top.shareus.common.core.web.controller.BaseController;
import top.shareus.common.log.annotation.Log;
import top.shareus.common.log.enums.BusinessType;

/**
 * 归档文件Controller
 *
 * @author zhaojl
 * @date 2023-01-09
 */
@RestController
@RequestMapping("/job/archived_file")
public class ArchivedFileJobController extends BaseController {
	@Autowired
	private Day day;
	@Autowired
	private Week week;
	@Autowired
	private Month month;
	
	/**
	 * 轮询求文日志结果
	 */
	@PostMapping("/day")
	@Log(title = "每日总结", businessType = BusinessType.OTHER)
	public R day() {
		day.execute();
		return R.ok();
	}
	
	
	/**
	 * 每周总结
	 */
	@PostMapping("/week")
	@Log(title = "每周总结", businessType = BusinessType.OTHER)
	public R week() {
		week.execute();
		return R.ok();
	}
	
	/**
	 * 每月总结
	 */
	@PostMapping("/month")
	@Log(title = "每月总结", businessType = BusinessType.OTHER)
	public R month() {
		month.execute();
		return R.ok();
	}
	
}
