package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.FinanceAccount;

public interface FinanceAccountService {
    /**
     * 根据用户标识获取账户信息
     * @param id
     * @return
     */
    FinanceAccount queryFinanceAccountByUid(Integer id);
}
