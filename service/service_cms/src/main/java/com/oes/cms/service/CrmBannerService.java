package com.oes.cms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oes.cms.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oes.cms.entity.vo.BannerQueryCondition;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author zh
 * @since 2022-06-29
 */
public interface CrmBannerService extends IService<CrmBanner> {
    // 查询所有的banner数据,用redis作缓存
    List<CrmBanner> getAllBannerInfo();
    
    // 条件查询banner信息
    void pageQueryBannerByCondition(Page<CrmBanner> bannerPage, BannerQueryCondition queryCondition);
}
