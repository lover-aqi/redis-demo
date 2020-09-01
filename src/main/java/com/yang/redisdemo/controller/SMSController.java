package com.yang.redisdemo.controller;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.yang.redisdemo.constants.Constants;
import com.yang.redisdemo.util.MathUtils;
import com.yang.redisdemo.util.RedisUtils;
import com.yang.redisdemo.util.SMSUtils;
import com.yang.redisdemo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sms",produces = "text/html;charset=utf-8")
public class SMSController {
    @Autowired
    private SMSUtils smsUtils;
    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("getCode")
    public String sendCode(String Phone) throws TencentCloudSDKException {
        //随机产生四位数
        String Code = MathUtils.random();
        //产生redis的目录设计
        String key = StringUtil.formatKeyWithPrefix(
                Constants.RedisKey.PROJECT_PRIFIX,
                Constants.RedisKey.SMS_PRIFIX,
                Phone,
                Constants.Sms.CodeType.LOGIN_OR_REGISTER+""
                );
        //将码存入redis
        redisUtils.putValue(key,Code,120);
        boolean result = smsUtils.sendTencentSMS(Phone,Code,"2");
        if (result){
            return "Send_Ok";
        }
        return "Send_Fail";
    }
}
