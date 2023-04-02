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
import top.shareus.domain.entity.QMember;
import top.shareus.web.service.IQMemberService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * QQ成员Controller
 *
 * @author zhaojl
 * @date 2023-04-02
 */
@RestController
@RequestMapping("/member")
public class QMemberController extends BaseController {
    @Autowired
    private IQMemberService qMemberService;
    
    /**
     * 查询QQ成员列表
     */
    @RequiresPermissions("system:member:list")
    @GetMapping("/list")
    public TableDataInfo list(QMember qMember) {
        startPage();
        List<QMember> list = qMemberService.selectQMemberList(qMember);
        return getDataTable(list);
    }
    
    /**
     * 导出QQ成员列表
     */
    @RequiresPermissions("system:member:export")
    @Log(title = "QQ成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QMember qMember) {
        List<QMember> list = qMemberService.selectQMemberList(qMember);
        ExcelUtil<QMember> util = new ExcelUtil<QMember>(QMember.class);
        util.exportExcel(response, list, "QQ成员数据");
    }
    
    /**
     * 获取QQ成员详细信息
     */
    @RequiresPermissions("system:member:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(qMemberService.selectQMemberById(id));
    }
    
    /**
     * 新增QQ成员
     */
    @RequiresPermissions("system:member:add")
    @Log(title = "QQ成员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QMember qMember) {
        return toAjax(qMemberService.insertQMember(qMember));
    }
    
    /**
     * 修改QQ成员
     */
    @RequiresPermissions("system:member:edit")
    @Log(title = "QQ成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QMember qMember) {
        return toAjax(qMemberService.updateQMember(qMember));
    }
    
    /**
     * 删除QQ成员
     */
    @RequiresPermissions("system:member:remove")
    @Log(title = "QQ成员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(qMemberService.deleteQMemberByIds(ids));
    }
}
