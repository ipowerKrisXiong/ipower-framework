package com.ipower.framework.common.core.text;

import com.ipower.framework.common.core.collection.ArrayUtil;
import com.ipower.framework.common.core.constant.CharPool;
import com.ipower.framework.common.core.lang.StringUtil;

/**
 * 字符串格式化工具
 * <p>
 * 参考:<a href="https://gitee.com/loolly/hutool">...</a>
 *
 * @author kris
 * @since 1.0.0
 */
public class StringFormatter {

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is {} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
     *
     * @param pattern 字符串模板
     * @param args    参数列表
     * @return 结果
     */
    public static String format(final String pattern, final Object... args) {
        if (StringUtil.isEmpty(pattern) || ArrayUtil.isEmpty(args)) {
            return pattern;
        }
        final int strPatternLength = pattern.length();

        // 初始化定义好的长度以获得更好的性能
        StringBuilder sb = new StringBuilder(strPatternLength + 50);

        // 记录已经处理到的位置
        int handledPosition = 0;
        int delimitIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            delimitIndex = pattern.indexOf(StringUtil.EMPTY_JSON, handledPosition);
            // 剩余部分无占位符
            if (delimitIndex == -1) {
                // 不带占位符的模板直接返回
                if (handledPosition == 0) {
                    return pattern;
                }
                // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                sb.append(pattern, handledPosition, strPatternLength);
                return sb.toString();
            }

            // 转义符
            if (delimitIndex > 0 && pattern.charAt(delimitIndex - 1) == CharPool.BACKSLASH) {
                // 双转义符
                if (delimitIndex > 1 && pattern.charAt(delimitIndex - 2) == CharPool.BACKSLASH) {
                    // 转义符之前还有一个转义符，占位符依旧有效
                    sb.append(pattern, handledPosition, delimitIndex - 1);
                    sb.append(StringUtil.toUtf8String(args[argIndex]));
                    handledPosition = delimitIndex + 2;
                } else {
                    // 占位符被转义
                    argIndex--;
                    sb.append(pattern, handledPosition, delimitIndex - 1);
                    sb.append(CharPool.DELIM_START);
                    handledPosition = delimitIndex + 1;
                }
            } else {// 正常占位符
                sb.append(pattern, handledPosition, delimitIndex);
                sb.append(StringUtil.toUtf8String(args[argIndex]));
                handledPosition = delimitIndex + 2;
            }
        }

        // append the characters following the last {} pair.
        // 加入最后一个占位符后所有的字符
        sb.append(pattern, handledPosition, pattern.length());

        return sb.toString();
    }
}
