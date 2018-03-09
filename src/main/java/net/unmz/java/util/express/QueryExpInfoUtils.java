package net.unmz.java.util.express;

import net.unmz.java.util.http.HttpUtils;
import net.unmz.java.util.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name: 常用工具类集合
 * 功能描述：快递查询工具类
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2017-12-09 17:57
 * @since JDK 1.8
 */
public class QueryExpInfoUtils {

    /**
     * 返回物流信息 用于显示物流
     *
     * @param orderNo
     * @param type
     * @return
     */
    public static String queryExpInfoPrintStr(String orderNo, String type) {
        QueryHeard queryHeard = new QueryHeard(orderNo, type).invoke();
        String host = queryHeard.getHost();
        String path = queryHeard.getPath();
        Map<String, String> headers = queryHeard.getHeaders();
        Map<String, String> queries = queryHeard.getQueries();
        ExpResultInfoVo dto = null;

        try {
            dto = getExpResultInfoDto(host, path, headers, queries);
            StringBuilder str = new StringBuilder();
            for (ExpResultInfoVo.ExpResultListVo.ExpResultListResultVo list : dto.getResult().getList()) {
                str.append(list.getTime()).append(" ").append(list.getStatus()).append("\n");
            }
            return str.toString();
        } catch (Exception e) {

        }
        return dto != null ? dto.getMsg() : null;
    }

    /**
     * 返回物流信息 用于显示物流
     *
     * @param orderNo 物流单号
     * @param type    快递公司类型
     * @return 一个数组
     */
    public static List<String> queryExpInfoPrintList(String orderNo, String type) {
        QueryHeard queryHeard = new QueryHeard(orderNo, type).invoke();
        String host = queryHeard.getHost();
        String path = queryHeard.getPath();
        Map<String, String> headers = queryHeard.getHeaders();
        Map<String, String> queries = queryHeard.getQueries();
        ExpResultInfoVo dto = null;
        List<String> arrayList = new ArrayList<>();

        try {
            dto = getExpResultInfoDto(host, path, headers, queries);
            StringBuilder str = new StringBuilder();

            for (ExpResultInfoVo.ExpResultListVo.ExpResultListResultVo list : dto.getResult().getList()) {
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append(list.getTime()).append(" ").append(list.getStatus()).append("\n");
                str.append(strBuilder);
                arrayList.add(strBuilder.toString());
            }

            return arrayList;
        } catch (Exception e) {

        }
        arrayList.add(dto != null ? dto.getMsg() : null);
        return arrayList;
    }


    /**
     * 返回物流实体 用于其他处理
     *
     * @param orderNo 物流单号
     * @param type    物流公司
     * @return
     */
    public static ExpResultInfoVo getExpInfo(String orderNo, String type) {
        QueryHeard queryHeard = new QueryHeard(orderNo, type).invoke();
        String host = queryHeard.getHost();
        String path = queryHeard.getPath();
        Map<String, String> headers = queryHeard.getHeaders();
        Map<String, String> queries = queryHeard.getQueries();

        try {
            return getExpResultInfoDto(host, path, headers, queries);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 循环Dto 输出物流信息
     *
     * @param dto
     * @return
     */
    public static String printExpInfoStr(ExpResultInfoVo dto) {
        if (null == dto)
            return null;
        StringBuilder str = new StringBuilder();
        for (ExpResultInfoVo.ExpResultListVo.ExpResultListResultVo list : dto.getResult().getList()) {
            str.append(list.getTime()).append(" ").append(list.getStatus()).append("\n");
        }
        return str.toString();
    }

    /**
     * Http请求和接受响应处理
     *
     * @param host    接口请求地址
     * @param path    接口请求项目
     * @param headers 请求头文件
     * @param queries 请求参数
     * @return
     * @throws Exception
     */
    public static ExpResultInfoVo getExpResultInfoDto(String host, String path, Map<String, String> headers, Map<String, String> queries) throws Exception {
        String result = HttpUtils.doGet(host, path, headers, queries);
        //获取response的body
        return JsonUtils.toBean(result, ExpResultInfoVo.class);
    }

    /**
     * 请求头模型
     */
    public static class QueryHeard {
        private String orderNo;
        private String type;
        private String host;
        private String path;
        private String appCode;
        private Map<String, String> headers;
        private Map<String, String> queries;

        public QueryHeard(String orderNo, String type) {
            this.orderNo = orderNo;
            this.type = type;
        }

        public String getHost() {
            return host;
        }

        public String getPath() {
            return path;
        }

        public String getAppCode() {
            return appCode;
        }

        public void setAppCode(String appCode) {
            this.appCode = appCode;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public Map<String, String> getQueries() {
            return queries;
        }

        public QueryHeard invoke() {
            host = "http://wuliu.market.alicloudapi.com";
            path = "/kdi";
            String appCode = this.appCode != null ? this.appCode : "9576f987c8d3458eaad2c5b645c19b95";
            headers = new HashMap<>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appCode);
            queries = new HashMap<>();
            queries.put("no", orderNo);
            if (StringUtils.isNotBlank(type))
                queries.put("type", type);
            return this;
        }
    }
}