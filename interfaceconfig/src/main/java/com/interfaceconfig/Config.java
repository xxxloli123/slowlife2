package com.interfaceconfig;

public interface Config {
    String LOCAL_HOST = "http://192.168.0.101:8080/";
    String HOST = "http://www.zsh7.com/";

    /**
     * 三方登录
     */
    String SIGNIN = "slowlife/appuser/otherlogin";
    // 登录
    String LOGIN = "slowlife/appuser/userlogin";
    // 注册
    String REGISTER = "slowlife/appuser/userregister";
    // 验证码
    String SMS_CODE = "slowlife/share/getsmsmobile";

    /**
     * 头像
     */
    String HEAD = HOST + "slowlife/img/userinfo/";
    /**
     * image
     */
    String IMG = HOST + "slowlife/img/order/";

    /**
     * 重设密码
     */
    String SEARPWD = "slowlife/appuser/findpassword";

    /**
     * 改绑手机号
     */
    String MOTIFYPHONE = "slowlife/appuser/updatePhone";

    /**
     * 上传文件
     */
    String UPLOADFILE = "slowlife/appuser/uploaduserimgs";

    /**
     * 收货地址列表
     */
    String ADDRLIST = "slowlife/appuser/usergetaddresses";

    /**
     * 添加收货地址
     */
    String NEWADDR = "slowlife/appuser/useraddaddresses";

    /**
     * 添加地址所需街道
     */
    String AddressStreet="slowlife/appuser/addaddressinformation";

    /**
     * 修改收货地址
     */
    String EDITADDR = "slowlife/appuser/userupdateaddresses";

    /**
     * 删除收货地址
     */
    String DELADDR = "slowlife/appuser/userdeladdresses";

    /**
     * 添加订单
     */
    String ADDORDER = "slowlife/appuserorder/addorder";

    /**
     * 创建订单所需信息
     */
    String AREAINFO = "slowlife/appuserorder/createorderinformation";

    /**
     * 创建订单
     */
    String CREATEORDER = "slowlife/appuserorder/addorder2";

    /**
     * 快递公司列表
     */
    String EXPRESSCOMPANY = "slowlife/appuserorder/getallexpresscompany";

    /**
     * 订单列表
     */
    String ORDERLIST = "slowlife/appuserorder/pagequeryorder";

    /**
     * 修改用户名
     */
    String MOTIFYUSERNAME = "slowlife/appuser/perfectuserinfo";

    /**
     * 天气
     */
    String WEATHER = "http://restapi.amap.com/v3/weather/weatherInfo";

    /**
     * 昵称
     */
    String NICKNAME = "slowlife/appuser/updatenickname";

    /**
     * 订单支付信息
     */
    String ORDERINFO = "slowlife/apppay/createtrade";

    /**
     * 红包
     */
    String REDPKG = "slowlife/appuser/getalluserredpackets";
    /**
     * 领红包
     */
    String PULLDOWNPKG = "slowlife/appuser/userredpackets";

    /**
     * orderId:订单id;userId:用户id；type：Cancel 用户取消订单 Delete 用户删除订单
     * 删除/取消订单
     */
    String DELORDER = "slowlife/appuserorder/delorder";

    /**
     * 积分兑换红包规格
     */
    String GETREDPACKETS = "slowlife/appuser/getredpackets";

    /**
     * 兑换红包
     */
    String EXCHANGEREDPACKETS = "slowlife/appuser/exchangeredpackets";


    /**
     * 分享
     */
    String SHARE = "/slowlife/app/recommend/receive.html?url=";

    /**
     * 下载链接
     */
    String DOWNLOAD = "slowlife/app/appupload.html";

    /**
     * 查询推荐
     */
    String RECOMMEND = "slowlife/appuser/recommend";
    /**
     * 消息
     */
    String MESSAGE = "slowlife/share/getmessage";
    /**
     * 订单详情
     */
    String ORDER_DETAILS = "slowlife/appuserorder/queryorderid";

    /**
     * 账户信息
     */
    String ACCOUNT = "slowlife/appuser/getuseraccount";


    /**
     * 绑定
     */
    String ORDERBIND = "slowlife/apppay/bindingaccount";

    /**
     * 提现
     */
    String CASH = "slowlife/apppay/showalipayapply";

    /**
     * 推荐兑换余额
     * slowlife/appuser/recommendnumberexchangebalance
     */
    String RECOMMENDNUMBEREXCHANGEBALANCE = "slowlife/appuser/recommendnumberexchangebalance";

    /**
     * 登录后修改密码
     */
    String MODIFYPWD = "slowlife/appuser/updatepassword";

    /**
     * 检测更新
     */
    String UPDATE = "slowlife/share/getnewestappversion";

    /**
     * bind
     */
    String BINDACCOUNT = "slowlife/appuser/bundling";

    /**
     * unbind
     */
    String UNBINDACCOUNT = "slowlife/appuser/unbundling";

    /**
     * 预算价格
     */
    String BUDGETCOST = "slowlife/appuserorder/budgetcost";

    /**
     * getrule
     */
    String GETRULE = "slowlife/appuserorder/getrule";
    /**
     * 用户添加评价
     */
    String EVALUATE = "slowlife/appuser/useraddevaluate";

    /**
     * 广告
     */
    String GETADVERTISEMENT = "slowlife/share/getadvertisement";

    /**
     *获取地区
     */
    String APPGETAREA = "slowlife/share/appgetarea";

    /**
     *描述：自动识别地址
     */
    String Discern_Address = "slowlife/api/distinguishaddress";

    static class Url {
        public static String getUrl(String url) {
            return HOST + url;
        }
    }
}
