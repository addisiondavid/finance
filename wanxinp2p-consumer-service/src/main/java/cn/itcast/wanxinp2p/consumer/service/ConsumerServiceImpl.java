package cn.itcast.wanxinp2p.consumer.service;

import cn.itcast.wanxinp2p.api.account.model.AccountDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountRegisterDTO;
import cn.itcast.wanxinp2p.api.consumer.model.ConsumerDTO;
import cn.itcast.wanxinp2p.api.consumer.model.ConsumerRegisterDTO;
import cn.itcast.wanxinp2p.common.domain.CodePrefixCode;
import cn.itcast.wanxinp2p.common.domain.RestResponse;
import cn.itcast.wanxinp2p.common.util.CodeNoUtil;
import cn.itcast.wanxinp2p.consumer.agent.AccountApiAgent;
import cn.itcast.wanxinp2p.consumer.entity.Consumer;
import cn.itcast.wanxinp2p.consumer.mapper.ConsumerMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumerServiceImpl extends ServiceImpl<ConsumerMapper, Consumer> implements ConsumerService{

    @Autowired
    private AccountApiAgent accountApiAgent;

    @Override
    public Integer checkMobile(String mobile) {

        return getByMobile(mobile)!=null?1:0;
    }

    private ConsumerDTO getByMobile(String mobile){
        QueryWrapper<Consumer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile",mobile);
        Consumer consumer = getOne(queryWrapper);

        return convertConsumerEntityToDTO(consumer);
    }

    @Override
    public void register(ConsumerRegisterDTO consumerRegisterDTO) {
        if(checkMobile(consumerRegisterDTO.getMobile())==1){
            //if it equals 1 means phone number found in database.... dont need to register, -> throw error
            throw new RuntimeException();
        }

        //System.out.println("consumerRegisterDTO.getMobile()................"+consumerRegisterDTO.getMobile());
        //register in consumer service
        Consumer consumer = new Consumer();
        BeanUtils.copyProperties(consumerRegisterDTO,consumer);
        //default setting
        consumer.setUserNo(CodeNoUtil.getNo(CodePrefixCode.CODE_REQUEST_PREFIX));
        consumer.setIsBindCard(0);
        consumer.setUserName(CodeNoUtil.getNo(CodePrefixCode.CODE_NO_PREFIX));

        consumerRegisterDTO.setUsername(consumer.getUserName());

        save(consumer);

        //Feign, call account microservice
        //also register in account service
        AccountRegisterDTO accountRegisterDTO = new AccountRegisterDTO();
        BeanUtils.copyProperties(consumerRegisterDTO,accountRegisterDTO);
        RestResponse<AccountDTO> restResponse = accountApiAgent.register(accountRegisterDTO);

        if(restResponse.getCode()!=0){
            throw new RuntimeException();
        }


    }

    private ConsumerDTO convertConsumerEntityToDTO(Consumer entity){
        if(entity == null){
            return null;
        }

        ConsumerDTO dto = new ConsumerDTO();
        BeanUtils.copyProperties(entity,dto);
        return dto;
    }
}
