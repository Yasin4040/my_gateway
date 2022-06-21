package com.jtyjy.gateway.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * Created by ZiYao Lee on 2022/06/21.
 * Time: 09:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageBody<T> implements Iterable<T>, Serializable {
    @ApiModelProperty("数据总条数")
    private Long total;
    @ApiModelProperty("总分页数")
    private Integer pages;
    @ApiModelProperty("分页大小")
    private Integer pageSize;
    @ApiModelProperty("当前页号")
    private Integer pageNum;
    @ApiModelProperty("记录列表")
    private List<T> records;

    /**
     * 兼容 pageHelper的pageInfo
     */
    public PageBody(PageInfo<T> page) {
        this(page.getTotal(), page.getPages(), page.getPageSize(), page.getPageNum(), page.getList());
    }

    /**
     * 兼容 pageHelper的pageInfo
     */
    public PageBody(List<T> list){
        this(new PageInfo<>(list));
    }

    /**
     * 兼容 mybatisPlus的page
     */
    public PageBody(Page<T> page) {
        this(page.getTotal(), (int)page.getPages(), (int)page.getSize(), (int)page.getCurrent(), page.getRecords());
    }


    public <U> PageBody<U> map(Function<? super T, ? extends U> mapper) {
        PageBody<U> pageBody = new PageBody<>();
        List<U> collect = Objects.isNull(records) ? Collections.emptyList() : records.stream().map(mapper).collect(Collectors.toList());
        pageBody.setPages(pages);
        pageBody.setTotal(total);
        pageBody.setPageNum(pageNum);
        pageBody.setPageSize(pageSize);
        pageBody.setRecords(collect);

        return pageBody;
    }

    @Override
    public Iterator<T> iterator() {
        Objects.requireNonNull(records);
        return records.iterator();
    }
}
