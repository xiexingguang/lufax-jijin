package com.lufax.jijin.base.constant;

public enum TransactionType {
    FROZEN_FUND("资金冻结", FundType.FREEZE_RELATED),
    UNFROZEN_FUND("资金解冻", FundType.UNFREEZE_RELATED),

    INSURANCE_FEE_REPAYMENT("担保费", FundType.EXPENSE),
    INSURANCE_FEE_COLLECTION("担保费", FundType.INCOME),

    PRINCIPAL_REPAYMENT("偿还本金", FundType.EXPENSE),
    INTEREST_REPAYMENT("偿还利息", FundType.EXPENSE),
    RE_OVERDUE_PENALTY("偿还逾期罚息", FundType.EXPENSE),

    COLLECTION("收回本息", FundType.INCOME),

    PRINCIPAL_COLLECTION("收回本金", FundType.INCOME),
    INTEREST_COLLECTION("收回利息", FundType.INCOME),
    CO_OVERDUE_PENALTY("收回逾期罚息", FundType.INCOME),

    PENAL_COLLECTION_LOANER("提前还款违约金", FundType.INCOME),
    PENAL_COLLECTION_XINBAO("提前还款违约金", FundType.INCOME),
    PENAL_REPAYMENT_LOANER("提前还款违约金", FundType.EXPENSE),
    PENAL_REPAYMENT_XINBAO("提前还款违约金", FundType.EXPENSE),

    MANAGEMENT_FEE_REPAYMENT("账户管理费", FundType.EXPENSE),
    MANAGEMENT_FEE_COLLECTION("账户管理费", FundType.INCOME),

    SUBROGATION_REPAYMENT("追偿支出", FundType.EXPENSE),
    SUBROGATION_COLLECTION("追偿收入", FundType.INCOME),

    PRINCIPAL_COLLECTION_COMPENSATE("收回代偿本金", FundType.INCOME),
    INTEREST_COLLECTION_COMPENSATE("收回代偿利息", FundType.INCOME),
    OVERDUE_PENALTY_COLLECTION_COMPENSATE("收回代偿逾期罚息", FundType.INCOME),
    PRINCIPAL_REPAYMENT_COMPENSATE("偿还代偿本金", FundType.EXPENSE),
    INTEREST_REPAYMENT_COMPENSATE("偿还代偿利息", FundType.EXPENSE),
    OVERDUE_PENALTY_REPAYMENT_COMPENSATE("偿还代偿逾期罚息", FundType.EXPENSE),

    EXPENSE_BUYBACK("回购转出", FundType.EXPENSE),
    INCOME_BUYBACK("回购转入", FundType.INCOME),

    SHOT_OVER_IN("收款尾差", FundType.INCOME),
    SHOT_OVER_OUT("扣款尾差", FundType.EXPENSE),

    EXPENSE_BILL_REPAYMENT("票据还款", FundType.EXPENSE),
    INCOME_BILL_REPAYMENT("票据还款", FundType.INCOME),

    EXPENSE_BILL_OVERDUE_PENALTY("票据逾期违约金", FundType.EXPENSE),
    INCOME_BILL_OVERDUE_PENALTY("票据逾期违约金", FundType.INCOME),

    EXPENSE_BILL_TRANSACTION_FEE("票据服务费", FundType.EXPENSE),
    INCOME_BILL_TRANSACTION_FEE("票据服务费", FundType.INCOME),
    EXPENSE_WITHDRAW_INSURANCE("退保成功", FundType.EXPENSE),
    INCOME_WITHDRAW_INSURANCE("退保成功", FundType.INCOME);

    private String value;

    private FundType fundType;

    private TransactionType(String value, FundType fundType) {
        this.value = value;
        this.fundType = fundType;
    }

    public enum FundType {
        INCOME, EXPENSE,
        //FROZEN_RELATED, //原来的冻结和解冻相关交易类型均关联该FROZEN_RELATED资金类型
        FREEZE_RELATED, UNFREEZE_RELATED
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
    
}
