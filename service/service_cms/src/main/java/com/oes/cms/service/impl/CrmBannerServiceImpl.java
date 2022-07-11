package com.oes.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oes.cms.entity.CrmBanner;
import com.oes.cms.entity.vo.BannerQueryCondition;
import com.oes.cms.mapper.CrmBannerMapper;
import com.oes.cms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author zh
 * @since 2022-06-29
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    // 查询所有的banner数据,用redis作缓存
    @Cacheable(value = "banner", key = "'getAllBannerInfo'")
    @Override
    public List<CrmBanner> getAllBannerInfo() {
        return baseMapper.selectList(null);
    }

    @Override
    public void pageQueryBannerByCondition(Page<CrmBanner> bannerPage, BannerQueryCondition queryCondition) {
        QueryWrapper<CrmBanner> wrapper = new QueryWrapper<>();
        if (queryCondition == null) {
            baseMapper.selectPage(bannerPage, null);
            return;
        }
        String title = queryCondition.getTitle();
        String begin = queryCondition.getBegin();
        String end = queryCondition.getEnd();

        //条件查询
        if (!StringUtils.isEmpty(title)) wrapper.like("title", title);
        if (!StringUtils.isEmpty(begin)) wrapper.ge("gmt_create", begin);
        if (!StringUtils.isEmpty(end)) wrapper.le("gmt_modified", end);

        //排序
        wrapper.orderByDesc("gmt_create");

        baseMapper.selectPage(bannerPage, wrapper);
    }
}
