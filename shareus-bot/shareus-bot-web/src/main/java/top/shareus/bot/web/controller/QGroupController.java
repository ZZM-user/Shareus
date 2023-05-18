package top.shareus.bot.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shareus.bot.common.domain.QGroup;
import top.shareus.bot.web.service.QGroupService;
import top.shareus.common.core.utils.poi.ExcelUtil;
import top.shareus.common.core.web.controller.BaseController;
import top.shareus.common.core.web.domain.AjaxResult;
import top.shareus.common.core.web.page.TableDataInfo;
import top.shareus.common.log.annotation.Log;
import top.shareus.common.log.enums.BusinessType;
import top.shareus.common.security.annotation.RequiresPermissions;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * QQ群组Controller
 *
 * @author zhaojl
 * @date 2023-04-02
 */
@RestController
@RequestMapping("/group")
public class QGroupController extends BaseController {
	@Autowired
	private QGroupService qGroupService;
	
	/**
	 * 查询QQ群组列表
	 */
	@RequiresPermissions("mirai:group:list")
	@GetMapping("/list")
	public TableDataInfo list(QGroup qGroup) {
		startPage();
		List<QGroup> list = qGroupService.selectQGroupList(qGroup);
		return getDataTable(list);
	}
	
	/**
	 * 导出QQ群组列表
	 */
	@RequiresPermissions("mirai:group:export")
	@Log(title = "QQ群组", businessType = BusinessType.EXPORT)
	@PostMapping("/export")
	public void export(HttpServletResponse response, QGroup qGroup) {
		List<QGroup> list = qGroupService.selectQGroupList(qGroup);
		ExcelUtil<QGroup> util = new ExcelUtil<QGroup>(QGroup.class);
		util.exportExcel(response, list, "QQ群组数据");
	}
	
	/**
	 * 获取QQ群组详细信息
	 */
	@RequiresPermissions("mirai:group:query")
	@GetMapping(value = "/{id}")
	public AjaxResult getInfo(@PathVariable("id") Long id) {
		return success(qGroupService.selectQGroupById(id));
	}
	
	/**
	 * 新增QQ群组
	 */
	@RequiresPermissions("mirai:group:add")
	@Log(title = "QQ群组", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@RequestBody QGroup qGroup) {
		return toAjax(qGroupService.insertQGroup(qGroup));
	}
	
	/**
	 * 修改QQ群组
	 */
	@RequiresPermissions("mirai:group:edit")
	@Log(title = "QQ群组", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@RequestBody QGroup qGroup) {
		return toAjax(qGroupService.updateQGroup(qGroup));
	}
	
	/**
	 * 删除QQ群组
	 */
	@RequiresPermissions("mirai:group:remove")
	@Log(title = "QQ群组", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
	public AjaxResult remove(@PathVariable Long[] ids) {
		return toAjax(qGroupService.deleteQGroupByIds(ids));
	}
}
