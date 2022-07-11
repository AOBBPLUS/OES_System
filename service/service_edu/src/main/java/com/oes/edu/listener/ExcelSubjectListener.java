package com.oes.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.edu.entity.EduSubject;
import com.oes.edu.entity.excel.ExcelSubjectData;
import com.oes.edu.service.EduSubjectService;
import com.oes.servicebase.exceptionhandler.OesException;

import java.util.Map;

// 该类不能交给Spring处理
public class ExcelSubjectListener extends AnalysisEventListener<ExcelSubjectData> {
    // 科目的服务类
    private EduSubjectService eduSubjectService;

    // 无参构造
    public ExcelSubjectListener() {
    }

    //创建有参构造方法，传递subjectService到监听器中用于操作excel数据到数据库中
    public ExcelSubjectListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    //一行一行读取excel
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        //先判断excel中是否有数据
        if (excelSubjectData == null) {
            throw new OesException(ResultCodeEnum.ADD_SUBJECT_FAILED);
        }
        //按行读取,第一个值则为一级分类,第二个值则为二级分类
        //不为空
        //添加一级分类,先判断数据库中是否已经存在了该一级分类名称
        EduSubject oneSubject = this.existOneSubject(excelSubjectData.getOneSubjectName());
        if (oneSubject == null) {
            // 数据库中不存在则应该添加进去
            oneSubject = new EduSubject();
            oneSubject.setParentId("0");
            oneSubject.setTitle(excelSubjectData.getOneSubjectName());
            eduSubjectService.save(oneSubject);
        }
        //二级分类中的parent_id 为一级分类的id
        String pid = oneSubject.getId();
        //添加二级分类，先判断数据库中是否已经存在了该二级分类名称
        EduSubject twoSubject = this.existTwoSubject(excelSubjectData.getTwoSubjectName(), pid);
        if (twoSubject == null) {
            twoSubject = new EduSubject();
            twoSubject.setParentId(pid);
            twoSubject.setTitle(excelSubjectData.getTwoSubjectName());
            eduSubjectService.save(twoSubject);
        }
    }

    // 查询数据库查看一级名称是否已经存在，防止数据库中重复出现
    public EduSubject existOneSubject(String name) {
        //select * from edu_subject where title = 'name' and paraent_id = '0'
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name).eq("parent_id", "0");
        //通过条件查询该一级分类是否存在
        //返回查询到的封装对象
        return eduSubjectService.getOne(wrapper);

    }

    // 查询数据库查看二级名称是否已经存在，防止数据库中重复出现
    public EduSubject existTwoSubject(String name, String pid) {
        //select * from edu_subject where title = 'name' and paraent_id = 'pid'
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", name).eq("parent_id", pid);
        return eduSubjectService.getOne(wrapper);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头" + headMap);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("读取完成");
    }
}