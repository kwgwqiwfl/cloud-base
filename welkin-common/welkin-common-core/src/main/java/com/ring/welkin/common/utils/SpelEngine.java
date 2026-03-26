package com.ring.welkin.common.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.script.ScriptException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class SpelEngine {
    public static final String FLAG = "#";
    private final ExpressionParser parser = new SpelExpressionParser();
    private final StandardEvaluationContext context = new StandardEvaluationContext();

    private SpelEngine() {
        registerFunctions();
    }

    /**
     * 注册默认的函数
     */
    private void registerFunctions() {
        // 注册静态方法 registerFunction 或 setVariable 都可以
        Class<EL> ElClass = EL.class;
        registerFunction("date", ElClass);
        registerFunction("format", ElClass, Date.class, String.class);
        registerFunction("env", ElClass, String.class);
        registerFunction("curdate", ElClass, String.class);
        registerFunction("yesterday", ElClass, String.class);
        registerFunction("today", ElClass);
        registerFunction("todayformat", ElClass, String.class);
        registerFunction("uuid", ElClass);
        registerFunction("yyyyMMdd", ElClass);
        registerFunction("yyyyMMddHHmm", ElClass);
        registerFunction("lastNWeekdays", ElClass, String.class, String.class, String.class, int.class);
        registerFunction("last7Weekdays", ElClass, String.class, String.class, String.class);
        registerFunction("last5Weekdays", ElClass, String.class, String.class, String.class);
        registerFunction("lastNWeekdayHours", ElClass, String.class, String.class, String.class, int.class);
        registerFunction("last7WeekdayHours", ElClass, String.class, String.class, String.class);
        registerFunction("last5WeekdayHours", ElClass, String.class, String.class, String.class);
        registerFunction("lastMonthOfYesterdayHours", ElClass, String.class, String.class, String.class);
        registerFunction("lastWeekFirstday", ElClass, String.class);
        registerFunction("beforeOneHour", ElClass, String.class);
        registerFunction("yesterdayHours", ElClass, String.class, String.class, String.class);
        registerFunction("year", ElClass, int.class);
        registerFunction("month", ElClass, int.class);
        registerFunction("today_offset", ElClass, String.class, int.class, String.class);
        registerFunction("today_before_offset", ElClass, String.class, String.class, String.class, int.class);
    }

    public static SpelEngine of() {
        return new SpelEngine();
    }

    public static SpelEngine of(Map<String, ?> contextData) {
        SpelEngine spelEngine = new SpelEngine();
        spelEngine.setVariables(contextData);
        return spelEngine;
    }

    /**
     * 向context中批量加入键值
     *
     * @param contextData 批量加入的键值
     */
    public void setVariables(Map<String, ?> contextData) {
        log.info("setVariables: contextData => {}", contextData);
        if (contextData != null && !contextData.isEmpty()) {
            this.context.setVariables(Maps.newHashMap(contextData));
        }
    }

    /**
     * 向context中加入键值
     *
     * @param key   键
     * @param value 值
     */
    public void setVariable(String key, Object value) {
        log.info("setVariable: key => {}, value => {}", key, value);
        this.context.setVariable(key, value);
    }

    /**
     * 注册函数
     *
     * @param funName 函数名称
     * @param method  注册成函数的方法
     */
    public void registerFunction(String funName, Method method) {
        log.info("final registerFunction: funName => {}, method => {}", funName, method);
        this.context.registerFunction(funName, method);
    }

    /**
     * 注册函数
     *
     * @param <E>            注册函数的方法来源类
     * @param funName        函数名称
     * @param funClass       来源类
     * @param methodName     方法名称
     * @param parameterTypes 参数类型数组
     */
    public <E> void registerFunction(String funName, Class<E> funClass, String methodName, Class<?>... parameterTypes) {
        try {
            log.info("registerFunction: funName => {}, funClass => {}, methodName => {}, parameterTypes => {}", funName,
                    funClass.getName(), methodName, parameterTypes);
            this.registerFunction(funName, funClass.getDeclaredMethod(methodName, parameterTypes));
        } catch (NoSuchMethodException | SecurityException e) {
            log.warn(e.getMessage(), e);
        }
    }

    /**
     * 注册函数
     *
     * @param <E>            注册函数的方法来源类
     * @param funName        函数名称
     * @param funClass       来源类
     * @param parameterTypes 参数类型数组
     */
    public <E> void registerFunction(String funName, Class<E> funClass, Class<?>... parameterTypes) {
        this.registerFunction(funName, funClass, funName, parameterTypes);
    }

    /**
     * 解析表达式结果
     *
     * @param <R>           返回结果类型
     * @param expr          表达式
     * @param resultType    返回值类型
     * @param parserContext 用于影响此表达式解析例程的上下文（可选）
     * @return 返回表达式解析的结果值
     */
    public <R> R evalSpel(String expr, Class<R> resultType, ParserContext parserContext) {
        log.info("evalSpel: expr => {}, resultType => {}, parserContext => {}", expr, resultType.getName(),
                parserContext);
        R result = null;
        if (parserContext == null) {
            result = this.parser.parseExpression(expr).getValue(this.context, resultType);
        } else {
            result = this.parser.parseExpression(expr, parserContext).getValue(this.context, resultType);
        }
        log.info("evalSpel: result => {} ", result);
        return result;
    }

    /**
     * 解析表达式结果
     *
     * @param <R>        返回结果类型
     * @param expr       表达式
     * @param resultType 返回值类型
     * @return 返回表达式解析的结果值
     */
    public <R> R evalSpel(String expr, Class<R> resultType) {
        return evalSpel(expr, resultType, null);
    }

    /**
     * 解析表达式结果
     *
     * @param expr          表达式
     * @param parserContext 用于影响此表达式解析例程的上下文（可选）
     * @return 返回表达式解析的结果值
     */
    public Object evalSpel(String expr, ParserContext parserContext) {
        return evalSpel(expr, Object.class, parserContext);
    }

    /**
     * 解析表达式结果
     *
     * @param expr 表达式
     * @return 返回表达式解析的结果值
     */
    public Object evalSpel(String expr) {
        return evalSpel(expr, Object.class);
    }

    /**
     * 执行静态函数
     *
     * @param <T>        函数封装类型
     * @param <R>        返回结果类型
     * @param expr       表达式
     * @param resultType 返回结果类型
     * @return 解析结果
     */
    public <T, R> R evalFunction(String expr, Class<T> clazzType, Class<R> resultType) {
        log.info("evalFunction: expr => {}, clazzType => {}, resultType => {}", expr, clazzType.getName(),
                resultType.getName());
        String join = StringUtils.join("T(", clazzType.getName(), ").", expr);
        return this.parser.parseExpression(join).getValue(resultType);
    }

    /**
     * 解析执行表达式结果为字符串结果，默认是表达式格式为:
     *
     * <pre>
     * 	解析上下文：
     * 		contextData => {name=张三, age=23}
     *
     * 	解析函数：
     * 		spel.eval("#{uuid()}") => c57cbc10-7802-4c02-a3e8-d828c17a9df8
     * 		spel.eval("#{today()}") => 20230614
     * 		spel.eval("#{todayformat('yyyyMMdd')}") => 20230614
     * 		spel.eval("#{yyyyMMdd()}") => 20230614
     * 		spel.eval("#{yesterday('yyyy-MM-dd')}") => 2023-06-13
     * 		spel.eval("#{lastWeekFirstday('yyyy-MM-dd')}") => 2023-06-05
     *
     * 	解析变量：
     * 		spel.eval("#{name}") => 张三
     * 		spel.eval("#{age}") => 23
     *
     * </pre>
     *
     * @param expr 表达式
     * @return 解析结果
     * @throws ScriptException
     */
    public String eval(String expr) throws ScriptException {
        log.info("eval: expr => {}", expr);
        return eval(FLAG, expr);
    }

    /**
     * 解析执行表达式结果为字符串结果，默认是表达式格式为:
     *
     * <pre>
     *  解析上下文：
     * 		contextData => {name=张三, age=23}
     *
     *  解析函数：
     * 		spel.eval("#", "#{uuid()}") => 0be1eb5c-3ee0-4e45-9f11-140c2eec3477
     * 		spel.eval("$", "${uuid()}") => 5f283a67-6782-450b-bdc2-a2e22075ab7d
     * 		spel.eval("@", "@{uuid()}") => da34a6c7-34b9-4335-b292-9b4196786102
     * 		spel.eval("#", "#{today()}") => 20230614
     * 		spel.eval("$", "${today()}") => 20230614
     * 		spel.eval("@", "@{today()}") => 20230614
     *
     * 	解析变量：
     * 		spel.eval("@", "@{name}") => 张三
     * 		spel.eval("#", "#{name}") => 张三
     * 		spel.eval("$", "${name}") => 张三
     * 		spel.eval("@", "@{age}") => 23
     * 		spel.eval("#", "#{age}") => 23
     * 		spel.eval("$", "${age}") => 23
     *
     * 正则匹配：
     *  pattern => (.*)(#\{)([^:]+):?(.*)(\})(.*)
     * 	group0 => before expr #{val:dffd} after expr
     * 	group1 => before expr
     * 	group2 => #{
     * 	group3 => val
     * 	group4 => dffd
     * 	group5 => }
     * 	group6 =>  after expr
     *
     * </pre>
     *
     * @param expr 表达式
     * @return 解析结果
     * @throws ScriptException
     */
    public synchronized String eval(String flag, String expr) throws ScriptException {
        log.info("eval:flag =>{}, expr => {} ", flag, expr);
        // Pattern p = Pattern.compile("\\" + flag + "\\{([^}]+)\\}");
        Pattern p = Pattern.compile("(.*)(" + flag + "\\{)([^:]+):?(.*)(\\})(.*)");
        // Pattern p = Pattern.compile("(?<=" + flag + "\\{)([^:]+):(.*)(?=\\})");
        Matcher m = p.matcher(expr);
        StringBuffer s = new StringBuffer();
        boolean matched = false;
        while (m.find()) {
            matched = true;
            Object result;
            String group1 = m.group(1);
            String group2 = m.group(2);
            String group3 = m.group(3);
            String group4 = m.group(4);
            String group5 = m.group(5);
            String group6 = m.group(6);
            log.info("group0 => {}, group1 => {}, group2 => {}, group3 => {}, group4 => {}, group5 => {}, group6 => {}",
                    expr, group1, group2, group3, group4, group5, group6);
            try {
                if (!group3.startsWith(FLAG)) {
                    group3 = FLAG + group3;
                    log.info("add group flag :group3 =>{}", group3);
                }
                result = evalSpel(group3, String.class);
                log.info("get eval result: group3 => {}, result => {}", group3, result);
                if (result == null && group4 != null) {
                    result = group4;
                    log.info("give default value: group4 => {}, result => {}", group4, result);
                }
            } catch (Throwable e) {
                throw new ScriptException("eval failed:" + expr, e.getMessage() + "", 1);
            }
            // m.appendReplacement(s, result == null ? "null" : result.toString());
            s.append(group1).append(result).append(group6);
        }
        // if (matched) {
        // m.appendTail(s);
        // }
        String finalResult = matched ? s.toString() : expr;
        log.info("eval result: flag => {}, expr => {}, result => {}", flag, expr, finalResult);
        return finalResult;
    }

    public static void main(String[] args) throws ScriptException {
        ImmutableMap<String, Object> contextData = ImmutableMap.of("name", "张三", "age", "23");
        SpelEngine spel = SpelEngine.of(contextData);

        System.out.println(spel.evalSpel("#{#uuid()}", new TemplateParserContext("#{", "}")));
        System.out.println(spel.evalSpel("#uuid()"));
        System.out.println(spel.evalSpel("#today()"));
        System.out.println(spel.evalSpel("#todayformat('yyyyMMdd')"));
        System.out.println(spel.evalSpel("#yyyyMMdd()"));
        System.out.println(spel.evalSpel("#yesterday('yyyy-MM-dd')"));
        System.out.println(spel.evalSpel("#lastWeekFirstday('yyyy-MM-dd')"));

        System.out.println(spel.evalSpel("#{#name}", new TemplateParserContext("#{", "}")));
        System.out.println(spel.evalSpel("#name", new TemplateParserContext("#{", "}")));
        System.out.println(spel.evalSpel("#name", String.class));
        System.out.println(spel.evalSpel("#age", Integer.class));
        System.out.println(spel.evalSpel("#{#age}", Integer.class, new TemplateParserContext("#{", "}")));

        System.out.println("解析上下文：");
        System.out.println("contextData => " + contextData);

        System.out.println("解析函数：");
        System.out.println("spel.eval(\"#\", \"#{uuid()}\") => " + spel.eval("#", "#{uuid()}"));
        System.out.println("spel.eval(\"$\", \"${uuid()}\") => " + spel.eval("$", "${uuid()}"));
        System.out.println("spel.eval(\"@\", \"@{uuid()}\") => " + spel.eval("@", "@{uuid()}"));
        System.out.println("spel.eval(\"#\", \"#{today()}\") => " + spel.eval("#", "#{today()}"));
        System.out.println("spel.eval(\"$\", \"${today()}\") => " + spel.eval("$", "${today()}"));
        System.out.println("spel.eval(\"@\", \"@{today()}\") => " + spel.eval("@", "@{today()}"));

        System.out.println("解析变量：");
        System.out.println("spel.eval(\"@\", \"@{name}\") => " + spel.eval("@", "@{name}"));
        System.out.println("spel.eval(\"#\", \"#{name}\") => " + spel.eval("#", "#{name}"));
        System.out.println("spel.eval(\"$\", \"${name}\") => " + spel.eval("$", "${name}"));
        System.out.println("spel.eval(\"@\", \"@{age}\") => " + spel.eval("@", "@{age}"));
        System.out.println("spel.eval(\"#\", \"#{age}\") => " + spel.eval("#", "#{age}"));
        System.out.println("spel.eval(\"$\", \"${age}\") => " + spel.eval("$", "${age}"));

        System.out.println("解析上下文：");
        System.out.println("contextData => " + contextData);

        System.out.println("解析函数：");
        System.out.println("spel.eval(\"#{uuid()}\") => " + spel.eval("#{uuid()}"));
        System.out.println("spel.eval(\"#{today()}\") =>" + spel.eval("#{today()}"));
        System.out.println("spel.eval(\"#{todayformat('yyyyMMdd')}\") =>" + spel.eval("#{todayformat('yyyyMMdd')}"));
        System.out.println("spel.eval(\"#{yyyyMMdd()}\") =>" + spel.eval("#{yyyyMMdd()}"));
        System.out.println("spel.eval(\"#{yesterday('yyyy-MM-dd')}\") =>" + spel.eval("#{yesterday('yyyy-MM-dd')}"));
        System.out.println(
                "spel.eval(\"#{lastWeekFirstday('yyyy-MM-dd')}\") =>" + spel.eval("#{lastWeekFirstday('yyyy-MM-dd')}"));

        System.out.println("解析变量：");
        System.out.println("spel.eval(\"#{name}\") => " + spel.eval("#{name}"));
        System.out.println("spel.eval(\"#{age}\") => " + spel.eval("#{age}"));
        System.out.println("spel.eval(\"#{age:12}\") => " + spel.eval("#{age:12}"));
        System.out.println("spel.eval(\"#{age1:12}\") => " + spel.eval("#{age1:12}"));
        System.out.println("spel.eval(\"select * from sink_hive6281 where stime='#{yesterday('yyyy-MM-dd')}'\") => "
                + spel.eval("select * from sink_hive6281 where stime='#{yesterday('yyyy-MM-dd')}'"));
    }
}
