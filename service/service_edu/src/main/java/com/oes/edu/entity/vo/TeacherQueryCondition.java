package com.oes.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/*
 * 讲师条件查询时的封装类
 */
@ApiModel(value = "Teacher查询对象", description = "讲师查询对象封装")
@Data
public class TeacherQueryCondition {
    @ApiModelProperty(value = "教师名称,模糊查询")
    private String name;

    @ApiModelProperty(value = "头衔,1:高级讲师 2:首席讲师")
    private Integer level;

    @ApiModelProperty(value = "查询开始时间", example = "2022-6-24 12:00:00")
    private String begin;

    @ApiModelProperty(value = "查询结束时间", example = "2022-6-24 12:00:00")
    private String end;
}
