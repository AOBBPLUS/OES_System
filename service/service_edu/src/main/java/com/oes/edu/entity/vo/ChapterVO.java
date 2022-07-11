package com.oes.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "章节VO", description = "传输章节的VO类")
@Data
public class ChapterVO {
    @ApiModelProperty(value = "章节id")
    private String id;

    @ApiModelProperty(value = "章节名称")
    private String title;

    @ApiModelProperty(value = "当前章节下的子小节")
    private List<VideoVO> children = new ArrayList<>();

    public void addChild(VideoVO video) {
        if (video != null) children.add(video);
    }
}
