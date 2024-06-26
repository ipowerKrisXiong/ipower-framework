package com.ipower.framework.common.core.convert.impl;

import com.ipower.framework.common.core.collection.ArrayUtil;
import com.ipower.framework.common.core.collection.Lists;
import com.ipower.framework.common.core.constant.StringPool;
import com.ipower.framework.common.core.convert.AbstractConverter;
import com.ipower.framework.common.core.convert.ConverterRegistry;

import java.io.Serial;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 数组转换器，包括原始类型数组
 * <p>
 * 参考:<a href="https://gitee.com/loolly/hutool">...</a>
 *
 * @author kris
 * @since 1.0.0
 */
public class ArrayConverter extends AbstractConverter<Object> {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Class<?> targetType;
    /**
     * 目标元素类型
     */
    private final Class<?> targetComponentType;

    /**
     * 构造
     *
     * @param targetType 目标数组类型
     */
    public ArrayConverter(Class<?> targetType) {
        if (null == targetType) {
            // 默认Object数组
            targetType = Object[].class;
        }

        if (targetType.isArray()) {
            this.targetType = targetType;
            this.targetComponentType = targetType.getComponentType();
        } else {
            //用户传入类为非数组时，按照数组元素类型对待
            this.targetComponentType = targetType;
            this.targetType = Array.newInstance(targetType, 0).getClass();
        }
    }

    @Override
    protected Object convertInternal(Object value) {
        return value.getClass().isArray() ? convertArrayToArray(value) : convertObjectToArray(value);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Class getTargetType() {
        return this.targetType;
    }

    // -------------------------------------------------------------------------------------- Private method start

    /**
     * 数组对数组转换
     *
     * @param array 被转换的数组值
     * @return 转换后的数组
     */
    private Object convertArrayToArray(Object array) {
        final Class<?> valueComponentType = array.getClass().getComponentType();

        if (valueComponentType == targetComponentType) {
            return array;
        }

        final int len = Array.getLength(array);
        final Object result = Array.newInstance(targetComponentType, len);

        final ConverterRegistry converter = ConverterRegistry.getInstance();
        for (int i = 0; i < len; i++) {
            Array.set(result, i, converter.convert(targetComponentType, Array.get(array, i)));
        }
        return result;
    }

    /**
     * 非数组对数组转换
     *
     * @param value 被转换值
     * @return 转换后的数组
     */
    private Object convertObjectToArray(Object value) {
        if (value instanceof CharSequence) {
            if (targetComponentType == char.class || targetComponentType == Character.class) {
                return convertArrayToArray(value.toString().toCharArray());
            }

            // 单纯字符串情况下按照逗号分隔后劈开
            final String[] strings = value.toString().split(StringPool.COMMA);
            return convertArrayToArray(strings);
        }

        final ConverterRegistry converter = ConverterRegistry.getInstance();
        Object result = null;
        if (value instanceof List<?> list) {
            // List转数组
            result = Array.newInstance(targetComponentType, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(result, i, converter.convert(targetComponentType, list.get(i)));
            }
        } else if (value instanceof Collection<?> collection) {
            // 集合转数组
            result = Array.newInstance(targetComponentType, collection.size());

            int i = 0;
            for (Object element : collection) {
                Array.set(result, i, converter.convert(targetComponentType, element));
                i++;
            }
        } else if (value instanceof Iterable) {
            // 可循环对象转数组，可循环对象无法获取长度，因此先转为List后转为数组
            final List<?> list = Lists.arrayList((Iterable<?>) value);
            result = Array.newInstance(targetComponentType, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(result, i, converter.convert(targetComponentType, list.get(i)));
            }
        } else if (value instanceof Iterator) {
            // 可循环对象转数组，可循环对象无法获取长度，因此先转为List后转为数组
            final List<?> list = Lists.arrayList((Iterator<?>) value);
            result = Array.newInstance(targetComponentType, list.size());
            for (int i = 0; i < list.size(); i++) {
                Array.set(result, i, converter.convert(targetComponentType, list.get(i)));
            }
        } else {
            // everything else:
            result = convertToSingleElementArray(value);
        }

        return result;
    }

    /**
     * 单元素数组
     *
     * @param value 被转换的值
     * @return 数组，只包含一个元素
     */
    private Object[] convertToSingleElementArray(Object value) {
        final Object[] singleElementArray = ArrayUtil.newArray(targetComponentType, 1);
        singleElementArray[0] = ConverterRegistry.getInstance().convert(targetComponentType, value);
        return singleElementArray;
    }
    // -------------------------------------------------------------------------------------- Private method end
}
