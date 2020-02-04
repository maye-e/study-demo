package com.may.regexp;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;

import java.util.function.Predicate;

/**
 * 身份证验证规则
 */
public class IdcardRegular {
    public static void main(String[] args) {

        //验证身份证是否有效
        Predicate<String> idcardOfValid = (idcard) -> {
            if (!idcard.matches("\\d{17}[0-9|x|X]")) {
                return false;
            }
            // 乘数系数
            int[] multi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            //身份证最后一位的字符映射
            Dict lastNumMapping = Dict.create()
                    .set("0", "1")
                    .set("1", "0")
                    .set("2", "x")
                    .set("3", "9")
                    .set("4", "8")
                    .set("5", "7")
                    .set("6", "6")
                    .set("7", "5")
                    .set("8", "4")
                    .set("9", "3")
                    .set("10", "2");

            String[] idcardArray = idcard.split("\\B");
            int len = idcardArray.length;
            int lastNumIndex = 0;// 身份证最后一位的索引，对应 lastNumMapping 的 key
            for (int i = 0; i < len; i++) {
                if (i < len - 1) {
                    lastNumIndex += Integer.valueOf(idcardArray[i]) * multi[i];// 将最后一位之前的数字乘以对的系数
                } else {
                    lastNumIndex %= 11;//最后除以 11 取余
                }
            }
            return idcardArray[len - 1].equalsIgnoreCase(lastNumMapping.getStr(String.valueOf(lastNumIndex)));
        };

        System.out.println("身份证是否有效：\t" + idcardOfValid.test("110101199003077993"));

    }
}
