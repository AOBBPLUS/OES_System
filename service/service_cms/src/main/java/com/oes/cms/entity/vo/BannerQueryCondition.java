package com.oes.cms.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "banner查询对象", description = "查询banner要封装的对象")
@Data
public class BannerQueryCondition {
    @ApiModelProperty(value = "banner标题")
    private String title;

    @ApiModelProperty(value = "查询开始时间", example = "2022-6-24 12:00:00")
    private String begin;

    @ApiModelProperty(value = "查询结束时间", example = "2022-6-24 12:00:00")
    private String end;
}
