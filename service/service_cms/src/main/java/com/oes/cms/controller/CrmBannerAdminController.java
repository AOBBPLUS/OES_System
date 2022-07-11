package com.oes.cms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oes.cms.entity.CrmBanner;
import com.oes.cms.entity.vo.BannerQueryCondition;
import com.oes.cms.service.CrmBannerService;
import com.oes.commonutils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "后台的banner管理控制器")
@RestController
@RequestMapping("/cmsservice/bannerAdmin")
public class CrmBannerAdminController {
    private CrmBannerService bannerService;

    // 推荐使用set注入
    @Autowired
    public void setBannerService(CrmBannerService bannerService) {
        this.bannerService = bannerService;
    }

    @ApiOperation(value = "分页查询banner信息")
    @PostMapping("/pageQueryBannerInfo/{index}/{limit}")
    public Result pageQueryBanner(
            @ApiParam(name = "index", value = "当前页", required = true)
            @PathVariable("index") Long index,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable("limit") Long limit,
            @ApiParam(name = "queryCondition", value = "banner查询条件", required = false)
            @RequestBody(required = false) BannerQueryCondition queryCondition) {
        // 创建page对象
        Page<CrmBanner> bannerPage = new Page<>(index, limit);
        bannerService.pageQueryBannerByCondition(bannerPage, queryCondition);
        return Result.ok().data("banners", bannerPage.getRecords()).data("total", bannerPage.getTotal());
    }

    @ApiOperation(value = "根据id获取banner信息")
    @GetMapping("getBannerInfo/{bannerId}")
    public Result getBannerInfoById(
            @ApiParam(name = "bannerId", value = "要查询的banner的Id", required = true)
            @PathVariable String bannerId) {
        CrmBanner banner = bannerService.getById(bannerId);
        return Result.ok().data("banner", banner);
    }

    @ApiOperation(value = "添加banner信息")
    @PostMapping("addBannerInfo")
    public Result addBannerInfo(
            @ApiParam(name = "crmBanner", value = "要加入的banner数据", required = true)
            @RequestBody CrmBanner banner) {
        bannerService.save(banner);
        return Result.ok();
    }

    @ApiOperation(value = "更新banner信息")
    @PostMapping("updateBannerInfo")
    public Result updateBannerInfo(
            @ApiParam(name = "banner", value = "要添加的banner数据", required = true)
            @RequestBody CrmBanner banner) {
        bannerService.updateById(banner);
        return Result.ok();
    }

    @ApiOperation(value = "根据banner id删除banner数据")
    @DeleteMapping("deleteBannerInfo/{bannerId}")
    public Result deleteBannerInfo(
            @ApiParam(name = "bannerId", value = "要删除的bannerId", required = true)
            @PathVariable String bannerId) {
        bannerService.removeById(bannerId);
        return Result.ok();
    }
}

