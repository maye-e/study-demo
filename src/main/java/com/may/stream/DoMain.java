package com.may.stream;

import cn.hutool.core.lang.Console;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class DoMain {

    public static Supplier<List<Student>> list =  () -> {
        Student s1 = new Student(1L, "肖战", 17, "浙江");
        Student s2 = new Student(2L, "王一博", 15, "湖北");
        Student s3 = new Student(3L, "杨紫", 19, "北京");
        Student s4 = new Student(4L, "李现", 19, "浙江");
        List<Student> students = new ArrayList<>();
        students.add(s1);
        students.add(s2);
        students.add(s3);
        students.add(s4);
        return students;
    };

    @Test
    public void testFilter(){// 筛选
        List<Student> collect = list.get().stream().filter(s -> Objects.equals("浙江", s.getAddress())).collect(Collectors.toList());
        collect.forEach(System.out::println);
    }

    @Test
    public void testMap(){// 转换
        List<String> collect = list.get().stream().map(s -> "住址：" + s.getAddress()).collect(Collectors.toList());
        collect.forEach(System.out::println);
    }

    @Test
    public void testDistinct(){//去重
        List<Integer> collect = list.get().stream().map(s -> s.getAge()).distinct().collect(Collectors.toList());
        collect.forEach(System.out::println);
    }

    @Test
    public void testSorted(){//排序
        // 按 id 降序，年龄升序
        list.get().stream().sorted(Comparator.comparing(Student::getAge)).sorted(Comparator.comparing(Student::getId).reversed()).forEach(System.out::println);
    }

    @Test
    public void testLimit(){// 限制返回个数（返回前几个）
        list.get().stream().limit(2).forEach(System.out::println);
    }

    @Test
    public void testSkip(){// 跳过前几个元素
        list.get().stream().skip(2).forEach(System.out::println);
    }

    @Test
    public void testReduce(){// 聚合（将集合中每个元素聚合成一条数据）
        List<String> list = Arrays.asList("你", "好");
        String may = list.stream().reduce("may", (a, b) -> a + b);
        System.out.println(may);
    }

    @Test
    public void testMin(){//最小值
        Student student = list.get().stream().min(Comparator.comparing(Student::getAge)).get();
        System.out.println(student);
    }

    @Test
    public void testMatch(){// 匹配
        boolean allMatch = list.get().stream().allMatch(s -> s.getAge() >= 15);
        if (allMatch) System.out.println("所有人年龄都满 15 的人");

        boolean anyMatch = list.get().stream().anyMatch(s -> "湖北".equals(s.getAddress()));
        if (anyMatch) System.out.println("有湖北人");

        boolean noneMatch = list.get().stream().noneMatch(s -> "马野".equals(s.getName()));
        if (noneMatch) System.out.println("没有名字为 马野 的明星");
    }

    @Test
    public void testCollect(){// 收集
        Map<String, List<Student>> collect = list.get().stream().collect(groupingBy(Student::getAddress));
        Console.log(collect);
    }
}
