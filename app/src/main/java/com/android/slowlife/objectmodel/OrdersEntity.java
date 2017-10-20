package com.android.slowlife.objectmodel;


import android.os.Parcel;

import com.android.slowlife.bean.OrderStatus;
import com.android.slowlife.bean.PayStatus;
import com.util.ParcelableUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单表
 */
public class OrdersEntity extends ParcelableUtil {


    /**
     * receivables : null
     * receivables_value : null
     * payType : null
     * payType_value : null
     * payDate : null
     * redPackets : no
     * redPacketsFee : 0
     * redPacketsName : null
     * redPacketsId : null
     * transactionNumber : null
     * comment : null
     * tariffId : 402881f05c900460015c90081c650005
     * userPayFee : 0
     * cost : 1
     * userActualFee : 1
     * urgentFee : 0
     * insured : no
     * goodsTotal : null
     * insuredFee : 0
     * weight : 1
     * postmanheaderImg : 1e3c4175-be0e-4079-8d13-0e4652b367d6.png
     * logisticsNumber : null
     * startLng : 106.477941
     * startLat : 29.589546
     * receiverId : null
     * endCity : null
     * endDistrict : null
     * endHouseNumber : null
     * endLng : 0
     * endLat : 0
     * shopId : null
     * shopName : null
     * arriveDate : null
     * postmanDate : null
     * shopSendDate : null
     * postmanPickupDate : 1498398420000
     * returnDate : null
     * returnReason : null
     * merchantReplay : null
     * tradeImg : 4164a5c4-c8b9-41b7-ba84-41b02e119433.jpg
     * evaluate : no
     * billoflading : null
     * id2 : null
     */

    private String receivables;
    private String receivables_value;
    private String redPackets;
    private double redPacketsFee;
    private String redPacketsName;
    private String redPacketsId;
    private String tariffId;
    private String insured;
    private String goodsTotal;
    private double insuredFee;
    private String postmanheaderImg;
    private String tradeImg;
    private String evaluate;
    private String id2;
    private String userChoiceCommpanyCode;
    /**
     * id : 402881f05c1a4b48015c1a4c194b0000
     * orderNumber : 15333333333-5131-20170518144333826
     * createDate : 2017-05-18 14:43:33
     * createUserId : 402881ed5bcdaa4f015bcdabe3c10001
     * createUserName :
     * createUserPhone : 15333333333
     * type : Intercity
     * type_value : 城际配送
     * status : UnReceivedOrder
     * status_value : 快递员未接单
     * payType : null
     * payType_value : null
     * payDate : null
     * transactionNumber : null
     * comment : null
     * userPayFee : 0
     * cost : 0
     * userActualFee : 0
     * payStatus : UnPayed
     * payStatus_value : 未付款
     * urgent : no
     * urgentFee : 0
     * weight : 0
     * postmanId : null
     * postmanName : null
     * postmanPhone : null
     * postmanCommpanyId : null
     * postmanCommpanyName : null
     * userChoiceCommpanyId : 402881ed5bd86638015bd867019b0000
     * userChoiceCommpanyName : 韵达
     * startPro : 重庆市
     * startCity : 重庆市
     * startDistrict : 江北区
     * startStreet : 石马河街道
     * startHouseNumber : 321
     * startLng : 106.484413
     * startLat : 29.587856
     * receiverId : null
     * receiverName :
     * receiverPhone : null
     * endPro : null
     * endCity : null
     * endDistrict : null
     * endStreet : null
     * endHouseNumber : null
     * endLng : 0
     * endLat : 0
     * shopId : null
     * shopName : null
     * arriveDate : null
     * postmanDate : null
     * shopSendDate : null
     * postmanSendDate : null
     * returnDate : null
     * returnReason : null
     * merchantReplay : null
     * evidenceImg : []
     * goods : [{"id":"402881f05c1a4b48015c1a4c196c0001","userOrder":null,"goodsId":null,"goodsName":"包裹","goodsPrice":0,"goodsnum":1,"shopId":null}]
     * orderRecord : [{"id":"402881f05c1a4b48015c1a4c19eb0002","userOrderNumber":"15333333333-5131-20170518144333826","createDate":"2017-05-18 14:43:33","userIdOfModifyOrder":"402881ed5bcdaa4f015bcdabe3c10001","userNameOfModifyOrder":null,"userPhoneOfModifyOrder":"15333333333","operationDetails":"用户：15333333333于2017-05-18 14:43:33创建订单成功,等待快递员接单。"}]
     * billoflading : null
     * logisticsNumber : null
     */

    public OrdersEntity() {
    }


    private String postmanPickupDate;

    private String id;
    private String orderNumber;
    private String createDate;
    private String createUserId;
    private String createUserName;
    private String createUserPhone;
    private String type;
    private String type_value;
    private String status;
    private String status_value;
    private String payType;
    private String payType_value;
    private String payDate;
    private String transactionNumber;
    private String comment;
    private double userPayFee;
    private double cost;
    private double userActualFee;
    private String payStatus;
    private String payStatus_value;
    private String urgent;
    private double urgentFee;
    private double weight;
    private String postmanId;
    private String postmanName;
    private String postmanPhone;
    private String postmanCommpanyId;
    private String postmanCommpanyName;
    private String userChoiceCommpanyId;
    private String userChoiceCommpanyName;
    private String startPro;
    private String startCity;
    private String startDistrict;
    private String startStreet;
    private String startHouseNumber;
    private double startLng;
    private double startLat;
    private String receiverId;
    private String receiverName;
    private String receiverPhone;
    private String endPro;
    private String endCity;
    private String endDistrict;
    private String endStreet;
    private String endHouseNumber;
    private double endLng;
    private double endLat;
    private String shopId;
    private String shopName;
    private String arriveDate;
    private String postmanDate;
    private String shopSendDate;
    private String postmanSendDate;
    private String returnDate;
    private String returnReason;
    private String merchantReplay;
    private String billoflading;
    private String logisticsNumber;
    private List<String> evidenceImg;
    private List<GoodsBean> goods;
    private List<OrderRecordBean> orderRecord;
    private OrderStatus orderStatus;
    private PayStatus payStatusE;

    private OrdersEntity(Parcel in) {
        super(in);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserPhone() {
        return createUserPhone;
    }

    public void setCreateUserPhone(String createUserPhone) {
        this.createUserPhone = createUserPhone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_value() {
        return type_value;
    }

    public void setType_value(String type_value) {
        this.type_value = type_value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.trim();
        this.orderStatus = OrderStatus.creat(this.status);
    }

    public String getStatus_value() {
        return status_value;
    }

    public void setStatus_value(String status_value) {
        this.status_value = status_value;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayType_value() {
        return payType_value;
    }

    public void setPayType_value(String payType_value) {
        this.payType_value = payType_value;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getUserPayFee() {
        return userPayFee;
    }

    public void setUserPayFee(double userPayFee) {
        this.userPayFee = userPayFee;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getUserActualFee() {
        return userActualFee;
    }

    public void setUserActualFee(double userActualFee) {
        this.userActualFee = userActualFee;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayStatus_value() {
        return payStatus_value;
    }

    public void setPayStatus_value(String payStatus_value) {
        this.payStatus_value = payStatus_value;
    }

    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }

    public double getUrgentFee() {
        return urgentFee;
    }

    public void setUrgentFee(double urgentFee) {
        this.urgentFee = urgentFee;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(String postmanId) {
        this.postmanId = postmanId;
    }

    public String getPostmanName() {
        return postmanName;
    }

    public void setPostmanName(String postmanName) {
        this.postmanName = postmanName;
    }

    public String getPostmanPhone() {
        return postmanPhone;
    }

    public void setPostmanPhone(String postmanPhone) {
        this.postmanPhone = postmanPhone;
    }

    public String getPostmanCommpanyId() {
        return postmanCommpanyId;
    }

    public void setPostmanCommpanyId(String postmanCommpanyId) {
        this.postmanCommpanyId = postmanCommpanyId;
    }

    public String getPostmanCommpanyName() {
        return postmanCommpanyName;
    }

    public void setPostmanCommpanyName(String postmanCommpanyName) {
        this.postmanCommpanyName = postmanCommpanyName;
    }

    public String getUserChoiceCommpanyId() {
        return userChoiceCommpanyId;
    }

    public void setUserChoiceCommpanyId(String userChoiceCommpanyId) {
        this.userChoiceCommpanyId = userChoiceCommpanyId;
    }

    public String getUserChoiceCommpanyName() {
        return userChoiceCommpanyName;
    }

    public void setUserChoiceCommpanyName(String userChoiceCommpanyName) {
        this.userChoiceCommpanyName = userChoiceCommpanyName;
    }

    public String getStartPro() {
        return startPro == null ? "" : startPro;
    }

    public void setStartPro(String startPro) {
        this.startPro = startPro;
    }

    public String getStartCity() {
        return startCity == null ? "" : startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getStartDistrict() {
        return startDistrict == null ? "" : startDistrict;
    }

    public void setStartDistrict(String startDistrict) {
        this.startDistrict = startDistrict;
    }

    public String getStartStreet() {
        return startStreet == null ? "" : startStreet;
    }

    public void setStartStreet(String startStreet) {
        this.startStreet = startStreet;
    }

    public String getStartHouseNumber() {
        return startHouseNumber == null ? "" : startHouseNumber;
    }

    public void setStartHouseNumber(String startHouseNumber) {
        this.startHouseNumber = startHouseNumber;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName == null ? "" : receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone == null ? "" : receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getEndPro() {
        return endPro == null ? "" : endPro;
    }

    public void setEndPro(String endPro) {
        this.endPro = endPro;
    }

    public String getEndCity() {
        return endCity == null ? "" : endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getEndDistrict() {
        return endDistrict == null ? "" : endDistrict;
    }

    public void setEndDistrict(String endDistrict) {
        this.endDistrict = endDistrict;
    }

    public String getEndStreet() {
        return endStreet == null ? "" : endStreet;
    }

    public void setEndStreet(String endStreet) {
        this.endStreet = endStreet;
    }

    public String getEndHouseNumber() {
        return endHouseNumber == null ? "" : endHouseNumber;
    }

    public void setEndHouseNumber(String endHouseNumber) {
        this.endHouseNumber = endHouseNumber;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public String getPostmanDate() {
        return postmanDate;
    }

    public void setPostmanDate(String postmanDate) {
        this.postmanDate = postmanDate;
    }

    public String getShopSendDate() {
        return shopSendDate;
    }

    public void setShopSendDate(String shopSendDate) {
        this.shopSendDate = shopSendDate;
    }

    public String getPostmanSendDate() {
        return postmanSendDate;
    }

    public void setPostmanSendDate(String postmanSendDate) {
        this.postmanSendDate = postmanSendDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public String getMerchantReplay() {
        return merchantReplay;
    }

    public void setMerchantReplay(String merchantReplay) {
        this.merchantReplay = merchantReplay;
    }

    public String getBilloflading() {
        return billoflading;
    }

    public void setBilloflading(String billoflading) {
        this.billoflading = billoflading;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public List<String> getEvidenceImg() {
        return evidenceImg;
    }

    public void setEvidenceImg(ArrayList<String> evidenceImg) {
        this.evidenceImg = evidenceImg;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<GoodsBean> goods) {
        this.goods = goods;
    }

    public List<OrderRecordBean> getOrderRecord() {
        return orderRecord;
    }

    public void setOrderRecord(ArrayList<OrderRecordBean> orderRecord) {
        this.orderRecord = orderRecord;
    }


    public static final Creator<OrdersEntity> CREATOR = new Creator<OrdersEntity>() {

        /**
         * 供外部类反序列化本类数组使用
         */
        @Override
        public OrdersEntity[] newArray(int size) {
            return new OrdersEntity[size];
        }

        /**
         * 从Parcel中读取数据
         */
        @Override
        public OrdersEntity createFromParcel(Parcel source) {
            return new OrdersEntity(source);
        }
    };

    public Object getReceivables() {
        return receivables;
    }

    public void setReceivables(String receivables) {
        this.receivables = receivables;
    }

    public Object getReceivables_value() {
        return receivables_value;
    }

    public void setReceivables_value(String receivables_value) {
        this.receivables_value = receivables_value;
    }


    public String getRedPackets() {
        return redPackets;
    }

    public void setRedPackets(String redPackets) {
        this.redPackets = redPackets;
    }

    public double getRedPacketsFee() {
        return redPacketsFee;
    }

    public void setRedPacketsFee(double redPacketsFee) {
        this.redPacketsFee = redPacketsFee;
    }

    public Object getRedPacketsName() {
        return redPacketsName;
    }

    public void setRedPacketsName(String redPacketsName) {
        this.redPacketsName = redPacketsName;
    }

    public Object getRedPacketsId() {
        return redPacketsId;
    }

    public void setRedPacketsId(String redPacketsId) {
        this.redPacketsId = redPacketsId;
    }


    public String getTariffId() {
        return tariffId;
    }

    public void setTariffId(String tariffId) {
        this.tariffId = tariffId;
    }


    public String getInsured() {
        return insured;
    }

    public void setInsured(String insured) {
        this.insured = insured;
    }

    public Object getGoodsTotal() {
        return goodsTotal;
    }

    public void setGoodsTotal(String goodsTotal) {
        this.goodsTotal = goodsTotal;
    }

    public double getInsuredFee() {
        return insuredFee;
    }

    public void setInsuredFee(int insuredFee) {
        this.insuredFee = insuredFee;
    }


    public String getPostmanheaderImg() {
        return postmanheaderImg;
    }

    public void setPostmanheaderImg(String postmanheaderImg) {
        this.postmanheaderImg = postmanheaderImg;
    }


    public String getTradeImg() {
        return tradeImg;
    }

    public void setTradeImg(String tradeImg) {
        this.tradeImg = tradeImg;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }


    public Object getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public static class GoodsBean extends ParcelableUtil {
        /**
         * id : 402881f05c1a4b48015c1a4c196c0001
         * userOrder : null
         * goodsId : null
         * goodsName : 包裹
         * goodsPrice : 0
         * goodsnum : 1
         * shopId : null
         */

        private String id;
        private String userOrder;
        private String goodsId;
        private String goodsName;
        private int goodsPrice;
        private int goodsnum;
        private String shopId;
        public static final Creator<GoodsBean> CREATOR = new Creator<GoodsBean>() {
            @Override
            public GoodsBean createFromParcel(Parcel source) {
                return new GoodsBean(source);
            }

            @Override
            public GoodsBean[] newArray(int size) {
                return new GoodsBean[size];
            }
        };

        public GoodsBean() {
        }

        private GoodsBean(Parcel in) {
            super(in);
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserOrder() {
            return userOrder;
        }

        public void setUserOrder(String userOrder) {
            this.userOrder = userOrder;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public int getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(int goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public int getGoodsnum() {
            return goodsnum;
        }

        public void setGoodsnum(int goodsnum) {
            this.goodsnum = goodsnum;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

    }

    public static class OrderRecordBean extends ParcelableUtil {
        /**
         * id : 402881f05c1a4b48015c1a4c19eb0002
         * userOrderNumber : 15333333333-5131-20170518144333826
         * createDate : 2017-05-18 14:43:33
         * userIdOfModifyOrder : 402881ed5bcdaa4f015bcdabe3c10001
         * userNameOfModifyOrder : null
         * userPhoneOfModifyOrder : 15333333333
         * operationDetails : 用户：15333333333于2017-05-18 14:43:33创建订单成功,等待快递员接单。
         */
        public OrderRecordBean() {
        }

        public OrderRecordBean(Parcel in) {
            super(in);
        }


        private String id;
        private String userOrderNumber;
        private String createDate;
        private String userIdOfModifyOrder;
        private String userNameOfModifyOrder;
        private String userPhoneOfModifyOrder;
        private String operationDetails;
        public static final Creator<OrderRecordBean> CREATOR = new Creator<OrderRecordBean>() {

            @Override
            public OrderRecordBean createFromParcel(Parcel source) {
                return new OrderRecordBean(source);
            }

            @Override
            public OrderRecordBean[] newArray(int size) {
                return new OrderRecordBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserOrderNumber() {
            return userOrderNumber;
        }

        public void setUserOrderNumber(String userOrderNumber) {
            this.userOrderNumber = userOrderNumber;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getUserIdOfModifyOrder() {
            return userIdOfModifyOrder;
        }

        public void setUserIdOfModifyOrder(String userIdOfModifyOrder) {
            this.userIdOfModifyOrder = userIdOfModifyOrder;
        }

        public String getUserNameOfModifyOrder() {
            return userNameOfModifyOrder;
        }

        public void setUserNameOfModifyOrder(String userNameOfModifyOrder) {
            this.userNameOfModifyOrder = userNameOfModifyOrder;
        }

        public String getUserPhoneOfModifyOrder() {
            return userPhoneOfModifyOrder;
        }

        public void setUserPhoneOfModifyOrder(String userPhoneOfModifyOrder) {
            this.userPhoneOfModifyOrder = userPhoneOfModifyOrder;
        }

        public String getOperationDetails() {
            return operationDetails;
        }

        public void setOperationDetails(String operationDetails) {
            this.operationDetails = operationDetails;
        }
    }
    public String getPostmanPickupDate() {
        return postmanPickupDate;
    }

    public void setPostmanPickupDate(String postmanPickupDate) {
        this.postmanPickupDate = postmanPickupDate;
    }


    public OrderStatus getOrderStatus() {
        if (status == null) throw new NullPointerException("status == null");
        synchronized (status) {
            if (orderStatus == null) {
                orderStatus = OrderStatus.creat(status);
            }
            return orderStatus;
        }
    }

    public PayStatus getPayStatusE() {
        if (payStatusE == null) payStatusE = PayStatus.getStatus(getPayStatus_value());
        return payStatusE;
    }

    public String getUserChoiceCommpanyCode() {
        return userChoiceCommpanyCode;
    }

    public void setUserChoiceCommpanyCode(String userChoiceCommpanyCode) {
        this.userChoiceCommpanyCode = userChoiceCommpanyCode;
    }
}
