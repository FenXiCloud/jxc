package com.flyemu.share.common;

import java.text.DecimalFormat;

/**
 * <p>****************************************************************************</p>
 * <p><b>Copyright © 2010-2019纷析云（杭州）科技有限公司All Rights Reserved<b></p>
 * <ul style="margin:15px;">
 * <li>Description : cn.gson.financial.common</li>
 * <li>Version     : 1.0</li>
 * <li>Creation    : 2019年09月12日</li>
 * <li>@author     : ____′↘夏悸</li>
 * </ul>
 * <p>****************************************************************************</p>
 */
public class ConvertMoney {
    // 大写数字
    private final static String[] STR_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    // 整数单位
    private final static String[] STR_UNIT = {"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟"};
    // 小数单位
    private final static String[] STR_UNIT2 = {"厘", "分", "角"};

    /**
     * 获取整数部分
     * ¬
     *
     * @param num 金额
     * @return 金额整数部分
     */
    public static String getInteger(String num) {
        // 判断是否包含小数点
        if (num.contains(".")) {
            num = num.substring(0, num.indexOf("."));
        }
        // 反转字符串
        num = new StringBuffer(num).reverse().toString();
        // 创建一个StringBuffer对象
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < num.length(); i++) {// 加入单位
            temp.append(STR_UNIT[i]);
            temp.append(STR_NUMBER[num.charAt(i) - 48]);
            /*
             * num.charAt(i)-48获取数值或者使用Integer.pa……
             * ASCLL表中0的位置是48，比如得到的字符3，对应ASCLL值 是51，减去48才是这里要的值
             */
        }
        // 反转字符串
        num = temp.reverse().toString();
        // 替换字符串的字符
        num = numReplace(num, "零拾", "零");
        num = numReplace(num, "零佰", "零");
        num = numReplace(num, "零仟", "零");
        num = numReplace(num, "零万", "万");
        num = numReplace(num, "零亿", "亿");
        num = numReplace(num, "零零", "零");
        num = numReplace(num, "亿万", "亿");
        // 如果字符串以零结尾将其除去
        if (num.lastIndexOf("零") == num.length() - 1) {
            num = num.substring(0, num.length() - 1);
        }
        return num;
    }

    /**
     * 获取小数部分
     *
     * @param num 金额
     * @return 金额的小数部分
     */
    public static String getDecimal(String num) {
        // 判断是否包含小数点
        if (!num.contains(".")) {
            return "";
        }
        num = num.substring(num.indexOf(".") + 1);
        // 反转字符串
        num = new StringBuffer(num).reverse().toString();
        // 创建一个StringBuffer对象
        StringBuffer temp = new StringBuffer();
        // 加入单位
        for (int i = 0; i < num.length(); i++) {
            if (num.length() == 1) {
                temp.append(STR_UNIT2[i + 2]);
                temp.append(STR_NUMBER[num.charAt(i) - 48]);
            } else if (num.length() == 2) {
                temp.append(STR_UNIT2[i + 1]);
                temp.append(STR_NUMBER[num.charAt(i) - 48]);
            } else if (num.length() >= 3) {
                temp.append(STR_UNIT2[i]);
                temp.append(STR_NUMBER[num.charAt(i) - 48]);
            }
        }
        // 反转字符串
        num = temp.reverse().toString();
        // 替换字符串的字符
        num = numReplace(num, "零角", "零");
        num = numReplace(num, "零分", "零");
        num = numReplace(num, "零厘", "零");
        num = numReplace(num, "零零", "零");

        // 如果字符串以零结尾将其除去
        if (num.lastIndexOf("零") == num.length() - 1) {
            num = num.substring(0, num.length() - 1);
        }
        return num;
    }

    /**
     * 替换字符串中内容
     *
     * @param num    字符串
     * @param oldStr 被替换内容
     * @param newStr 新内容
     * @return 替换后的字符串
     */
    public static String numReplace(String num, String oldStr, String newStr) {
        while (true) {
            // 判断字符串中是否包含指定字符
            if (!num.contains(oldStr)) {
                break;
            }
            // 替换字符串
            num = num.replaceAll(oldStr, newStr);
        }
        // 返回替换后的字符串
        return num;
    }

    /**
     * 金额转换
     *
     * @param d 金额
     * @return 转换成大写的全额
     */
    public static String convert(double d) {
        boolean negativeNumber = false;
        if (d < 0d) {
            negativeNumber = true;
            d = Math.abs(d);
        }
        if (d == 0d) {
            return "零元整";
        }
        // 实例化DecimalFormat对象
        DecimalFormat df = new DecimalFormat("#0.###");
        // 格式化double数字
        String strNum = df.format(d);
        // 判断是否包含小数点
        if (strNum.contains(".")) {
            String num = strNum.substring(0, strNum.indexOf("."));
            // 整数部分大于12不能转换
            if (num.length() > 12) {
                System.out.println("数字太大，不能完成转换！");
                return "";
            }
        }
        String point = "";// 小数点
        if (strNum.contains(".")) {
            point = "元";
        } else {
            if (strNum.length() > 12) {
                System.out.println("数字太大，不能完成转换！");
                return "";
            }
            point = "元整";
        }
        // 转换结果
        String result = getInteger(strNum) + point + getDecimal(strNum);
        if (result.startsWith("元")) { // 判断是字符串是否已"元"结尾
            // 截取字符串
            result = result.substring(1, result.length());
        }
        // 返回新的字符串
        return (negativeNumber ? "负" : "") + result;
    }
}
