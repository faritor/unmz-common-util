package net.unmz.java.util.sms;

import net.unmz.java.util.http.HttpUtils;
import net.unmz.java.util.json.JsonUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Project Name: 常用工具类集合
 * 功能描述：短信工具类 <br/>
 * 本项目依托于赛邮云通信完成短信发送
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2017-12-10 16:40
 * @since JDK 1.8
 */
public class SmsUtils {

    private static final String APPID = "*";
    private static final String SIGNATURE = "*";

    public static String SendSms(String mobile, String context, String templates) {
        System.out.println("request context :" + context);
        String host = "https://api.mysubmail.com";
        String path = "/message/xsend";
        Map<String, String> headers = new HashMap<>();
        Map<String, String> queries = new HashMap<>();
        Map<String, String> body = new HashMap<>();
        body.put("appid", APPID);
        body.put("signature", SIGNATURE);
        body.put("project", templates.trim());
        body.put("to", mobile.trim());
        body.put("vars", context);

        try {
            HttpResponse response = HttpUtils.doPost(host, path, headers, queries, body);
            String result = EntityUtils.toString(response.getEntity());
            System.out.println("sms response message: " + result);
            SmsResultVo vo = JsonUtils.toBean(result, SmsResultVo.class);
            if (vo.getStatus().equalsIgnoreCase("success"))
                return "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    public static class SmsResultVo {
        private String status;
        private String send_id;
        private int fee;
        private long sms_credits;
        private String code;
        private String msg;
        private int json_decoding_error;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSend_id() {
            return send_id;
        }

        public void setSend_id(String send_id) {
            this.send_id = send_id;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public long getSms_credits() {
            return sms_credits;
        }

        public void setSms_credits(long sms_credits) {
            this.sms_credits = sms_credits;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getJson_decoding_error() {
            return json_decoding_error;
        }

        public void setJson_decoding_error(int json_decoding_error) {
            this.json_decoding_error = json_decoding_error;
        }
    }
}
