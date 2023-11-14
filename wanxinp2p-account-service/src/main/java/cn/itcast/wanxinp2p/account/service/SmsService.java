package cn.itcast.wanxinp2p.account.service;

import cn.itcast.wanxinp2p.common.domain.RestResponse;
import cn.itcast.wanxinp2p.common.util.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${sms.url}")
    private String smsURL;
    @Value("${sms.enable}")
    private Boolean smsEnable;

    public RestResponse getSmsCode(String mobile) {
        if(smsEnable){
            return OkHttpUtil.post(smsURL+"/generate?effectiveTime=300&name=sms","{\"mobile\":"+mobile+"}");
        }

        return RestResponse.success();
    }

    public void verifySmsCode(String key, String code){
        if(smsEnable){
            //http://localhost:56085/sailing/verify?name=sms&verificationKey=xxx&verficationCode=xxx
            StringBuilder params = new StringBuilder("/verify?name=sms");
            params.append("&verificationKey=").append(key).append("&verificationCode=").append(code);
            RestResponse restResponse = OkHttpUtil.post(smsURL+params,"");
            if(restResponse.getCode() != 0 || restResponse.getResult().toString().equalsIgnoreCase("false")){
                throw new RuntimeException();
            }
        }
    }

}
