package com.bjpowernode.p2p.web;

import com.alibaba.fastjson.JSONObject;
import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.common.util.HttpClientUtils;
import com.bjpowernode.p2p.config.Config;
import com.bjpowernode.p2p.model.user.FinanceAccount;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.model.vo.ResultObject;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.FinanceAccountService;
import com.bjpowernode.p2p.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
//@RestController //等同于@controller ，类中每个方法响应的都是JSON对象@ResponseBody
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private Config config;
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private BidInfoService bidInfoService;

    /**
     * 验证手机号是否存在
     * http://localhost:8080/p2p/loan/checkPhone
     *
     * @param request
     * @param phone   必填
     * @return
     */
    @RequestMapping(value = "/loan/checkPhone")
    @ResponseBody
    public Object checkPhone(HttpServletRequest request,
                             @RequestParam(value = "phone", required = true) String phone) {
        Map<String, Object> retMap = new HashMap<>();

        //查询手机号是否重复 ->返回Boolean|user|int
        //根据手机号查询用户信息
        User user = userService.queryUserByPhone(phone);

        //判断用户是否存在
        if (user != null) {
            retMap.put(Constants.ERROR_MESSAGE, "该手机号已被注册，请更换手机号码");
            return retMap;
        }
        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);

        return retMap;
    }

    // @RequestMapping(value = "/loan/checkCaptcha",method = RequestMethod.POST)//等同于@PostMapping
    @PostMapping(value = "/loan/checkCaptcha")
    @ResponseBody
    public Map<String, Object> checkCaptcha(HttpServletRequest request,
                                            @RequestParam(value = "captcha", required = true) String captcha) {
        Map<String, Object> retMap = new HashMap<>();
        //获取session中的图形验证码
        String sessionCaptcha = (String) request.getSession().getAttribute(Constants.CAPTCHA);
        //验证图形验证码是否一致
        if (!StringUtils.equalsIgnoreCase(sessionCaptcha, captcha)) {
            retMap.put(Constants.ERROR_MESSAGE, "请输入正确的图形验证码");
            return retMap;
        }
        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);
        return retMap;
    }

    @RequestMapping(value = "/loan/register")
    @ResponseBody
    public Object register(HttpServletRequest request,
                           @RequestParam(value = "phone", required = true) String phone,
                           @RequestParam(value = "loginPassword", required = true) String loginPassword,
                           @RequestParam(value = "replayLoginPassword", required = true) String replayLoginPassword) {
        Map<String, Object> retMap = new HashMap<>();
        //验证参数
        if (!Pattern.matches("^1[1-9]\\d{9}$", phone)) {
            retMap.put(Constants.ERROR_MESSAGE, "请输入正确的手机号码");
            return retMap;
        }
        if (!StringUtils.equals(loginPassword, replayLoginPassword)) {
            retMap.put(Constants.ERROR_MESSAGE, "两次密码输入不一致");
            return retMap;
        }
        //用户的注册(手机号，登录密码)(1.新增用户信息 2.开立账户) ->Boolean|int|结果对象ResultObject
        ResultObject resultObject = userService.register(phone, loginPassword);

        //判断是否注册成功
        if (!StringUtils.equals(Constants.SUCCESS, resultObject.getErrorCode())) {
            retMap.put(Constants.ERROR_MESSAGE, "注册失败，请稍后重试");
            return retMap;
        }
        //将用户的 信息存放到session中
        request.getSession().setAttribute(Constants.SESSION_USER, userService.queryUserByPhone(phone));
        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);
        return retMap;
    }

    @RequestMapping(value = "/loan/verifyRealName")
    @ResponseBody
    public Object verifyRealName(HttpServletRequest request,
                                 @RequestParam(value = "realName", required = true) String realName,
                                 @RequestParam(value = "idCard", required = true) String idCard,
                                 @RequestParam(value = "replayIdCard", required = true) String replayIdCard) {
        Map<String, Object> retMap = new HashMap<>();
        //验证参数
        if (!Pattern.matches("^[\\u4e00-\\u9fa5]{0,}$", realName)) {
            retMap.put(Constants.ERROR_MESSAGE, "真实姓名只支持中文");
            return retMap;
        }
        if (!Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)", idCard)) {
            retMap.put(Constants.ERROR_MESSAGE, "请输入正确的身份证号码");
            return retMap;
        }
        if (!StringUtils.equals(replayIdCard, idCard)) {
            retMap.put(Constants.ERROR_MESSAGE, "两次输入身份证号码不一致");
            return retMap;
        }
        //准备实名认证的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //实名认证appkey
        paramMap.put("appkey", config.getRealNameAppkey());
        //真实姓名
        paramMap.put("realName", realName);
        //身份证号码
        paramMap.put("cardNo", idCard);
        //发送请求验证用户真实信息->返回json格式的字符串
        String jsonString = HttpClientUtils.doPost(config.getRealNameUrl(), paramMap);

        //解析json格式字符串,fastjson
        //将json格式的字符串转换为JSON对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //获取指定key所对应的value,获取code通信标识
        String code = jsonObject.getString("code");

        //判断通信是否成功
        if (StringUtils.equals("10000", code)) {
            //通信成功
            //获取isok是否匹配标识
            Boolean isok = jsonObject.getJSONObject("result").getJSONObject("result").getBoolean("isok");

            //判断真实姓名与身份证号码是否一致
            if (isok) {
                //从session中获取用户信息
                User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
                //更新用户信息
                User updateUser = new User();
                updateUser.setId(sessionUser.getId());
                updateUser.setName(realName);
                updateUser.setIdCard(idCard);
                int modifyUserCount = userService.modifyUserById(updateUser);
                if (modifyUserCount > 0) {
                    System.out.println("hi");
                    //更新session中用户的信息
                    request.getSession().setAttribute(Constants.SESSION_USER,
                            userService.queryUserByPhone(sessionUser.getPhone()));
                    retMap.put(Constants.ERROR_MESSAGE, Constants.OK);
                } else {
                    retMap.put(Constants.ERROR_MESSAGE, "系统繁忙，请稍后再试...");
                    return retMap;
                }
            } else {
                retMap.put(Constants.ERROR_MESSAGE, "真实姓名与身份证不匹配");
                return retMap;
            }
        } else {
            //通信失败
            retMap.put(Constants.ERROR_MESSAGE, "获取通信失败，请稍后重试...");
            return retMap;
        }

        return retMap;
    }
    @RequestMapping(value = "/loan/myFinanceAccount")
    @ResponseBody
    public FinanceAccount myFinanceAccount(HttpServletRequest request){
        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        return  financeAccountService.queryFinanceAccountByUid(sessionUser.getId());
    }
    @RequestMapping(value = "/loan/login")
    @ResponseBody
    public Object login(HttpServletRequest request,
                        @RequestParam(value ="phone",required = true)String phone,
                        @RequestParam(value = "loginPassword",required = true)String loginPassword){
        Map<String, Object> retMap = new HashMap<>();

        //验证手机号码格式
        if (!Pattern.matches("^1[1-9]\\d{9}$", phone)) {
            retMap.put(Constants.ERROR_MESSAGE, "请输入正确的手机号码");
            return retMap;
        }
        //用户登录【1.根据手机号和密码查询用户 2.更新最近登录时间】(手机号，密码)->返回User|Map
       User user=userService.login(phone,loginPassword);
        //判断用户是否存在
        if (user == null) {
            retMap.put(Constants.ERROR_MESSAGE,"用户名或密码输入有误，请重新输入");
            return  retMap;
        }
      //将用户的信息存放到session中
        request.getSession().setAttribute(Constants.SESSION_USER,user);
        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
        return retMap;
    }

    @RequestMapping(value = "/loan/logout")
    public String logout(HttpServletRequest request){
        //让session失效或者清除指定session中key的值
        request.getSession().invalidate();

       // request.getSession().removeAttribute(Constants.SESSION_USER);

        return "redirect:/index";
    }
    @RequestMapping(value = "/loan/loadStat")
    public  @ResponseBody Object loadStat(HttpServletRequest request){

        Map<String, Object> retMap = new HashMap<>();
        //历史平均年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        //累计投资金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        //平台注册总人数
        Long allUserCount = userService.queryAllUserCount();
        retMap.put(Constants.HISTORY_AVERAGE_RATE,historyAverageRate);
        retMap.put(Constants.ALL_USER_COUNT,allUserCount);
        retMap.put(Constants.ALL_BID_MONEY,allBidMoney);
        return retMap;
    }
    @RequestMapping(value = "/loan/myCenter")
    public String myCenter(HttpServletRequest request, Model model){

        //session中获取用户标识
       User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //根据用户标识获取账户可用余额
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccountByUid(sessionUser.getId());

        model.addAttribute("financeAccount",financeAccount);

        return "myCenter";
    }
}
