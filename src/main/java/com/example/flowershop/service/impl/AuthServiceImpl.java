package com.example.flowershop.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.codec.Base64;
import com.example.flowershop.entity.LoginRecord;
import com.example.flowershop.entity.User;
import com.example.flowershop.repositories.LoginRecordRepository;
import com.example.flowershop.repositories.UserRepository;
import com.example.flowershop.service.AuthService;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户身份验证服务
 */
@Log
@Transactional
@Service
public class AuthServiceImpl implements AuthService {
    private final JavaMailSenderImpl sender;
    private final StringRedisTemplate stringRedisTemplate;
    private final UserRepository userRepository;
    private final LoginRecordRepository loginRecordRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthServiceImpl(JavaMailSenderImpl sender, StringRedisTemplate stringRedisTemplate, UserRepository userRepository, LoginRecordRepository loginRecordRepository) {
        this.sender = sender;
        this.stringRedisTemplate = stringRedisTemplate;
        this.userRepository = userRepository;
        this.loginRecordRepository = loginRecordRepository;
    }

    @Value("${spring.mail.username}")
    String mailFrom;

    /**
     *
     * @param mailTo 接收方邮件地址
     * @param type 1：注册，2：重置密码
     */
    @SneakyThrows
    @Override
    public void sendMailVerifyCode(String mailTo, Integer type) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        Properties properties = sender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", true); //outlook邮箱需要开启tls加密
        //将生成的六位数验证码保存到redis
        String mailEncode = Base64.encode(mailTo);
        String verifyCode = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        stringRedisTemplate.opsForValue().set(mailEncode, verifyCode, 3, TimeUnit.MINUTES);
        helper.setSubject("Doge开发团队");
        if (type == 1) {
            helper.setText("您正在注册“木子·花语”网上花店\n验证码：" + verifyCode + "，3分钟以内有效，请妥善保管");
        } else if (type == 2) {
            helper.setText("您正在重置密码\n验证码：" + verifyCode + "，3分钟以内有效，请妥善保管");
        }
//        File file = new File("C:\\Users\\VMWIN10\\Downloads\\Redis-x64-5.0.14.1.zip");
//        helper.addAttachment("redis",file);
        helper.setFrom(mailFrom);
        helper.setTo(mailTo);
        sender.send(message);
    }


    /**
     * 生成扭曲干扰验证码
     */
    @Override
    public Map<String, Object> generateShearCaptcha() {
        HashMap<String, Object> map = new HashMap<>();
//        定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(123, 38, 4, 3);
//        ShearCaptcha captcha = new ShearCaptcha(200, 100, 4, 4);
//        图形验证码写出，可以写出到文件，也可以写出到流
//        captcha.write("C:\\Users\\VMWIN10\\Downloads\\shear.png");
        String captchaBase64Encoded = captcha.getImageBase64Data();//获取图片验证码的base64编码
//        设置键值并将其保存到redis
        String codeKey = "captcha" + System.currentTimeMillis() + "";
        String codeVal = captcha.getCode();
        stringRedisTemplate.opsForValue().set(codeKey, codeVal, 3, TimeUnit.MINUTES);//将图片验证码的值写入redis
        map.put("codeKey", codeKey);
        map.put("captchaBase64Encoded", captchaBase64Encoded);
        return map;
    }

    /**
     * 校验图形验证码
     *
     * @param codeKey          验证码在redis中的键
     * @param userInputCaptcha 用户输入的验证码
     * @return 返回验证结果bool值
     */
    @Override
    public boolean checkShearCaptcha(String codeKey, String userInputCaptcha) {
        String captchaVal = stringRedisTemplate.opsForValue().get(codeKey);
        if (Objects.nonNull(captchaVal) && Objects.nonNull(userInputCaptcha) && captchaVal.equalsIgnoreCase(userInputCaptcha)) {
            return true;
        } else return false;
    }

    /**
     * 校验邮箱验证码
     *
     * @param mail                用户的邮箱地址
     * @param userInputVerifyCode 用户输入的验证码
     * @return 返回校验结果bool值
     */
    @Override
    public boolean checkMailVerifyCode(String mail, String userInputVerifyCode) {
        String mailEncode = Base64.encode(mail);
        String verifyCodeFromRedis = stringRedisTemplate.opsForValue().get(mailEncode);
        if (verifyCodeFromRedis == null || !verifyCodeFromRedis.equals(userInputVerifyCode)) {
            return false;
        }
        stringRedisTemplate.delete(mail);//验证成功后删除验证码
        return true;
    }

    /**
     * 已登录的用户修改密码（普通用户与管理员）
     * @param email 邮箱
     * @param password 原密码
     * @param newPassword 新密码
     * @return 修改密码结果
     */
    @Override
    public boolean modifyPassword(String email, String password, String newPassword) {
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        //获取用户组
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) { //判断是否已经登录
            //校验原密码是否正确，哈希加盐不能直接比较
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                return userRepository.updatePwdByEmail(encodedNewPassword, email);
            }
        }
        return false;
    }

    @Override
    public boolean forgotPassword(String newPassword, String email) {
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        return userRepository.updatePwdByEmail(encodedNewPassword, email);
    }

    @Override
    public boolean register(String email, String password, String username, String phone) {
        User user = User.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .phone(phone)
                .role(User.Role.USER)
                .build();

        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
            return true;
        } else {
            // 处理用户已存在的情况
            return false;
        }
    }

    //获取用户信息
    @Override
    public User getProfile(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) { //判断是否已经登录
            return userRepository.findByEmail(email).orElse(null);
        }
        return null;
    }

    /**
     * 记录登录历史
     * @param ipAddr 客户端IP地址
     * @param email  电子邮箱
     * @param status 登录状态（成功、失败）
     */
    @Override
    public void addRecord(String ipAddr, String email, String status) {
        User user = userRepository.findByEmail(email).orElse(null);
        LoginRecord record = new LoginRecord();
        record.setTime(new Date());
        record.setIp(ipAddr);
        record.setStatus(status);
        record.setUser(user);
        loginRecordRepository.save(record);
    }

    @Override
    public Page<LoginRecord> findRecord(String email, Integer page, Integer limit) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            Pageable pageable = PageRequest.of(page - 1, limit);
            return loginRecordRepository.findByUserId(user.getId(), pageable);
        }
        return null;
    }

    //Spring Security自定义用户查找逻辑，根据邮箱查找
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        if (user != null) {
            authorities.add(() -> user.getRole().name());
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("找不到用户！");
        }
    }
}
