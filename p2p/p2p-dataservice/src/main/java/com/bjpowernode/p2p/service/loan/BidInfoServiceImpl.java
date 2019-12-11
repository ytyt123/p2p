package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.BidInfoMapper;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.mapper.user.FinanceAccountMapper;
import com.bjpowernode.p2p.model.loan.BidInfo;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.BidUserTop;
import com.bjpowernode.p2p.model.vo.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService {
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public Double queryAllBidMoney() {
        //先去redis缓存中获取，有则直接使用，没有则去数据库查询并放到redis缓存中

        //修改redis中key值序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //获取指定key的操作对象
        BoundValueOperations<Object, Object> boundValueOps =
                redisTemplate.boundValueOps(Constants.ALL_BID_MONEY);
        //获取指定key的值
        Double allBidMooney = (Double) boundValueOps.get();
        //判断是否有值
        if (allBidMooney == null) {
            //去数据库查找
            allBidMooney = bidInfoMapper.selectAllBidMoney();
            //将查找的值放到redis
            boundValueOps.set(allBidMooney, 15, TimeUnit.MINUTES);
        }
        return allBidMooney;
    }

    @Override
    public List<BidInfo> queryBidInfoListByLoanId(Integer loanId) {
        return bidInfoMapper.selectBidInfoListByLoanId(loanId);
    }

    @Override
    public ResultObject invest(Map<String, Object> paramMap) {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);
        System.out.println("hi");
        //超卖现象，实际销售的数量超过了原有的销售数量
        //使用数据库乐观锁解决超卖

        //获取产品的版本号
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey((Integer)paramMap.get("loanId"));
        paramMap.put("version", loanInfo.getVersion());
        //更新产品剩余可投金额
        int updateLeftProductMoneyCount =
                loanInfoMapper.updateLeftProductMoneyByLoanId(paramMap);
        if (updateLeftProductMoneyCount > 0) {
            //更新账户可用余额
            int updateFinanceAccountCount =
                    financeAccountMapper.updateFinanceAccountByBid(paramMap);
            if (updateFinanceAccountCount > 0) {
                //新增投资记录
                BidInfo bidInfo = new BidInfo();
                //投资金额
                bidInfo.setBidMoney((Double) paramMap.get("bidMoney"));
                //用户标识
                bidInfo.setUid((Integer) paramMap.get("uid"));
                //产品标识
                bidInfo.setLoanId((Integer) paramMap.get("loanId"));
                //投资时间
                bidInfo.setBidTime(new Date());
                //投资状态
                bidInfo.setBidStatus(1);

                int insertBidCount = bidInfoMapper.insertSelective(bidInfo);
                if (insertBidCount > 0) {
                    //再次查看产品的剩余可投金额是否为0
                    LoanInfo loanDetail = loanInfoMapper.selectByPrimaryKey((Integer) paramMap.get("loanId"));
                    if (0 == loanDetail.getLeftProductMoney()) {
                        //为0，更新产品的状态及满标时间
                        LoanInfo updateLoanInfo = new LoanInfo();
                        updateLoanInfo.setId(loanDetail.getId());
                        updateLoanInfo.setProductFullTime(new Date());
                        updateLoanInfo.setProductStatus(1);//0未满标，1满标，2满标且生成收益
                        int updateLoanInfoCount = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
                        if (updateLoanInfoCount < 0) {
                            resultObject.setErrorCode(Constants.FAIL);
                        }
                    }
                    //将用户的投资金额存放到redis缓存中
                    redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,
                            (String)paramMap.get("phone"),(Double)paramMap.get("bidMoney"));

                } else {
                    resultObject.setErrorCode(Constants.FAIL);
                }
            } else {
                resultObject.setErrorCode(Constants.FAIL);
            }
        } else {
            resultObject.setErrorCode(Constants.FAIL);
        }


        return resultObject;
    }

    @Override
    public List<BidUserTop> queryBidUserTop() {
        List<BidUserTop> bidUserTopList = new ArrayList<>();

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP,
                0, 9);
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();

         while (iterator.hasNext()){
             ZSetOperations.TypedTuple<Object> next = iterator.next();
             String phone = (String) next.getValue();
             Double score = next.getScore();

             BidUserTop bidUserTop = new BidUserTop();
             bidUserTop.setPhone(phone);
             bidUserTop.setScore(score);
             bidUserTopList.add(bidUserTop);
         }
        return bidUserTopList;
    }
}
