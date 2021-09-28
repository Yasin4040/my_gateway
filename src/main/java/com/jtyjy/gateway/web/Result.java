package com.jtyjy.gateway.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jtyjy.gateway.exception.BizException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


@Data
@Slf4j
@Builder
@NoArgsConstructor
public class Result<T> {

    @ApiModelProperty("响应状态码")
    private Integer code;

    @ApiModelProperty("响应状态信息")
    private String message;

    @ApiModelProperty("响应数据")
    private T data;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    /*静态方法,用于快速构建结果*/

    public static <T> Result<T> ok() {
        return new Result<>(ResultCode.OK.getCode(), ResultCode.OK.getMessage(), null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultCode.OK.getCode(), ResultCode.OK.getMessage(), data);
    }

    public static <T> Result<T> failure(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> Result<T> failure(Integer code, String message) {
        return new Result<>(code, message, null);
    }


    /*扩展方法，参考Optional类，用于结果的判断、转换、异常处理等,PS: ifOk和ifPresent 有区别一个表示http成功，一个表示有返回值*/


    /**
     * 判断http调用是否成功
     *
     * @return ok
     */
    @JsonIgnore
    public boolean isOk() {
        return ResultCode.OK.getCode().equals(this.code);
    }

    /**
     * 如果Http调用成功调用该方法
     *
     * @param consumer 消费函数
     */
    @JsonIgnore
    public void ifOk(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        if (isOk()) {
            consumer.accept(data);
        }
    }


    /**
     * 判断是否有返回数据
     *
     * @return data
     */
    @JsonIgnore
    public boolean isPresent() {
        return data != null;
    }

    /**
     * http调用成功且有返回值消费
     */
    @JsonIgnore
    public void ifPresent(Consumer<? super T> consumer) {
        throwIfNotOk();
        Objects.requireNonNull(consumer);
        if (isPresent()) {
            consumer.accept(data);
        }
    }


    /**
     * http调用成功返回data，否则按照返回信息抛出异常
     *
     * @return data 数据
     * @throws BizException 业务异常
     */
    @JsonIgnore
    public T getOrThrow() {
        return getOrThrow(() -> new BizException(this.code, this.message));
    }

    /**
     * http调用成功返回data，否则按照返回信息抛出自定义异常
     *
     * @return data 数据
     * @throws BizException 业务异常
     */
    @JsonIgnore
    public T getOrThrow(Supplier<BizException> supplier) {
        if (!isOk()) {
            throw supplier.get();
        }
        return data;
    }

    /**
     * 同时满足调用成功，返回数据，否则抛出自定义异常
     *
     * @return data 数据
     * @throws BizException 业务异常
     */
    @JsonIgnore
    public T orElseThrow(Supplier<BizException> supplier) {

        throwIfNotOk();

        Objects.requireNonNull(supplier);

        if (!isPresent()) {
            throw supplier.get();
        }

        return data;
    }


    /**
     * 如果调用失败抛出错误信息
     */
    @JsonIgnore
    public void throwIfNotOk() {
        throwIfNotOk(() -> new BizException(code, message));
    }

    /**
     * 调用失败，抛出自定义错误信息
     *
     * @param supplier 异常提供者
     */
    @JsonIgnore
    public void throwIfNotOk(Supplier<BizException> supplier) {
        Objects.requireNonNull(supplier);
        if (!isOk()) {
            throw supplier.get();
        }
    }


    @JsonIgnore
    public Result<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (Objects.isNull(data)) {
            return this;
        } else {
            return predicate.test(data) ? this : new Result<>(code, message, null);
        }
    }

    @JsonIgnore
    public T orElse(T other) {
        throwIfNotOk();
        return data != null ? data : other;
    }

    @JsonIgnore
    public <U> Result<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (Objects.isNull(data)) {
            return new Result<>(code, message, null);
        } else {
            return new Result<>(code, message, mapper.apply(data));
        }
    }


}
