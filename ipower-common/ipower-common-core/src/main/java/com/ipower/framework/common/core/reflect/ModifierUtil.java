package com.ipower.framework.common.core.reflect;

import com.ipower.framework.common.core.collection.ArrayUtil;
import com.ipower.framework.common.core.exception.UtilException;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 修饰符工具类
 * <p>
 * 参考:<a href="https://gitee.com/loolly/hutool">hutool</a>
 *
 * @author kris
 * @since 1.0.0
 */
public class ModifierUtil {

    /**
     * 修饰符枚举
     *
     * @author looly
     */
    @Getter
    public static enum ModifierType {
        /**
         * public修饰符，所有类都能访问
         */
        PUBLIC(Modifier.PUBLIC),
        /**
         * private修饰符，只能被自己访问和修改
         */
        PRIVATE(Modifier.PRIVATE),
        /**
         * protected修饰符，自身、子类及同一个包中类可以访问
         */
        PROTECTED(Modifier.PROTECTED),
        /**
         * static修饰符，（静态修饰符）指定变量被所有对象共享，即所有实例都可以使用该变量。变量属于这个类
         */
        STATIC(Modifier.STATIC),
        /**
         * final修饰符，最终修饰符，指定此变量的值不能变，使用在方法上表示不能被重载
         */
        FINAL(Modifier.FINAL),
        /**
         * synchronized，同步修饰符，在多个线程中，该修饰符用于在运行前，对他所属的方法加锁，以防止其他线程的访问，运行结束后解锁。
         */
        SYNCHRONIZED(Modifier.SYNCHRONIZED),
        /**
         * （易失修饰符）指定该变量可以同时被几个线程控制和修改
         */
        VOLATILE(Modifier.VOLATILE),
        /**
         * （过度修饰符）指定该变量是系统保留，暂无特别作用的临时性变量，序列化时忽略
         */
        TRANSIENT(Modifier.TRANSIENT),
        /**
         * native，本地修饰符。指定此方法的方法体是用其他语言在程序外部编写的。
         */
        NATIVE(Modifier.NATIVE),

        /**
         * abstract，将一个类声明为抽象类，没有实现的方法，需要子类提供方法实现。
         */
        ABSTRACT(Modifier.ABSTRACT),
        /**
         * strictfp，一旦使用了关键字strictfp来声明某个类、接口或者方法时，那么在这个关键字所声明的范围内所有浮点运算都是精确的，符合IEEE-754规范的。
         */
        STRICT(Modifier.STRICT);

        /**
         * 修饰符枚举对应的int修饰符值
         * -- GETTER --
         * 获取修饰符枚举对应的int修饰符值，值见
         */
        private final int value;

        /**
         * 构造
         *
         * @param modifier 修饰符int表示，见{@link Modifier}
         */
        private ModifierType(int modifier) {
            this.value = modifier;
        }

    }

    /**
     * 是否同时存在一个或多个修饰符（可能有多个修饰符，如果有指定的修饰符则返回true）
     *
     * @param clazz         类
     * @param modifierTypes 修饰符枚举
     * @return 是否有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
     */
    public static boolean hasModifier(Class<?> clazz, ModifierType... modifierTypes) {
        if (clazz == null || ArrayUtil.isEmpty(modifierTypes)) {
            return false;
        }
        return (clazz.getModifiers() & modifiersToInt(modifierTypes)) != 0;
    }

    /**
     * 是否同时存在一个或多个修饰符（可能有多个修饰符，如果有指定的修饰符则返回true）
     *
     * @param constructor   构造方法
     * @param modifierTypes 修饰符枚举
     * @return 是否有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
     */
    public static boolean hasModifier(Constructor<?> constructor, ModifierType... modifierTypes) {
        if (constructor == null || ArrayUtil.isEmpty(modifierTypes)) {
            return false;
        }
        return (constructor.getModifiers() & modifiersToInt(modifierTypes)) != 0;
    }

    /**
     * 是否同时存在一个或多个修饰符（可能有多个修饰符，如果有指定的修饰符则返回true）
     *
     * @param method        方法
     * @param modifierTypes 修饰符枚举
     * @return 是否有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
     */
    public static boolean hasModifier(Method method, ModifierType... modifierTypes) {
        if (method == null || ArrayUtil.isEmpty(modifierTypes)) {
            return false;
        }
        return (method.getModifiers() & modifiersToInt(modifierTypes)) != 0;
    }

    /**
     * 是否同时存在一个或多个修饰符（可能有多个修饰符，如果有指定的修饰符则返回true）
     *
     * @param field         字段
     * @param modifierTypes 修饰符枚举
     * @return 是否有指定修饰符，如果有返回true，否则false，如果提供参数为null返回false
     */
    public static boolean hasModifier(Field field, ModifierType... modifierTypes) {
        if (field == null || ArrayUtil.isEmpty(modifierTypes)) {
            return false;
        }
        return (field.getModifiers() & modifiersToInt(modifierTypes)) != 0;
    }

    /**
     * 是否是Public字段
     *
     * @param field 字段
     * @return 是否是Public
     */
    public static boolean isPublic(Field field) {
        return hasModifier(field, ModifierType.PUBLIC);
    }

    /**
     * 是否是Public方法
     *
     * @param method 方法
     * @return 是否是Public
     */
    public static boolean isPublic(Method method) {
        return hasModifier(method, ModifierType.PUBLIC);
    }

    /**
     * 是否是Public类
     *
     * @param clazz 类
     * @return 是否是Public
     */
    public static boolean isPublic(Class<?> clazz) {
        return hasModifier(clazz, ModifierType.PUBLIC);
    }

    /**
     * 是否是Public构造
     *
     * @param constructor 构造
     * @return 是否是Public
     */
    public static boolean isPublic(Constructor<?> constructor) {
        return hasModifier(constructor, ModifierType.PUBLIC);
    }

    /**
     * 是否是static字段
     *
     * @param field 字段
     * @return 是否是static
     */
    public static boolean isStatic(Field field) {
        return hasModifier(field, ModifierType.STATIC);
    }

    /**
     * 是否是static方法
     *
     * @param method 方法
     * @return 是否是static
     */
    public static boolean isStatic(Method method) {
        return hasModifier(method, ModifierType.STATIC);
    }

    /**
     * 是否是static类
     *
     * @param clazz 类
     * @return 是否是static
     */
    public static boolean isStatic(Class<?> clazz) {
        return hasModifier(clazz, ModifierType.STATIC);
    }

    /**
     * 设置final的field字段可以被修改
     * 只要不会被编译器内联优化的 final 属性就可以通过反射有效的进行修改 --  修改后代码中可使用到新的值;
     * <p>以下属性，编译器会内联优化，无法通过反射修改：</p>
     * <ul>
     *     <li> 基本类型 byte, char, short, int, long, float, double, boolean</li>
     *     <li> Literal String 类型(直接双引号字符串)</li>
     * </ul>
     * <p>以下属性，可以通过反射修改：</p>
     * <ul>
     *     <li>基本类型的包装类 Byte、Character、Short、Long、Float、Double、Boolean</li>
     *     <li>字符串，通过 new String("")实例化</li>
     *     <li>自定义java类</li>
     * </ul>
     * <pre class="code">
     * {@code
     *      //示例，移除final修饰符
     *      class JdbcDialects {private static final List<Number> dialects = new ArrayList<>();}
     *      Field field = ReflectUtil.getField(JdbcDialects.class, fieldName);
     * 		ReflectUtil.removeFinalModify(field);
     * 		ReflectUtil.setFieldValue(JdbcDialects.class, fieldName, dialects);
     *    }
     * </pre>
     *
     * <p>JDK9+此方法抛出NoSuchFieldException异常，原因是除非开放，否则模块外无法访问属性</p>
     *
     * @param field 被修改的field，不可以为空
     * @throws UtilException IllegalAccessException等异常包装
     */
    public static void removeFinalModify(final Field field) {
        if (!hasModifier(field, ModifierType.FINAL)) {
            return;
        }

        //将字段的访问权限设为true：即去除private修饰符的影响
        ReflectUtil.setAccessible(field);

        //去除final修饰符的影响，将字段设为可修改的
        final Field modifiersField;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
        } catch (final NoSuchFieldException e) {
            throw new UtilException(e, "Field [modifiers] not exist!");
        }

        try {
            //Field 的 modifiers 是私有的
            modifiersField.setAccessible(true);
            //& ：位与运算符，按位与；  运算规则：两个数都转为二进制，然后从高位开始比较，如果两个数都为1则为1，否则为0。
            //~ ：位非运算符，按位取反；运算规则：转成二进制，如果位为0，结果是1，如果位为1，结果是0.
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch (final IllegalAccessException e) {
            //内部，工具类，基本不抛出异常
            throw new UtilException(e, "IllegalAccess for [{}.{}]", field.getDeclaringClass(), field.getName());
        }
    }

    //-------------------------------------------------------------------------------------------------------- Private method start

    /**
     * 多个修饰符做“与”操作，表示同时存在多个修饰符
     *
     * @param modifierTypes 修饰符列表，元素不能为空
     * @return “与”之后的修饰符
     */
    private static int modifiersToInt(ModifierType... modifierTypes) {
        int modifier = modifierTypes[0].getValue();
        for (int i = 1; i < modifierTypes.length; i++) {
            modifier &= modifierTypes[i].getValue();
        }
        return modifier;
    }
    //-------------------------------------------------------------------------------------------------------- Private method end
}
