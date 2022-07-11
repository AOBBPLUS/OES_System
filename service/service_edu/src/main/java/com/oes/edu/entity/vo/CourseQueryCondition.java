package com.oes.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "Course查询对象", description = "查询课程对象封装")
@Data
public class CourseQueryCondition implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程名")
    private String title;

    @ApiModelProperty(value = "课程状态")
    private String status;
}
