package com.bjpowernode.p2p.service.loan;

public interface IncomeRecordService {
    /**
     * 生成收益计划
     */
    void generateIncomePlan();

    /**
     * 返还收益
     */
    void generateIncomeBack();
}
