package cn.itcast.wanxinp2p.account.controller;

import cn.itcast.wanxinp2p.account.service.AccountService;
import cn.itcast.wanxinp2p.api.account.AccountAPI;
import cn.itcast.wanxinp2p.api.account.model.AccountDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountLoginDTO;
import cn.itcast.wanxinp2p.api.account.model.AccountRegisterDTO;
import cn.itcast.wanxinp2p.common.domain.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "统一账户服务的API")
public class AccountController implements AccountAPI {

    @Autowired
    private AccountService accountService;

    @ApiOperation("测试")
    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }

    @ApiOperation("获取手机sms code")
    @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String")
    @GetMapping("/sms/{mobile}")
    @Override
    public RestResponse getSMSCode(@PathVariable String mobile) {

        return accountService.getSMScode(mobile);
    }

    @ApiOperation("验证手机sms_code")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "key", value = "校验标识", required = true ,dataType = "String"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true ,dataType = "String")
    })
    @GetMapping("/mobiles/{mobile}/key/{key}/code/{code}")
    public RestResponse<Integer> checkMobile(@PathVariable String mobile,@PathVariable String key,@PathVariable String code) {
        return RestResponse.success(accountService.checkMobile(mobile,key,code));
    }
    //内部调用， 前端不用
    @ApiOperation("用户注册")
    @ApiImplicitParam(name = "accountRegisterDTO", value = "账户注册信息", dataType = "AccountRegisterDTO", required = true, paramType="body")
    @PostMapping(value ="/l/accounts")
    @Override
    public RestResponse<AccountDTO> register(@RequestBody AccountRegisterDTO accountRegisterDTO) {
        return RestResponse.success(accountService.register(accountRegisterDTO));
    }

    @ApiOperation("用户登录")
    @ApiImplicitParam(name = "accountLoginDTO", value = "登录信息", dataType = "AccountLoginDTO", required = true, paramType="body")
    @PostMapping(value ="/l/accounts/session")
    @Override
    public RestResponse<AccountDTO> login(@RequestBody AccountLoginDTO accountLoginDTO) {
        return RestResponse.success(accountService.login(accountLoginDTO));
    }


}
