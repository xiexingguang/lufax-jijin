package com.lufax.jijin.daixiao.constant;

import com.google.gson.Gson;

/**
 * Created by NiuZhanJun on 7/24/15.
 */
public enum JijinExAddFileResultEnum {
    SUCCESS("新增成功", "0"),
    ERROR_PARAMETER("参数错误", "251001"),
    ERROR_FILE_EXISTED("文件已存在", "251002"),
    ERROR_EXCEPTION("系统错误", "251099");


    private String res_msg;
    private String res_code;

    JijinExAddFileResultEnum(String resultMsg, String typeCode) {
        setResultMsg(resultMsg);
        setResultCode(typeCode);
    }

    public String getResultMsg() {
        return res_msg;
    }

    public String toJson() {
        ResponseGson responseGson = new ResponseGson(this.getResultCode(), this.getResultMsg());
        return new Gson().toJson(responseGson);
    }

    public void setResultMsg(String resultMsg) {
        this.res_msg = resultMsg;
    }

    public String getResultCode() {
        return res_code;
    }

    public void setResultCode(String resultCode) {
        this.res_code = resultCode;
    }

    class ResponseGson {
        private String res_msg;
        private String res_code;

        ResponseGson(String code, String msg) {
            res_code = code;
            res_msg = msg;
        }
    }
}
