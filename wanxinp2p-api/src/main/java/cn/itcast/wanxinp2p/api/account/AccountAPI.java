package cn.itcast.wanxinp2p.api.account;

import cn.itcast.wanxinp2p.api.account.model.AccountDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountLoginDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountRegisterDTO;
import cn.itcast.wanxinp2p.common.domain.RestResponse;

public interface AccountAPI {

    RestResponse getSMSCode(String mobile);
    RestResponse<Integer> checkMobile(String mobile, String key, String code);

    //account registration
    RestResponse<AccountDTO> register(AccountRegisterDTO accountRegisterDTO);

    RestResponse<AccountDTO> login(AccountLoginDTO accountLoginDTO);
}
