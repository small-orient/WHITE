package com.baidetu;

import com.baidetu.common.dto.PaginationDto;
import com.baidetu.service.PaginationService;
import com.baidetu.util.QuickSortUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class BaidetuApplicationTests02 {

    /**
     * 定时任务测试
     */
    @Test
    void test01() throws InterruptedException {
       /* TaskUtils.task1();
        TaskUtils.task2();*/
    }

    @Resource
    private QuickSortUtils quickSortUtils;

    /**
     * 快速排序测试
     */
    @Test
    void test02() {

        List<PaginationDto> list = new ArrayList<>();
        PaginationDto paginationDto1 = new PaginationDto();
        PaginationDto paginationDto2 = new PaginationDto();
        PaginationDto paginationDto3 = new PaginationDto();
        PaginationDto paginationDto4 = new PaginationDto();
        PaginationDto paginationDto5 = new PaginationDto();
        PaginationDto paginationDto6 = new PaginationDto();
        PaginationDto paginationDto7 = new PaginationDto();


        paginationDto1.setId(5L);
        paginationDto2.setId(8L);
        paginationDto3.setId(1L);
        paginationDto4.setId(3L);
        paginationDto5.setId(7L);
        paginationDto6.setId(4L);
        paginationDto7.setId(2L);

        list.add(paginationDto1);
        list.add(paginationDto2);
        list.add(paginationDto3);
        list.add(paginationDto4);
        list.add(paginationDto5);
        list.add(paginationDto6);
        list.add(paginationDto7);

        quickSortUtils.sort(list);
        System.out.println(list.get(0).getId() + ","
                + list.get(1).getId() + ","
                + list.get(2).getId() + ","
                + list.get(3).getId() + ","
                + list.get(4).getId() + ","
                + list.get(5).getId() + ","
                + list.get(6).getId());
    }

    @Resource
    private PaginationService paginationService;

    @Test
    void test03() {
        System.out.println(paginationService.addLikeForRedis(26L).toString());
    }


}
