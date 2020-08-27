package com.course.business.controller.admin;


import com.course.server.dto.ChapterDto;
import com.course.server.dto.ChapterPageDto;
import com.course.server.dto.ResponseDto;
import com.course.server.service.ChapterService;
import com.course.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/admin/chapter")
public class ChapterController {

    @Resource
    private ChapterService ChapterService;

    private static final Logger LOG = LoggerFactory.getLogger(ChapterController.class);

    public static final String BUSINESS_NAME = "大章";


    @PostMapping("/list")
    public ResponseDto list(@RequestBody ChapterPageDto chapterPageDto){

        ResponseDto responseDto = new ResponseDto();
        ValidatorUtil.require(chapterPageDto.getCourseId(), "课程ID");
        ChapterService.list(chapterPageDto);

        responseDto.setContent(chapterPageDto);
        return responseDto;
    }

    @PostMapping("/save")
    public ResponseDto save(@RequestBody ChapterDto chapterDto){

        // 保存校验  这里会 在 ControllerExceptionHandler 捕捉异常
        ValidatorUtil.require(chapterDto.getName(), "名称");
        ValidatorUtil.require(chapterDto.getCourseId(), "课程ID");
        ValidatorUtil.length(chapterDto.getCourseId(), "课程ID", 1, 8);

        ResponseDto responseDto = new ResponseDto();
        ChapterService.save(chapterDto);
        responseDto.setContent(chapterDto);
        return responseDto;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id){
        ResponseDto responseDto = new ResponseDto();
        ChapterService.delete(id);
//        responseDto.setContent(chapterDto);
        return responseDto;
    }

}
