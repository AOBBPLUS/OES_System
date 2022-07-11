package com.oes.edu.service;

import com.oes.edu.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oes.edu.entity.subject.LevelSubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 课程科目 服务类
public interface EduSubjectService extends IService<EduSubject> {
    // 添加课程分类
    void saveSubject(MultipartFile file, EduSubjectService eduSubjectService);

    //得到所有课程，一级分类中包含二级分类
    List<LevelSubject> getAllSubjectList();
}
