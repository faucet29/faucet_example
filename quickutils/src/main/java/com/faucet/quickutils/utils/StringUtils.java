package com.faucet.quickutils.utils;

import java.text.DecimalFormat;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/16
 *     desc  : 字符串相关工具类
 * </pre>
 */
public class StringUtils {

    private StringUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param string 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param string 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String string) {
        return (string == null || string.trim().length() == 0);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param string 待转字符串
     * @return string为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(String string) {
        return string == null ? "" : string;
    }

    /**
     * 返回字符串长度
     *
     * @param string 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(CharSequence string) {
        return string == null ? 0 : string.length();
    }

    /**
     * 首字母大写
     *
     * @param string 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String string) {
        if (isEmpty(string) || !Character.isLowerCase(string.charAt(0))) {
            return string;
        }
        return String.valueOf((char) (string.charAt(0) - 32)) + string.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param string 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(String string) {
        if (isEmpty(string) || !Character.isUpperCase(string.charAt(0))) {
            return string;
        }
        return String.valueOf((char) (string.charAt(0) + 32)) + string.substring(1);
    }

    /**
     * 转化为半角字符
     *
     * @param string 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(String string) {
        if (isEmpty(string)) {
            return string;
        }
        char[] chars = string.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param string 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String string) {
        if (isEmpty(string)) {
            return string;
        }
        char[] chars = string.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }
    /**
     * 给传入的字符串前补足'0'，以使字符串长度为len。例如：输入字符串:"23",4 返回:"0023"。
     * @param str String
     * @param len int
     * @return String
     */
    public String getZeroStr(String str, int len) {
        int strlen = str.length();
        for(int i = 0; i < len - strlen; i++) {
            str = "0" + str;
        }
        return str;
    }
    /**
     * 用于转义单斜杠特殊字符
     * @param str String
     * @return String
     */
    public static final String appendStr(String str) {
        char c = ' ';
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if(c == '\\') {
                sb.append("\\\\");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    /**
     * 返回字符串的len个字符.取前后，去掉中间 例如:输入"abcdefg",3 返回 "ab ... g".
     * @param value String
     * @param len int
     * @return String
     */
    public static String getLmtStrx(String value, int len) {
        if(value == null || value.length() <= len)
            return value;
        value = value.substring(0,len/2) + ".." + value.substring(value.length()-len/2);
        return value;
    }
    /**
     * 返回字符串的前len个字符.例如:输入"abcdefg",3 返回 "abc".
     * @param value String
     * @param len int
     * @return String
     */
    public static String getLmtStr(String value, int len) {
        if(value == null || value.length() <= len)
            return value;
        return value.substring(0, len);
    }

    /**
     * 返回字符串的后len个字符，例如：输入："abcdefg",3,返回 "efg"
     * @param value
     * @param len
     * @return
     */
    public static String getLmtStrEndWith(String value,int len){
        if(value==null || value.length()<=len){
            return value;
        }
        return value.substring(value.length()-2,value.length());
    }
    /**
     * 返回字符串的前len个字符.例如:输入"abcdefg",3 返回 "abc...".
     * @param value String
     * @param len int
     * @return String
     */
    public static String getLmtString(String value, int len) {
        if(value == null || value.length() <= len)
            return value;
        return value.substring(0, len) + "...";
    }
    /**
     * 过滤HTML标签
     * @param value String
     * @return String
     */
    public static String filterScriptHtml(String value) {
        if(value == null)
            return "";
        else {
            value = value.replaceAll("<", "&lt;");
            value = value.replaceAll(">", "&gt;");
        }
        return value;
    }
    /**
     * 将textarea输入的文本转化成前台html显示的格式，主要将回车（/r/n）替换成<br>," "替换成&nbsp;
     * @param text String
     * @return String
     */
    public static String textConvertToHtmlText(String text){
        if(text != null){
            return text.replaceAll("\r\n", "<br>").replaceAll(" ", "&nbsp;");
        }else{
            return null;
        }
    }
    /**
     * @param value double
     * @param format String  "0.00"
     * @return String
     */
    public static String getFormatDouble(double value, String format) {
        DecimalFormat d = new DecimalFormat(format);
        return d.format(value);
    }
    /**
     * 0.34567 -> 0.35
     * @param value double
     * @return String
     */
    public static String getFormatDouble(double value) {
        return getFormatDouble(value, "0.00");
    }
    /**
     * 判断str是否在src String[]中
     * @param src 源String[]
     * @param str 要判断的String
     * @param isIngoreCase 是否忽略大小写
     * @return boolean
     */
    public static boolean isContain(String[] src, String str,boolean isIngoreCase) {
        if(null == src) {
            return false;
        }
        for(int i = 0; i < src.length; i++) {
            if(isIngoreCase) {
                if (str.equalsIgnoreCase(src[i])) {
                    return true;
                }
            }else{
                if(str.equals(src[i])){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 字符串转ASCII
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if(i != chars.length - 1)
            {
                sbu.append((int)chars[i]).append(",");
            }
            else {
                sbu.append((int)chars[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * ASCII转字符串
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }
}