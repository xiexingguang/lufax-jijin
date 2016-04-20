package com.lufax.jijin.base.constant;


public class AccountInterfaceConstants {

    private static final String ACCOUNT_GET_AVAILABLE_FUNDS_INTERFACE_URL = "users/%s/account/getAvailableFund";
    private static final String ACCOUNT_BATCH_TRANSFER_INTERFACE_URL = "users/%s/batchTxTransfer";
    private static final String ACCOUNT_TRANSFER_STANDARD_URL = "users/%s/transfer/standard";
    private static final String ACCOUNT_TRANSFER_REFUND_URL = "users/%s/transfer/refund";
    private static final String ACCOUNT_BATCH_TRANSFER_AND_FREEZE_INTERFACE_URL = "users/%s/batch-tx-frozen-transfer-and-freeze";

    public static String getAccountGetAvailableFundsInterfaceUrl(long plSysUrl) {
        return String.format(ACCOUNT_GET_AVAILABLE_FUNDS_INTERFACE_URL, plSysUrl);
    }

    public static String getAccountBatchTransferInterfaceUrl(long plSysUrl) {
        return String.format(ACCOUNT_BATCH_TRANSFER_INTERFACE_URL, plSysUrl);
    }

    public static String getAccountTransferStandardUrl(long plSysUrl) {
        return String.format(ACCOUNT_TRANSFER_STANDARD_URL, plSysUrl);
    }

    public static String getAccountTransferRefundUrl(long plSysUrl) {
        return String.format(ACCOUNT_TRANSFER_REFUND_URL, plSysUrl);
    }

    public static String getAccountBatchTransferAndFreezeInterfaceUrl(long plSysUrl) {
        return String.format(ACCOUNT_BATCH_TRANSFER_AND_FREEZE_INTERFACE_URL, plSysUrl);
    }
}
