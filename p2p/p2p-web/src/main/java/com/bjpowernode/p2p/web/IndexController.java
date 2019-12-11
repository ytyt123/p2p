package com.bjpowernode.p2p.web;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.user.User;
import com.bjpowernode.p2p.service.loan.BidInfoService;
import com.bjpowernode.p2p.service.loan.LoanInfoService;
import com.bjpowernode.p2p.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private BidInfoService bidInfoService;

    @RequestMapping(value = "/index")
    public String index(HttpServletRequest request, Model model) {
        //获取历史年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        model.addAttribute(Constants.HISTORY_AVERAGE_RATE, historyAverageRate);
        //获取平台注册总人数
        Long allUserCount = userService.queryAllUserCount();
        model.addAttribute(Constants.ALL_USER_COUNT, allUserCount);
        //获取平台累计投资金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constants.ALL_BID_MONEY, allBidMoney);

        //将以下查询看成是一个分页，实际功能:根据产品类型查询产品信息显示前几个
        //loanInfoService.queryLoanInfoListByProductType(产品类型，页码，每页显示条数);
        //数据持久层用户的是limit函数 limit 起始下标，截取长度
        Map<String, Object> paramMap = new HashMap<>();
        //页码：起始下标
        paramMap.put("currentPage", 0);
        //获取新手宝产品：产品类型：0，显示第一页，每页显示1个
        paramMap.put("productType", Constants.PRODUCT_TYPE_X);
        paramMap.put("pageSize", 1);
        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        //获取优选产品：产品类型：1，显示第一页，每页显示4个
        paramMap.put("productType", Constants.PRODUCT_TYPE_U);
        paramMap.put("pageSize", 4);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        //获取散标产品：产品类型：2，显示第一页，每页显示8个
        paramMap.put("productType", Constants.PRODUCT_TYPE_S);
        paramMap.put("pageSize", 8);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);

        model.addAttribute("xLoanInfoList", xLoanInfoList);
        model.addAttribute("uLoanInfoList", uLoanInfoList);
        model.addAttribute("sLoanInfoList", sLoanInfoList);
        return "index";
    }
}
