package com.oes.edu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.edu.entity.EduSubject;
import com.oes.edu.entity.excel.ExcelSubjectData;
import com.oes.edu.entity.subject.LevelSubject;
import com.oes.edu.listener.ExcelSubjectListener;
import com.oes.edu.mapper.EduSubjectMapper;
import com.oes.edu.service.EduSubjectService;
import com.oes.servicebase.exceptionhandler.OesException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 课程科目 服务实现类
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    // 添加课程分类
    @Override
    public void saveSubject(MultipartFile file, EduSubjectService eduSubjectService) {
        try {
            //1.获取文件输入流
            InputStream inputStream = file.getInputStream();
            //2.读取excel
            EasyExcel.read(inputStream, ExcelSubjectData.class, new ExcelSubjectListener(eduSubjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
            throw new OesException(ResultCodeEnum.ADD_SUBJECT_FAILED);
        }
    }

    // 获取所有的科目
    @Override
    public List<LevelSubject> getAllSubjectList() {
        //1.先获得一级分类  select * from edu_subject where parent_id = '0'
        QueryWrapper<EduSubject> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("parent_id", "0");
        queryWrapper1.orderByAsc("sort", "id");
        List<EduSubject> oneSubjects = baseMapper.selectList(queryWrapper1);

        //2.再获得二级分类
        QueryWrapper<EduSubject> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.ne("parent_id", "0");
        queryWrapper2.orderByAsc("sort", "id");
        List<EduSubject> twoSubjects = baseMapper.selectList(queryWrapper2);

        //利用Map进行优化
        Map<String, LevelSubject> oneSubjectMap = new HashMap<>();

        //3.封装一级分类
        for (EduSubject oneSubject : oneSubjects) {
            //得到每个eduSubject（一级分类）
            //转化为一级分类对应的bean
            LevelSubject levelOneSubject = new LevelSubject();
            levelOneSubject.initialize(false);
            //简写
            BeanUtils.copyProperties(oneSubject, levelOneSubject);
            //简写后应该将一级学科加入到Map中
            oneSubjectMap.put(levelOneSubject.getId(), levelOneSubject);
        }

        //4.封装二级分类,并且在二级分类时利用其parentId直接将其添加进其父节点下
        for (EduSubject twoSubject : twoSubjects) {
            //得到每个eduSubject（二级分类）
            //转化为二级分类对应的bean
            LevelSubject levelTwoSubject = new LevelSubject();
            //简写
            BeanUtils.copyProperties(twoSubject, levelTwoSubject);
            //二级分类应该将其加入到一级分类的子科目下
            LevelSubject parent = oneSubjectMap.get(twoSubject.getParentId());
            parent.addChild(levelTwoSubject);
        }

        //5.最后返回数据
        return new ArrayList<>(oneSubjectMap.values());

        // 如果使用递归则只需要一行代码:return recursiveSearch("0");
    }

    // 获取所有科目,递归写法,递归写法支持多级分类
    protected List<LevelSubject> recursiveSearch(String parentId) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        wrapper.orderByAsc("sort", "id");
        List<EduSubject> children = baseMapper.selectList(wrapper);
        List<LevelSubject> childrenList = new ArrayList<>();
        if (children != null) {
            for (EduSubject child : children) {
                LevelSubject subject = new LevelSubject();
                subject.setId(child.getId());
                subject.setTitle(child.getTitle());
                subject.setChildren(recursiveSearch(child.getId()));
                childrenList.add(subject);
            }
        }
        return childrenList;
    }
}