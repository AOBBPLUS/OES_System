package com.oes.edu.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel(value = "ExcelSubjectData对象", description = "excel中每列对应的数据")
@Data
public class ExcelSubjectData {
    //一级科目名
    @ExcelProperty(index = 0)
    private String oneSubjectName;

    //二级科目名
    @ExcelProperty(index = 1)
    private String twoSubjectName;
}
