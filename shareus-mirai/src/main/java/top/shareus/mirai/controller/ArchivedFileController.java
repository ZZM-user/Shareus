package top.shareus.mirai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.shareus.common.core.utils.poi.ExcelUtil;
import top.shareus.common.core.web.controller.BaseController;
import top.shareus.common.core.web.domain.AjaxResult;
import top.shareus.common.core.web.page.TableDataInfo;
import top.shareus.common.log.annotation.Log;
import top.shareus.common.log.enums.BusinessType;
import top.shareus.common.security.annotation.RequiresPermissions;
import top.shareus.mirai.domain.ArchivedFile;
import top.shareus.mirai.service.IArchivedFileService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 归档文件Controller
 *
 * @author zhaojl
 * @date 2023-01-09
 */
@RestController
@RequestMapping("/archived_file")
public class ArchivedFileController extends BaseController {
    @Autowired
    private IArchivedFileService archivedFileService;

    /**
     * 查询归档文件列表
     */
    @RequiresPermissions("mirai:mirai:list")
    @GetMapping("/list")
    public TableDataInfo list(ArchivedFile archivedFile) {
        startPage();
        List<ArchivedFile> list = archivedFileService.selectArchivedFileList(archivedFile);
        return getDataTable(list);
    }

    /**
     * 导出归档文件列表
     */
    @RequiresPermissions("mirai:mirai:export")
    @Log(title = "归档文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ArchivedFile archivedFile) {
        List<ArchivedFile> list = archivedFileService.selectArchivedFileList(archivedFile);
        ExcelUtil<ArchivedFile> util = new ExcelUtil<ArchivedFile>(ArchivedFile.class);
        util.exportExcel(response, list, "归档文件数据");
    }

    /**
     * 获取归档文件详细信息
     */
    @RequiresPermissions("mirai:mirai:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(archivedFileService.selectArchivedFileById(id));
    }

    /**
     * 新增归档文件
     */
    @RequiresPermissions("mirai:mirai:add")
    @Log(title = "归档文件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ArchivedFile archivedFile) {
        return toAjax(archivedFileService.insertArchivedFile(archivedFile));
    }

    /**
     * 修改归档文件
     */
    @RequiresPermissions("mirai:mirai:edit")
    @Log(title = "归档文件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ArchivedFile archivedFile) {
        return toAjax(archivedFileService.updateArchivedFile(archivedFile));
    }

    /**
     * 删除归档文件
     */
    @RequiresPermissions("mirai:mirai:remove")
    @Log(title = "归档文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(archivedFileService.deleteArchivedFileByIds(ids));
    }
}
