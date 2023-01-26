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
import top.shareus.domain.entity.QueryLog;
import top.shareus.domain.vo.BatchChangeStatusVO;
import top.shareus.web.service.IQueryLogService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 求文日志Controller
 *
 * @author zhaojl
 * @date 2023-01-09
 */
@RestController
@RequestMapping("/qiuwen_log")
public class QueryLogController extends BaseController {
    @Autowired
    private IQueryLogService queryLogService;

    /**
     * 查询求文日志列表
     */
    @RequiresPermissions("mirai:qiuwen_log:list")
    @GetMapping("/list")
    public TableDataInfo list(QueryLog queryLog) {
        startPage();
        List<QueryLog> list = queryLogService.selectQueryLogList(queryLog);
        return getDataTable(list);
    }

    /**
     * 导出求文日志列表
     */
    @RequiresPermissions("mirai:qiuwen_log:export")
    @Log(title = "求文日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QueryLog queryLog) {
        List<QueryLog> list = queryLogService.selectQueryLogList(queryLog);
        ExcelUtil<QueryLog> util = new ExcelUtil<QueryLog>(QueryLog.class);
        util.exportExcel(response, list, "求文日志数据");
    }

    /**
     * 获取求文日志详细信息
     */
    @RequiresPermissions("mirai:qiuwen_log:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(queryLogService.selectQueryLogById(id));
    }

    /**
     * 新增求文日志
     */
    @RequiresPermissions("mirai:qiuwen_log:add")
    @Log(title = "求文日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QueryLog queryLog) {
        return toAjax(queryLogService.insertQueryLog(queryLog));
    }

    /**
     * 修改求文日志
     */
    @RequiresPermissions("mirai:qiuwen_log:edit")
    @Log(title = "求文日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QueryLog queryLog) {
        return toAjax(queryLogService.updateQueryLog(queryLog));
    }

    /**
     * 删除求文日志
     */
    @RequiresPermissions("mirai:qiuwen_log:remove")
    @Log(title = "求文日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(queryLogService.deleteQueryLogByIds(ids));
    }

    /**
     * 修改求文日志状态
     */
    @RequiresPermissions("mirai:qiuwen_log:edit")
    @Log(title = "求文日志", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody BatchChangeStatusVO vo) {
        return toAjax(queryLogService.batchChangeStatus(vo));
    }

}
