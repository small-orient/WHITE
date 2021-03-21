package com.baidetu.controller;


import com.baidetu.common.Result;
import com.baidetu.common.dto.PaginationDto;
import com.baidetu.service.PaginationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 定义归档页controller
 */
@RestController
@RequestMapping("/archive")
public class ArchiveController {

    @Resource
    private PaginationService paginationService;

    @GetMapping("/archives")
    public Result archives() {
        return Result.success(paginationService.selectByList(
                new QueryWrapper<PaginationDto>()
                        .eq("deleted", 0)
                        .eq("published", 0)
                        .orderByDesc("create_time")));
    }
}
