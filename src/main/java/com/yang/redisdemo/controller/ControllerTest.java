package com.yang.redisdemo.controller;

import com.yang.redisdemo.constants.Constants;
import com.yang.redisdemo.util.MathUtils;
import com.yang.redisdemo.util.RedisUtils;
import com.yang.redisdemo.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/test",produces = "text/html;charset=utf-8")
public class ControllerTest {
    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping("add")
    public String add(String k,String v){
        redisUtils.putValue(k,v,60);
        return "add_success";
    }

    @GetMapping("get")
    public String get(String k){
        String v = redisUtils.getValue(k);
        if (v!=null){
            return "get_success"+v;
        }else{
            return "get_field";
        }
    }

    @PostMapping("getLoginCode")
    public String getLoginCode(String Phone){
        //产生随机四位数随机码
        String Code = MathUtils.random();
        //产生六位随机数，不过后面会有小数，暂时未解决
//        String num = String.valueOf((((Math.random()*9)+1)*100000));
        //生成redis的key,项目编码:业务编码:用户TEL:0(登陆注册专用)
        String key = StringUtil.formatKeyWithPrefix(
                Constants.RedisKey.PROJECT_PRIFIX,
                Constants.RedisKey.SMS_PRIFIX,
                Phone,
                Constants.Sms.CodeType.LOGIN_OR_REGISTER+""
                );
        redisUtils.putValue(key,Code,120);
        return "sendLoginCodeOK";
    }
}
