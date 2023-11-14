package cn.itcast.wanxinp2p.account.service;

import cn.itcast.wanxinp2p.account.entity.Account;
import cn.itcast.wanxinp2p.account.mapper.AccountMapper;
import cn.itcast.wanxinp2p.api.account.model.AccountDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountLoginDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountRegisterDTO;
import cn.itcast.wanxinp2p.common.domain.RestResponse;
import cn.itcast.wanxinp2p.common.util.PasswordUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService{

    @Autowired
    private SmsService smsService;

    @Value("${sms.enable}")
    private Boolean smsEnable;

    @Override
    public RestResponse getSMScode(String mobile) {
        return smsService.getSmsCode(mobile);
    }

    @Override
    public Integer checkMobile(String mobile, String key, String code) {
        smsService.verifySmsCode(key,code);

        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        int count = count(wrapper);

        return count>0?1:0;
    }

    @Override
    public AccountDTO register(AccountRegisterDTO accountRegisterDTO) {
        Account account = new Account();
        account.setUsername(accountRegisterDTO.getUsername());
        account.setMobile(accountRegisterDTO.getMobile());
        account.setPassword(PasswordUtil.generate(accountRegisterDTO.getPassword()));

        if(smsEnable){
            account.setPassword(PasswordUtil.generate(accountRegisterDTO.getMobile()));
        }
        account.setDomain("c");
        save(account);

        return convertAccountEntityToDTO(account);
    }

    @Override
    public AccountDTO login(AccountLoginDTO accountLoginDTO) {
        //check username, password at the same time -> login
        //check username, password one by one
        Account account = null;
        if(accountLoginDTO.getDomain().equalsIgnoreCase("c")){
            //c domain customer -> username = mobile
            account = getAccountByMobile(accountLoginDTO.getMobile());
        } else{
            //b domain -> username = account
            account = getAccountByUsername(accountLoginDTO.getUsername());
        }

        if(account == null){
            //用户不存在
            throw new RuntimeException();
        }

        //compare password
        AccountDTO accountDTO = convertAccountEntityToDTO(account);

        //is sms service is enable -> use sms to login, else use password
        if(smsEnable){
            return accountDTO;
        }

        if(PasswordUtil.verify(accountLoginDTO.getPassword(),account.getPassword())){
            return accountDTO;
        }
        throw new RuntimeException();
    }

    private AccountDTO convertAccountEntityToDTO(Account entity){
        if(entity == null){
            return null;
        }
        AccountDTO dto = new AccountDTO();
        BeanUtils.copyProperties(entity,dto);
        return dto;
    }

    private Account getAccountByMobile(String mobile){
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        return getOne(wrapper);
    }

    private Account getAccountByUsername(String username){
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return getOne(wrapper);
    }
}
