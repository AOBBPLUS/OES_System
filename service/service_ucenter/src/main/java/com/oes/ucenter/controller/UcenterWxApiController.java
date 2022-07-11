package com.oes.ucenter.controller;

import com.google.gson.Gson;
import com.oes.commonutils.JwtUtils;
import com.oes.commonutils.ResultCodeEnum;
import com.oes.servicebase.exceptionhandler.OesException;
import com.oes.ucenter.entity.UcenterMember;
import com.oes.ucenter.service.UcenterMemberService;
import com.oes.ucenter.utils.ConstantWxUtils;
import com.oes.ucenter.utils.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

@Api(tags = "学员微信登录管理控制器")
@Controller
@RequestMapping("/api/ucenter/wx")
public class UcenterWxApiController {
    private UcenterMemberService memberService;

    @Autowired
    public void setMemberService(UcenterMemberService memberService) {
        this.memberService = memberService;
    }

    // 尚硅谷提供的接口回调方法,实际开发时不需要这么复杂,只需要跳转到服务器即可
    @ApiOperation(value = "不需要前端调用")
    @GetMapping("callback")
    public String callback(String code, String state) {

        // 第一步：得到授权临时票据code
        System.out.println("code: " + code);
        System.out.println("state: " + state);

        // 第二步：得到code 和 state后，根据code请求微信提供的固定地址，得到两个值：
        // access_token:访问凭证，openid:微信唯一标识
        String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=%s" +
                "&secret=%s" +
                "&code=%s" +
                "&grant_type=authorization_code";
        // 完善上面请求地址的参数
        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                ConstantWxUtils.WX_OPEN_APP_SECRET,
                code
        );

        // 需要采用httpclient
        try {
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo: " + accessTokenInfo);

            // accessToken最终返回的是json格式的字符串，我们需要解析这串字符串，采用gson工具
            Gson gson = new Gson();
            HashMap tokenMap = gson.fromJson(accessTokenInfo, HashMap.class);

            // 从map中获取access_token和openid
            // access_token:访问凭证
            // openid:每个微信唯一标识
            String access_token = (String) tokenMap.get("access_token");
            String openid = (String) tokenMap.get("openid");

            // 接着将扫码人的信息保存到数据库中
            // 先根据openid查询数据库是否有该微信用户信息
            UcenterMember ucenterMember = memberService.getMemberByOpenId(openid);

            // 如果没有该用户信息,则请求微信的服务器,并将用户注册到本地
            if (ucenterMember == null) {
                // 第三步：再去请求一个固定地址,得到微信扫码人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";

                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);

                // 还是采用httpClient
                String userInfo = HttpClientUtils.get(userInfoUrl);

                // 解析
                HashMap userMap = gson.fromJson(userInfo, HashMap.class);
                // 微信的昵称
                String nickname = (String) userMap.get("nickname");
                // 微信头像路径
                String headimgurl = (String) userMap.get("headimgurl");
                System.out.println("headimgurl: " + headimgurl);

                // 添加到数据库中
                ucenterMember = new UcenterMember();

                ucenterMember.setNickname(nickname);
                ucenterMember.setAvatar(headimgurl);
                ucenterMember.setOpenid(openid);

                boolean save = memberService.save(ucenterMember);

                if (!save) throw new OesException(ResultCodeEnum.SAVE_USER_FAILED);
            }

            //使用jwt根据ucenterMember对象生成token字符串
            String token = JwtUtils.getJwtToken(
                    ucenterMember.getId(), ucenterMember.getNickname()
            );

            //返回首页。通过路径传递token字符串
            return "redirect:http://localhost:3000?token=" + token;

        } catch (Exception e) {
            e.printStackTrace();
            throw new OesException(ResultCodeEnum.LOGIN_FAILED);
        }
    }

    // 生成微信扫描二维码
    @GetMapping("userLogin")
    public String login() {
        // 微信开放平台授权baseUrl
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        // 回调地址
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 防止csrf攻击（跨站请求伪造攻击）
        String state = "zhengH";

        // 生成qrcodeUrl
        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                state);

        // 重定向到这个地址
        return "redirect:" + qrcodeUrl;
    }
}
