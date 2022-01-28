package com.conghuhu.controller;


import com.conghuhu.params.TagParam;
import com.conghuhu.result.JsonResult;
import com.conghuhu.service.TagService;
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
 * @since 2022-01-27
 */
@Api(tags = "标签类")
@RestController
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @ApiOperation(value = "创建新标签", notes = "创建新标签", produces = "application/json")
    @PostMapping("/create")
    public JsonResult createTag(@RequestBody TagParam tagParam) {
        return tagService.createTag(tagParam);
    }

    @ApiOperation(value = "修改标签", notes = "修改标签", produces = "application/json")
    @PostMapping("/modify")
    public JsonResult updateTag(@RequestBody TagParam tagParam) {
        return tagService.updateTag(tagParam);
    }

    @ApiOperation(value = "获取当前项目的标签", notes = "获取当前项目的标签", produces = "application/json")
    @GetMapping("/getTagsByProductId")
    public JsonResult getTagsByProductId(@RequestParam("productId") Long productId) {
        return tagService.getTagsByProductId(productId);
    }

    @ApiOperation(value = "根据id删除对应的标签", notes = "根据id删除对应的标签", produces = "application/json")
    @PostMapping("/removeTagById/{id}")
    public JsonResult removeTagById(@PathVariable Long id) {
        return tagService.removeTagById(id);
    }

    @ApiOperation(value = "根据cardId设置对应的标签", notes = "根据cardId设置对应的标签", produces = "application/json")
    @PostMapping("/{tagId}/setTagByCardId/{cardId}/")
    public JsonResult setTagByCardId(@PathVariable Long tagId, @PathVariable Long cardId) {
        return tagService.setTagByCardId(tagId, cardId);
    }
}
