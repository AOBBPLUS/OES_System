package com.oes.edu.entity.subject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "层级学科", description = "LevelSubject可层层嵌套,用于表示n级科目")
@Data
public class LevelSubject {
    @ApiModelProperty(value = "学科id")
    private String id;

    @ApiModelProperty(value = "学科名")
    private String title;

    //树形结构
    //一个一级分类包含1个或多个二级分类
    //data2: [{
    //        id: 1,
    //        label: '一级分类 1',
    //        children: [{
    //          id: 4,
    //          label: '二级分类 1-1',
    //          children : null
    //        }
    @ApiModelProperty(value = "子科目")
    private List<LevelSubject> children;

    public void initialize(boolean isLeaf) {
        if (isLeaf) return;
        //如果不是叶子结点则应该创建List
        children = new ArrayList<>();
    }

    public void addChild(LevelSubject child) {
        if (children != null && child != null) children.add(child);
    }
}
