package com.bjpowernode.p2p.service.user;

import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.ResultObject;

public interface UserService {

    /**
     * 获取平台注册总人数
     * @return
     */
    Long queryAllUserCount();

    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 用户注册
     * @param phone
     * @param loginPassword
     * @return
     */
    ResultObject register(String phone, String loginPassword);

    /**
     * 根据用户标识更新用户信息
     * @param updateUser
     * @return
     */
    int modifyUserById(User user);

    /**
     * 用户登录
     * @param phone
     * @param loginPassword
     * @return
     */
    User login(String phone, String loginPassword);
}
