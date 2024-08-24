package com.zhangyun.file.common.uilt;

import com.zhangyun.file.common.domain.resp.BaseResp;
import com.zhangyun.file.common.enums.RespCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
public class ObjectUtil {

    public static  <T, R> R map(T object, Function<T, R> function) {
        if (object == null || function == null) {
            return null;
        }
        return function.apply(object);
    }

    public static  <T, R> R mapOrDefault(T object, Function<T, R> function, R defaultValue) {
        R value = map(object, function);
        return value != null ? value : defaultValue;
    }

    public static boolean anyBlank(Object... params) {
        if (params == null || params.length == 0) {
            return true;
        }
        for (Object param : params) {
            if (Objects.isNull(param)) {
                return true;
            }
            if (param instanceof CharSequence && StringUtils.isBlank((CharSequence) param)) {
                return true;
            }
            if (param instanceof Collection && CollectionUtils.isEmpty((Collection<?>) param)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends BaseResp, R> T successResp(R data, Class<T> clz) {
        try {
            Field dataF = clz.getDeclaredField("data");
            dataF.setAccessible(true);
            T resp = clz.newInstance();
            dataF.set(resp, data);
            resp.setCode(RespCodeEnum.SUCCESS.getCode());
            resp.setMsg(RespCodeEnum.SUCCESS.getDesc());
            return resp;
        } catch (NoSuchFieldException | InstantiationException | IllegalAccessException e) {
            log.error("构造响应失败, e: {}", ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }
    }
}
