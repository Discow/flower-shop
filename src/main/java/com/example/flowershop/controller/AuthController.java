package com.example.flowershop.controller;

import com.example.flowershop.dto.RestBean;
import com.example.flowershop.entity.LoginRecord;
import com.example.flowershop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController //自动将控制器方法的返回值转换为JSON或XML格式的响应体，并发送给客户端，省略在每个方法加上@RestController
//传入参数用于接收来自前端的数据，返回值返回给前端
@RequestMapping("/api/auth/")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 发送邮件验证码
     *
     * @param mailTo 接收方邮件地址
     * @param type   1：注册，2：重置密码
     */
    @GetMapping("send-verify-code")
    public RestBean<?> sendVerifyCode(@RequestParam String mailTo, @RequestParam String type) {
        try {
            authService.sendMailVerifyCode(mailTo, Integer.valueOf(type));
            return RestBean.success("验证码发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure("验证码发送失败");
        }
    }

    //注册操作，验证邮件验证码和图形验证码
    @PostMapping("doRegister")
    public RestBean<?> doRegister(String email, @RequestParam("vercode") String verifyCode, String password,
                                  String username, String phone, String codeKey,
                                  String captcha) {
        if (authService.checkMailVerifyCode(email, verifyCode) && authService.checkShearCaptcha(codeKey, captcha)) {
            if (authService.register(email, password, username, phone)) {
                return RestBean.success("注册成功");
            } else {
                return RestBean.failure("注册失败");
            }
        } else return RestBean.failure("注册失败：验证码错误");
    }

    //登录验证已由Spring Security接管


    //生成验证码
    @GetMapping("generate-captcha")
    public RestBean<?> generateCaptcha() {
        return RestBean.success("验证码生成成功", authService.generateShearCaptcha());
    }

    @GetMapping("is-require-captcha")
    public RestBean<?> isRequireCaptcha(@SessionAttribute(value = "isRequireCaptcha", required = false) Boolean isRequireCaptcha) {
        if (isRequireCaptcha != null && isRequireCaptcha) {
            return RestBean.failure(1, "需要验证码");
        }
        return null;
    }

    //忘记密码
    @PostMapping("forgot-password")
    public RestBean<?> forgotPassword(@RequestParam String email, @RequestParam String password,
                                      @RequestParam String vercode, @RequestParam String codeKey,
                                      @RequestParam String captcha) {
        if (authService.checkMailVerifyCode(email, vercode) && authService.checkShearCaptcha(codeKey, captcha)) {
            if (authService.forgotPassword(password, email)) {
                return RestBean.success("密码重置成功");
            } else {
                return RestBean.failure(400, "密码重置失败");
            }
        } else return RestBean.failure(400, "验证码校验失败");
    }

    //获取当前登录的用户信息
    @PreAuthorize("isAuthenticated()")
    @GetMapping("my-profile")
    public RestBean<?> myProfile(Authentication authentication) {
        Object profile = authService.getProfile(authentication.getName());
        if (profile != null) {
            return RestBean.success("查询成功", profile);
        } else {
            return RestBean.failure(401, "查询失败，用户未验证");
        }
    }

    //修改密码
    @PreAuthorize("isAuthenticated()")
    @PostMapping("modify-password")
    public RestBean<?> modifyPassword(String email, String password, String newPassword) {
        if (newPassword.equals(password)) {
            return RestBean.failure("新密码不能和原密码相同！");
        }
        if (authService.modifyPassword(email, password, newPassword)) {
            return RestBean.success("密码修改成功");
        } else {
            return RestBean.failure("密码修改失败，请检查原密码是否正确");
        }
    }

    //查询登录历史（不能由前端传来账号进行查询，以防隐私泄露）
    @PreAuthorize("isAuthenticated()")
    @GetMapping("login-record")
    public RestBean<?> loginRecord(Authentication authentication,
                                   @RequestParam("page") String page,
                                   @RequestParam("limit") String limit) {
        Page<LoginRecord> record = authService.findRecord(authentication.getName(),
                Integer.valueOf(page),
                Integer.valueOf(limit));
        return RestBean.success(record.getContent(), (int) record.getTotalElements());
    }
}
