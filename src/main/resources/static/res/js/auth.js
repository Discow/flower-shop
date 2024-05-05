// 请求发送验证码
function askVerifyCode() {
    var type;
    if ((getCurrentPath() === '/register.html')) {
        type = 1;
    }else if ((getCurrentPath() === '/forgot-password.html')) {
        type = 2;
    }
    $.get('/api/auth/send-verify-code',{
        mailTo: $("#reg-email").val(),
        type: type
    },function (data) {
        layer.msg(data.msg)
    });
}

// 获取图形验证码
function getCaptcha() {
    $.ajax({
        url: '/api/auth/generate-captcha',
        type: 'GET',
        success: function(response) {
            // 请求成功时的回调函数
            codeKey = response.data.codeKey;
            var captchaBase64Encoded = response.data.captchaBase64Encoded;
            var captchaImg = document.getElementById('captchaImg');
            captchaImg.src = captchaBase64Encoded;
            console.log('接收成功', response);
        },
        error: function(xhr, status, error) {
            // 请求失败时的回调函数
            console.error('接收失败', error);
        }
    })
}

//从后台获取登录是否需要图形验证码
function loginRequireCaptcha() {
    $.ajax({
        url: '/api/auth/is-require-captcha',
        type: 'GET',
        success: function(response) {
            // 请求成功时的回调函数
            if (response.code === 1) {
                loginShowCaptcha();
            }
        },
        error: function(xhr, status, error) {
            // 请求失败时的回调函数
            console.error('接收失败', error);
        }
    })
}

function loginShowCaptcha() {
    if (getCurrentPath() === '/login.html') {
        $('#vercode-form-item').show(); //展示验证码输入框
        var inputVerCode = document.getElementById('input-vercode');
        inputVerCode.setAttribute("lay-verify", "required"); //设置为必填字段
        getCaptcha(); //展示验证码
    }
}

function getCurrentPath() {
    var currentUrl = window.location.href; // "http://localhost:8080/login.html"
    var currentPath=currentUrl.substring(currentUrl.lastIndexOf("/")); // "/login.html"
    return currentPath;
}

var codeKey="" //定义codeKey全局变量

//预加载图形验证码，注册与忘记密码界面默认加载验证码，登录界面按需加载
$(document).ready(function() {
    if (getCurrentPath() === '/register.html' || getCurrentPath() === '/forgot-password.html') {
        getCaptcha();
    } else loginRequireCaptcha();
});

// 登录操作
layui.use(function(){
    var form = layui.form;
    var layer = layui.layer;
    // 提交事件
    form.on('submit(demo-login)', function(data){
        var field = data.field; // 获取表单字段值
        field.codeKey=codeKey
        // 显示填写结果，仅作演示用
        console.log(JSON.stringify(field));
        // var fieldJSON=JSON.stringify(field)

        // 此处可执行 Ajax 等操作
        $.ajax({
            url: "/api/auth/doLogin",
            type: "POST",
            // contentType: 'application/json', //Spring Security暂不支持json格式的数据
            // data: fieldJSON,
            data: field,
            success: function (response) {
                // 一次登录失败显示验证码，并修改其为必填选项（lay-verify="required"）
                if (response.code === 500) {
                    loginShowCaptcha();
                    layer.msg(response.msg, {icon: 0})
                }else if (response.code === 200) {
                    /**
                     * response.data的数据格式：
                     * {"password":null,"username":"123456@qq.com","authorities":[],
                     * "accountNonExpired":true,"accountNonLocked":true,"credentialsNonExpired":true,"enabled":true}
                     */
                    // 存入sessionStorage
                    // sessionStorage.setItem("principal", JSON.stringify(response.data))
                    // 从sessionStorage取出
                    // var principal=JSON.parse(sessionStorage.getItem("principal"))
                    // console.log('用户信息：'+principal)

                    //从Spring security的principal获取用户组和账号
                    var role = response.data.authorities[0].authority; //获取角色
                    var email = response.data.username; //获取已登录用户的账号
                    localStorage.setItem("role", role);

                    //获取用户信息，并保存到sessionStorage
                    $.get('/api/auth/my-profile', function (response) {
                        localStorage.setItem("profile", JSON.stringify(response.data))
                        //跳转到对应角色的页面
                        if (role === 'USER') {
                            window.location.href = '/index.html';
                        } else {
                            window.location.href = '/admin.html';
                        }
                    });
                }else layer.msg(response.msg, {icon: 0})
            },
            error: function(xhr, status, error) {
                // 请求失败时的回调函数
                console.error('登录失败', error);
            }
        });
        return false; // 阻止默认 form 跳转
    });
});

//注册操作
layui.use(function(){
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var util = layui.util;

    // 自定义验证规则
    form.verify({
        // 确认密码
        confirmPassword: function(value, item){
            var passwordValue = $('#reg-password').val();
            if(value !== passwordValue){
                return '两次密码输入不一致';
            }
        }
    });

    // 提交事件
    form.on('submit(demo-reg)', function(data){//点击注册按钮动作
        var field = data.field; // 获取表单字段值

        // 是否勾选同意
        if(!field.agreement){
            layer.msg('您必须勾选同意用户协议才能注册');
            return false;
        }

        //向表单添加codeKey并转换成JSON
        field.codeKey=codeKey
        // var fieldJSON=JSON.stringify(field)
        // 此处可执行 Ajax 等操作
        $.ajax({
            url: '/api/auth/doRegister', // Controller的URL
            type: 'POST',
            // contentType: 'application/json', // 设置Content-Type为application/json
            // data: fieldJSON,
            data: field,
            success: function(response) {
                // 请求成功时的回调函数
                if (response.code === 500) {
                    layer.msg("注册失败，请检查验证码是否填写正确", {icon: 0});
                }else {
                    layer.alert('注册成功，即将跳转到登录页面', {
                        time: 5*1000,
                        success: function(layero, index){
                            var timeNum = this.time/1000, setText = function(start){
                                layer.title('<span class="layui-font-red">'+ (start ? timeNum : --timeNum) + '</span> 秒后自动关闭', index);
                            };
                            setText(!0);
                            this.timer = setInterval(setText, 1000);
                            if(timeNum <= 0) clearInterval(this.timer);
                        },
                        end: function(){
                            clearInterval(this.timer);
                            location.href = "/login.html";
                        }
                    });
                }
            },
            error: function(xhr, status, error) {
                // 请求失败时的回调函数
                console.error('注册失败', error);
            }
        });

        // 显示填写结果，仅作演示用
        console.log(JSON.stringify(field));

        return false; // 阻止默认 form 跳转
    });

    // 普通事件
    util.on('lay-on', {
        // 获取验证码
        'reg-get-vercode': function(othis){
            var isvalid = form.validate('#reg-email'); // 主动触发验证，v2.7.0 新增
            // 验证通过
            if(isvalid){
                layer.msg('电子邮件规则验证通过');
                // 此处可继续书写「发送验证码」等后续逻辑
                // …
                askVerifyCode()
            }
        }
    });
});

//重置密码
layui.use(function(){
    var $ = layui.$;
    var form = layui.form;
    var layer = layui.layer;
    var util = layui.util;

    // 自定义验证规则
    form.verify({
        // 确认密码
        confirmPassword: function(value, item){
            var passwordValue = $('#reg-password').val();
            if(value !== passwordValue){
                return '两次密码输入不一致';
            }
        }
    });

    // 提交事件
    form.on('submit(demo-forgot)', function(data){//点击注册按钮动作
        var field = data.field; // 获取表单字段值
        //向表单添加codeKey并转换成JSON
        field.codeKey=codeKey
        // var fieldJSON=JSON.stringify(field)
        // 此处可执行 Ajax 等操作
        $.ajax({
            url: '/api/auth/forgot-password', // Controller的URL
            type: 'POST',
            // contentType: 'application/json', // 设置Content-Type为application/json
            // data: fieldJSON,
            data: field,
            success: function(response) {
                // 请求成功时的回调函数
                if (response.code === 400) {
                    layer.msg(response.msg, {icon: 0});
                }else {
                    layer.alert('密码重置成功，即将跳转到登录页面', {
                        time: 5*1000,
                        success: function(layero, index){
                            var timeNum = this.time/1000, setText = function(start){
                                layer.title('<span class="layui-font-red">'+ (start ? timeNum : --timeNum) + '</span> 秒后自动关闭', index);
                            };
                            setText(!0);
                            this.timer = setInterval(setText, 1000);
                            if(timeNum <= 0) clearInterval(this.timer);
                        },
                        end: function(){
                            clearInterval(this.timer);
                            location.href = "/login.html";
                        }
                    });
                }
            },
            error: function(xhr, status, error) {
                // 请求失败时的回调函数
                console.error('请求失败', error);
            }
        });

        // 显示填写结果，仅作演示用
        console.log(JSON.stringify(field));

        return false; // 阻止默认 form 跳转
    });

    // 普通事件
    util.on('lay-on', {
        // 获取验证码
        'reg-get-vercode': function(othis){
            var isvalid = form.validate('#reg-email'); // 主动触发验证，v2.7.0 新增
            // 验证通过
            if(isvalid){
                layer.msg('电子邮件规则验证通过');
                // 此处可继续书写「发送验证码」等后续逻辑
                // …
                askVerifyCode()
            }
        }
    });
});