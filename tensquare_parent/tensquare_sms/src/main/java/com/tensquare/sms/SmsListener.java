package com.tensquare.sms;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
/**
 * 短信监听类（消息消费者）
 */
@Component // 生效
@RabbitListener(queues = "itcast")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.temp_code}")
    private String tempCode;

    @Value("${aliyun.sms.sign_name}")
    private String signName;

    /**
     * 消息处理
     */
    @RabbitHandler
    public void handlerMessage(Map message){
        System.out.println("手机号码"+message.get("mobile"));
        System.out.println("验证码"+message.get("code"));

        //使用阿里云短信发送短信
        try {
            SendSmsResponse sendSmsResponse = smsUtil.sendSms((String) message.get("mobile"), tempCode, signName, "{\"code\":\"" + message.get("code") + "\"}");

            if(sendSmsResponse.getCode().equals("OK")){
                System.out.println("短信发送成功");
            }else{
                System.out.println("短信发送失败："+sendSmsResponse.getCode());
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }
}
