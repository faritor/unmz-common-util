package net.unmz.java.util.express;

import java.util.List;

/**
 * Project Name: 常用工具类集合
 * 功能描述：快递查询数据模型
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2017-12-09 18:13
 * @since JDK 1.8
 */
public class ExpResultInfoVo {

    private String msg;//消息
    private ExpResultListVo result;//结果集
    private String status;//状态码

    public class ExpResultListVo {

        private String issign;//是否本人签收【不准 可能物管帮收】
        private String number;//运单编号
        private String name;//快递公司名字
        private String deliverystatus;//投递状态 1.在途中 2.正在派件 3.已签收 4.派送失败
        private String site;//快递公司官网
        private String phone;//快递公司电话
        private String type;//快递公司编码
        private List<ExpResultListResultVo> list;//结果集

        public class ExpResultListResultVo {

            private String time;//时间点
            private String status;//事件详情

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }

        public String getIssign() {
            return issign;
        }

        public void setIssign(String issign) {
            this.issign = issign;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDeliverystatus() {
            return deliverystatus;
        }

        public void setDeliverystatus(String deliverystatus) {
            this.deliverystatus = deliverystatus;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<ExpResultListResultVo> getList() {
            return list;
        }

        public void setList(List<ExpResultListResultVo> list) {
            this.list = list;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ExpResultListVo getResult() {
        return result;
    }

    public void setResult(ExpResultListVo result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
