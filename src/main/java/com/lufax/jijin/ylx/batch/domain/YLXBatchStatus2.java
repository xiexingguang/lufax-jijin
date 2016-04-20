package com.lufax.jijin.ylx.batch.domain;


public enum YLXBatchStatus2 {
    REQUEST_INIT(StatusType.ONGOING),
    REQUEST_DATA_PREPARED(StatusType.ONGOING),//准备数据成功
    REQUEST_FILE_CREATED(StatusType.ONGOING),//创建文件成功
    REQUEST_FILE_SEND(StatusType.ONGOING),//发送文件成功
    REQUEST_WITHDRAW(StatusType.END),//生成对公待付记录成功
    /**
     * 1.当没有REQUEST数据时（open_request/buy_request/purchase_request/redeem_request），直接更新batch至这个状态
     * 2.对OPEN_REQUEST batch,发送完文件就更新到这个状态
     * 3.对confirm batch,处理完数据更新至这个状态（confirm batch 的最终状态）
     */
    COMPLETE(StatusType.END),
    REQUEST_FILE_SEND_FAIL(StatusType.END),//发送文件失败

    CONFIRM_RECEIVE(StatusType.ONGOING),//confirm batch 初始状态
    CONFIRM_RECEIVE_FAIL(StatusType.END),
    RECEIVED_PREPARE(StatusType.ONGOING), //拉到confirm文件更新batch至这个状态
    RECEIVED_PREPARE_FAIL(StatusType.END),
    RECEIVED(StatusType.ONGOING),//将CONFIRM 文件同步到CONFIRM表，更新batch至这个状态
    RECEIVED_FAIL(StatusType.END),
    CONFIRMED(StatusType.ONGOING),// 赎回：request,confirm表校验完成
    CONFIRM_RECHARGE(StatusType.END) //生成待扣

    ;

    private StatusType statusType;
    YLXBatchStatus2(StatusType statusType){
        this.statusType = statusType;
    }
    private enum StatusType{
        ONGOING,
        END
        ;
    }
}
