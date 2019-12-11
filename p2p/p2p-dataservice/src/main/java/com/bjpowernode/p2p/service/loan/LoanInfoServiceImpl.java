package com.bjpowernode.p2p.service.loan;

import com.bjpowernode.p2p.common.constant.Constants;
import com.bjpowernode.p2p.mapper.loan.LoanInfoMapper;
import com.bjpowernode.p2p.model.loan.LoanInfo;
import com.bjpowernode.p2p.model.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service("loanInfoServiceImpl")
public class LoanInfoServiceImpl implements LoanInfoService {


    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public Double queryHistoryAverageRate() {
        //先去redis缓存中获取，有则直接使用，没有则去数据库查询并放到redis缓存中

        //修改redis中key值序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //获取操作key=value的数据类型的redis的操作对象,并获取指定key的value值
        Double historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);

        //判断是否有值
        if (historyAverageRate == null) {
            //没有值,去在数据库查询
            historyAverageRate = loanInfoMapper.selectHistoryAverageRate();
            //将该值存放到redis缓存中
            redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE, historyAverageRate, 15, TimeUnit.MINUTES);
        }
        return historyAverageRate;
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {
        return loanInfoMapper.selectLoanInfoByPage(paramMap);
    }

    @Override
    public PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paraMap) {
        PaginationVO<LoanInfo> paginationVO = new PaginationVO<>();

        Long tatal = loanInfoMapper.selectTotal(paraMap);
        //查询的总记录数
        paginationVO.setTotal(tatal);

        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoByPage(paraMap);
        //查询显示数据
        paginationVO.setDataList(loanInfoList);
        return paginationVO;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return  loanInfoMapper.selectByPrimaryKey(id);
    }
}
