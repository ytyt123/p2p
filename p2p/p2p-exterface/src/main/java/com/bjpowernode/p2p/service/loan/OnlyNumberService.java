package com.bjpowernode.p2p.service.loan;

public interface OnlyNumberService {
    /**
     * 获取redis的全局唯一数字
     * @return
     */
    Long getOnlyNumber();
}
