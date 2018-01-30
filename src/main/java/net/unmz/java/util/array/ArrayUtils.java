package net.unmz.java.util.array;

/**
 * Project Name: 常用工具类集合
 * 功能描述： 数组工具类
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2018/1/30 18:23
 * @since JDK 1.8
 */
public class ArrayUtils {

    /**
     * 将数组转为为字符串 以下列形式显示
     *  new String[]{"a","b","c"} 转换为: 'a','b','c'
     * @param str
     * @return
     */
    public static String convertArrayToStr(Object[] str){
        StringBuilder sql = new StringBuilder();
        for(Object s : str){
            sql.append(",'").append(s).append("'");
        }
        return org.apache.commons.lang3.ArrayUtils.toString(sql).substring(1,sql.length());
    }


}
