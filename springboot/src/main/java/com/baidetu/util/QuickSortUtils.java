package com.baidetu.util;

import com.baidetu.common.dto.PaginationDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 快排工具类
 */
@Component
public class QuickSortUtils {

    public List<PaginationDto> sort(List<PaginationDto> list) {
        return quickSort(list, 0, list.size() - 1);
    }

    //倒序快排
    private List<PaginationDto> quickSort(List<PaginationDto> list, int l, int r) {
        if (l < r) {
            int i = l, j = r;
            Long x = list.get(i).getId();
            PaginationDto dto = list.get(i);

            //大到小排序
            while (i < j) {
                //右往左获取，取大于X
                while (i < j && list.get(j).getId() <= x) {
                    j--;
                }
                if (i < j) {
                    PaginationDto dtoJ = list.get(j);
                    list.set(i, dtoJ);
                    i++;
                }

                //从左往右获取，取小于X
                while (i < j && list.get(i).getId() > x) {
                    i++;
                }

                if (i < j) {
                    PaginationDto dtoI = list.get(i);
                    list.set(j, dtoI);
                    j--;
                }
            }
            //当指针i与j相遇，那么一次分区结束
            if (i == j)
                list.set(i, dto);

            //递归继续分区
            quickSort(list, l, i - 1);
            quickSort(list, i + 1, r);
        }
        return list;
    }
}
