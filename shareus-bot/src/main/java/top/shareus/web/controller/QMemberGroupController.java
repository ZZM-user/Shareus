package top.shareus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shareus.common.core.utils.poi.ExcelUtil;
import top.shareus.common.core.web.controller.BaseController;
import top.shareus.common.core.web.domain.AjaxResult;
import top.shareus.common.core.web.page.TableDataInfo;
import top.shareus.common.log.annotation.Log;
import top.shareus.common.log.enums.BusinessType;
import top.shareus.common.security.annotation.RequiresPermissions;
import top.shareus.domain.entity.QMemberGroup;
import top.shareus.web.service.IQMemberGroupService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * QQ成员和群的关系Controller
 *
 * @author zhaojl
 * @date 2023-04-02
 */
@RestController
@RequestMapping("/member_group")
public class QMemberGroupController extends BaseController {
    @Autowired
    private IQMemberGroupService qMemberGroupService;
    
    /**
     * 查询QQ成员和群的关系列表
     */
    @RequiresPermissions("system:group:list")
    @GetMapping("/list")
    public TableDataInfo list(QMemberGroup qMemberGroup) {
        startPage();
        List<QMemberGroup> list = qMemberGroupService.selectQMemberGroupList(qMemberGroup);
        return getDataTable(list);
    }
    
    /**
     * 导出QQ成员和群的关系列表
     */
    @RequiresPermissions("system:group:export")
    @Log(title = "QQ成员和群的关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QMemberGroup qMemberGroup) {
        List<QMemberGroup> list = qMemberGroupService.selectQMemberGroupList(qMemberGroup);
        ExcelUtil<QMemberGroup> util = new ExcelUtil<QMemberGroup>(QMemberGroup.class);
        util.exportExcel(response, list, "QQ成员和群的关系数据");
    }
    
    /**
     * 获取QQ成员和群的关系详细信息
     */
    @RequiresPermissions("system:group:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(qMemberGroupService.selectQMemberGroupById(id));
    }
    
    /**
     * 新增QQ成员和群的关系
     */
    @RequiresPermissions("system:group:add")
    @Log(title = "QQ成员和群的关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QMemberGroup qMemberGroup) {
        return toAjax(qMemberGroupService.insertQMemberGroup(qMemberGroup));
    }
    
    /**
     * 修改QQ成员和群的关系
     */
    @RequiresPermissions("system:group:edit")
    @Log(title = "QQ成员和群的关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QMemberGroup qMemberGroup) {
        return toAjax(qMemberGroupService.updateQMemberGroup(qMemberGroup));
    }
    
    /**
     * 删除QQ成员和群的关系
     */
    @RequiresPermissions("system:group:remove")
    @Log(title = "QQ成员和群的关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(qMemberGroupService.deleteQMemberGroupByIds(ids));
    }
}
