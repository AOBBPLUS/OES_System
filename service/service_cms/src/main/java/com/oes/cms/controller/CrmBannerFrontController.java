package com.oes.cms.controller;

import com.oes.cms.entity.CrmBanner;
import com.oes.cms.service.CrmBannerService;
import com.oes.commonutils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "前台的banner管理控制器")
@RestController
@RequestMapping("/cmsservice/bannerFront")
public class CrmBannerFrontController {
    private CrmBannerService bannerService;

    @Autowired
    public void setBannerService(CrmBannerService bannerService) {
        this.bannerService = bannerService;
    }

    @ApiOperation(value = "获取全部的banner数据")
    @GetMapping("getAllBannerInfo")
    public Result getAllBannerInfo() {
        List<CrmBanner> list = bannerService.getAllBannerInfo();
        return Result.ok().data("list", list);
    }
}
