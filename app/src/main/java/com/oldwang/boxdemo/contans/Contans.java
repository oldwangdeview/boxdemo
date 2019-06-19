package com.oldwang.boxdemo.contans;

/**
 * Author wangshifu
 * Dest  常量
 */

public interface Contans {

    String token = "token";
    String userInfo = "userInfo";

    /**权限请求码*/
    int  PERMISSION_REQUST_COND=2001;
    int GALLERY_REQUEST_CODE = 500;
    int CAMERA_REQUEST_CODE = 600;
    int CODE_RESULT_REQUEST = 0xa2;
    /**密码验证正则*/
    String PASSWORD_REGEX="^[0-9A-Za-z]{6,20}$";
    /**手机号正则*/
    String PHONE_REGEX="^[1][3,4,5,7,8,9][0-9]{9}$";
        String TANSLATEFORKRJ = "strideovertranslate";
     // 加载默认的状态
      int STATE_UNLOADED = 1;
     // 加载的状态
    int STATE_LOADING = 2;
     // 加载失败的状态
     int STATE_ERROR = 3;
     // 加载空的状态
     int STATE_EMPTY = 4;
     // 加载成功的状态
    int STATE_SUCCEED = 5;

    /**下架*/
     int STATE_SOLDOUT=6;
    /**传递intent数据*/
    String INTENT_DATA="intent_data";
    String INTENT_TYPE = "intent_type";
    String INTENT_TYPE_TWO = "intent_type_two";
    String INTENT_TYPE_THREE = "intent_type_three";

    int rows=20;
    /**楼盘预售*/
    int  HOUSE_PRESELL_TYPE=1;
    /**楼盘在售*/
    int HOUSE_ONSALE_TYPE=2;
    /**楼盘售罄*/
    int HOUSE_SELLOUT_TYPE=3;

    String INTENT_PHONE="intent_data_phone";
    String INTENT_CODE="intent_data_code";



    /**AESkey*/
    String AESKey = "nlkfd%M%O$xhkXLBri98fqZFdrNanDtH@sfOBkGE%YH@6g4VymgshuV7LHkeceo8vg8Da9sj7cAFYiAvwMD@NWe6c$VoeRdlCC9yI@$NgDStTI46wY1HU7ME@%CRlTMeY16%kwOVAGrREB@jD%8wnWk@pJdd$Zh$lgvc6z!5oHvMkYvTAFag#xfGGmqQvOSzPRA2R0#cJIfD0iTpuZemQFtfwLmu1uUNH0XslIrglywkJsRS%7x0GlPht#PCxT%i";
//    String AESKey="scmsw76449880330";
    int cow = 20;

    String appid = "998c15ad720f57e1ae40407c78e5b593";
    String salt = "5def0afe4824502e861352d4c13b87c6";

    String WEIXIN_APP_ID = "wx11144bc8fbf8e1e4";

    String HISTORY_ONE_USER = "history_one_user";//历史记录 装备
    String HISTORY_TWO_USER = "history_two_user";//历史记录 快讯
    String HISTORY_THREE_USER = "history_three_user";//历史记录 视频
    String HISTORY_FOUR_USER = "history_four_user";//历史记录 场馆
    String HISTORY_FIVE_USER = "history_five_user";//历史记录 视频

}
