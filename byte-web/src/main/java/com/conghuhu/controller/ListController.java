package com.conghuhu.controller;


import com.conghuhu.entity.List;
import com.conghuhu.params.ListMoveParam;
import com.conghuhu.params.ListParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.service.ListService;
import com.qiniu.util.Json;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author conghuhu
 * @since 2022-01-28
 */
@Api(tags = "列类")
@RestController
@RequestMapping("/list")
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @ApiOperation(value = "创建新列", notes = "创建新列", produces = "application/json")
    @PostMapping("/addList")
    public JsonResult<List> addList(@RequestBody ListParam listParam) {
        return listService.addList(listParam);
    }

    @ApiOperation(value = "归档列", notes = "归档列", produces = "application/json")
    @PostMapping("/closeList/{listId}")
    public JsonResult closeList(@PathVariable Long listId) {
        return listService.closeList(listId);
    }

    @ApiOperation(value = "删除列", notes = "删除列", produces = "application/json")
    @DeleteMapping("/removeList/{listId}")
    public JsonResult removeList(@PathVariable Long listId) {
        return listService.removeList(listId);
    }

    @ApiOperation(value = "编辑列名称", notes = "编辑列名称", produces = "application/json")
    @PostMapping("/editListName")
    public JsonResult editListName(@RequestParam("listId") Long listId, @RequestParam("listName") String listName) {
        return listService.editListName(listId, listName);
    }

    @ApiOperation(value = "编辑列背景颜色", notes = "编辑列背景颜色", produces = "application/json")
    @PostMapping("/editBackGroundColor")
    public JsonResult editBackGroundColor(@RequestParam("listId") Long listId, @RequestParam("backgroundColor") String backgroundColor) {
        return listService.editBackGroundColor(listId, backgroundColor);
    }

    @ApiOperation(value = "移动列", notes = "移动列", produces = "application/json")
    @PostMapping("/moveList")
    public JsonResult moveList(@RequestBody ListMoveParam listMoveParam) {
        return listService.moveList(listMoveParam);
    }
}
