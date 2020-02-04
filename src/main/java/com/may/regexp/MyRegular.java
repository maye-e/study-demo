package com.may.regexp;

import cn.hutool.core.lang.Console;
import com.sun.corba.se.spi.ior.iiop.IIOPFactories;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则练习
 */
public class MyRegular {

    // 计数（实现序号）
    private static int cnt = 0;

    public static void main(String[] args) {
        /**
         * 规则匹配器
         * @param: 待匹配的字符串，匹配规则
         */
        BiConsumer<String, String> reg = (str, rule) -> {
            cnt += 1;
            Console.log("{}.\t{}", cnt, str.matches(rule));
        };
//基础篇
        //1.匹配 11 次数字
        reg.accept("12345678901", "\\d{11}");

        //2.精准匹配，相当于 equals()
        reg.accept("a&b", "a\\&b");

        //3.匹配 n~m 次用 {n,m}，至少匹配 n 次用 {n,},最多匹配 m 次用 {0,m}
        reg.accept("12", "\\d{2,}");

        //4. \d（匹配一个数字）、\w（匹配一个字符，包含数字,字母,下划线）、\s（匹配一个空格，包含空格,tab(\t),回车(\n)打出来的空格）
        reg.accept("12  abc", "\\d{0,2}\\s{2}\\w{3}");

        //5. 规则字母大写表示相反的意思(\D 匹配非数字，\W 匹配非字母，\S 匹配非空格)
        reg.accept("s #", "\\D\\s\\W");

        //6.对于某些位置的字符没有要求，仅需要占位，用 .
        reg.accept("a_b", "a.b");

        //7.匹配任意次用 * ，等价于 {0,}
        reg.accept("", "\\d*");

        //8.至少匹配一次用 + ，等价于 {1,}
        reg.accept("12f", "\\d+\\w");

        //9.最多匹配一次用 ? ，等价用 {0,1}
        reg.accept("2ff", "\\d?\\w+");

        //10.匹配制定范围内的字符用 []，如 [1-9a-zC-D]，表示匹配 1-9 或 a-z 或 C-D。
        // 若匹配0-1,8-9，可以写成[0189] 或 [0-18-9]，因为正则一次只会匹配一个字符，所以不会被匹配成 0-18
        reg.accept("639abvC8", "[1-9]{0,3}[a-z]{0,3}[C-D]?[0-18-9]");

        //11. 或运算符 |
        reg.accept("abc", "abc|ABC|xyz");

        //12.范围排除，比如某些位置不能是 123 ，可以写成 [^123] 或 [^1-3]，^ 表示非
        reg.accept("abc45_123", "\\w{0,3}[^1-3]{0,2}\\w[1-3]{0,3}");

        /*13.匹配单词边界 \b。注意边界是一个位置，并不匹配任何字符。空白、换行等看不见的字符不是边界。
            搞不清字符串中边界如何划分，参考这个例子：
            String str = "(中文问号？123???英文)问号?我是华丽[的制表符\t]我是华丽{的空格符 我是华丽}的换行符\n";
            String[] split = str.split("\\b");\\按边界分割
            Arrays.stream(split).forEach(System.out::println);
         */
        reg.accept("abc123-789:xyz", "abc123\\b-\\b789\\b:\\bxyz");

        //14. 匹配非单词边界 \B，与 \b 正好相反
        reg.accept("123", "1\\B2\\B3");

        //15. 匹配字符串的开始位置，匹配的是一个边界 ^
        reg.accept("abc123", "^\\w+\\d+");

        //16. 匹配字符串的结束位置，匹配的是一个边界 $
        reg.accept("abc123", "\\w+\\d+$");

        //17. 分组。
        /*
            也可以这样写：reg.accept("ababab","(\\w+)\\1*");// \1 表示匹配第一个括号中的字符
         */
        reg.accept("ababab", "(\\w+)*");// 组内的字符重复多次

//进阶篇
        //1. 探囊取物
        Supplier<Matcher> regGet = () -> {
            /**
             * 取出姓名和年龄，将规则中要取出的部分用 () 括起来。
             * 查看源码，如果用 String.matches()，则每次调用都会创建一个 Pattern 对象，占内存、效率低，
             * 如果有多个字符串用相同的规则匹配，优化的方法是创建一个 Pattern 对象，复用 Pattern 去匹配字符串。
             */
            String str = "name: may   age: 25";
            /*
             捕获到的分组保存在内存中
             */
            // 数字编号捕获组
            // Pattern pattern = Pattern.compile("name: (\\w*)\\s*age: (\\d{1,3})");
            // 命名方式捕获组，语法：(?<name>exp)
            Pattern pattern = Pattern.compile("name: (?<name>\\w*)\\s*age: (?<age>\\d{1,3})");
            Matcher matcher = pattern.matcher(str);
            return matcher;
        };
        Matcher matcher = regGet.get();
        if (matcher.matches()) {
            int groupCount = matcher.groupCount();

            // 数字编号捕获组
//            String name = matcher.group(1);
//            String age = matcher.group(2);

            // 命名方式捕获组
            String name = matcher.group("name");
            String age = matcher.group("age");
            Console.log("捕获---分组个数为：{}，姓名：{}，年龄：{}", groupCount, name, age);
            //group(0) 存的是整个待匹配的字符串，也可以用 group() 取
            System.out.println(matcher.group(0));// matcher.group()
        }

        // 非捕获组，语法：(?:exp)
        Matcher noget = Pattern.compile("(?:0\\d{2})-(\\d{8})").matcher("020-85653333");
        if (noget.find()) {
            Console.log("非捕获---分组个数：{}，第 0 个分组：{}，第 1 个分组：{}", noget.groupCount(), noget.group(0), noget.group(1));
        }

        // split() 函数也可以传入正则，在不确定具体分隔符的情况下这样做
        System.out.println(Arrays.toString("java,php,python,golang".split("[,;\\s]+")));
        System.out.println(Arrays.toString("java;php;python;golang".split("[,;\\s]+")));
        System.out.println(Arrays.toString("java php python golang".split("[,;\\s]+")));

        //2.移花接木
        // replaceAll() 也可以用正则，通过 $1 $2 来反向引用字符串，只需要将被引用的地方用()括起来
        System.out.println("java,php,python,golang".replaceAll("([,;\\s]+)", "--$1--"));
        System.out.println("java;php;python;golang".replaceAll("([,;\\s]+)", "--$1--"));
        System.out.println("java php python golang".replaceAll("([,;\\s]+)", "--$1--"));

        // 反向引用。数字编号分组反向引用：\k 或 \number；命名方式捕获组反向引用：\k 或 \name



        //正则默认是贪婪匹配，即在能匹配目标字符串的前提下尽可能向后匹配更多。而在非贪婪匹配的正则后加 ? ，即表示非贪婪匹配
        //待匹配字符，统计末尾 e 的个数
        String str = "mayee";
        Matcher greedyMatcher = Pattern.compile("(\\w+)(e*)").matcher(str);
        Matcher noGreedyMatcher = Pattern.compile("(\\w+?)(e*)").matcher(str);
        if (greedyMatcher.matches()) {
            System.out.println("group(1): " + greedyMatcher.group(1));
            System.out.println("group(2): " + greedyMatcher.group(2));
        }
        if (noGreedyMatcher.matches()) {
            System.out.println("group(1): " + noGreedyMatcher.group(1));
            System.out.println("group(2): " + noGreedyMatcher.group(2));
        }

//习题
        //将以下字符串替换成连续的句子
        String tStr = "肚子。。好饿......, ....早知道.....当...初....。 。 。 不学 ... 。。 ， java  .. 。 . 了！";
        System.out.println(tStr.replaceAll("[.。,，\\s]+", ""));


// 高阶知识：零宽断言
    /*
        正向先行断言（正前瞻）
        语法：(?=pattern)
        作用：匹配 pattern 表达式前面的内容，不返回表达式本身。
     */
        Matcher matcher1 = Pattern.compile(".+(?=</p>)").matcher("<p>这是一个段落</p>");
        while (matcher1.find()) {
            System.out.println("正前瞻：\t" + matcher1.group());
        }
     /*
        正向后行断言（正后顾）
        语法：(?<=pattern)
        作用：匹配 pattern 表达式后面的内容，不返回表达式本身。
     */
        Matcher matcher2 = Pattern.compile("(?<=<p>).+").matcher("<p>这是一个段落</p>");
        while (matcher2.find()) {
            System.out.println("正后顾：\t" + matcher2.group());
        }

     /*
        负向先行断言（负前瞻）
        语法：(?!pattern)
        作用：匹配非 pattern 表达式前面的内容，不返回表达式本身。
     */
        Matcher matcher3 = Pattern.compile("祖国(?!的花朵)").matcher("我爱祖国，我是祖国的花朵");
        while (matcher3.find()) {// 找到不是“的花朵”前面的“祖国”
            System.out.println("负前瞻：\t" + matcher3.group());
        }
     /*
        负向后行断言（负后顾）
        语法：(?<!pattern)
        作用：匹配非 pattern 表达式后面的内容，不返回表达式本身。
     */
        Matcher matcher4 = Pattern.compile("(?<!祖国的)花朵").matcher("我是花朵，祖国的花朵是我");
        while (matcher4.find()) {// 找到不是“祖国的”后面的“花朵”
            System.out.println("负后顾：\t" + matcher4.group());
        }
    }
}
