package com.matt.project.seckill.controller;

import com.matt.project.seckill.controller.viewobject.UserVo;
import com.matt.project.seckill.error.BusinessException;
import com.matt.project.seckill.error.EnumBusinessError;
import com.matt.project.seckill.response.CommonReturnType;
import com.matt.project.seckill.service.UserService;
import com.matt.project.seckill.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author matt
 * @create 2020-12-06 13:46
 */
@RestController("user")
@RequestMapping("/user")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    // 和当前线程进行绑定
    @Autowired
    private  HttpServletRequest httpServletRequest;
    @Autowired
    private RedisTemplate redisTemplate;

    private UserVo convertVOFromModel(UserModel userModel) {

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userModel,userVo);
        return userVo;
    }

    /*
     * 功能描述: 根据用户ID获取用户信息
     * @Param: [id]
     * @Return: com.matt.project.seckill.response.CommonReturnType
     * @Author: matt
     * @Date: 2020/12/15 11:48
     */
    @GetMapping("/get")
    public CommonReturnType getUserById(@RequestParam(name = "id")Integer id) throws BusinessException {

        UserModel userModel = userService.getUserById(id);

        if (userModel == null) {
            throw new BusinessException(EnumBusinessError.USER_NOT_EXISTS);
        }

        UserVo userVo = convertVOFromModel(userModel);
        return CommonReturnType.create(userVo);
    }



   /**
    * 功能：根据手机号获取验证码
    * @author matt
    * @date 2020/12/15
    * @param telephone
    * @return com.matt.project.seckill.response.CommonReturnType
   */
    @PostMapping(value = "getotp")
    public CommonReturnType getOtp(@RequestParam(name="telephone")String telephone) {

        //生成验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt = 100000 + randomInt;
        String otpCode = String.valueOf(randomInt);

        // 和手机号绑定
        redisTemplate.opsForValue().set(telephone,otpCode);
        redisTemplate.expire(telephone, 300, TimeUnit.SECONDS);
        System.out.println("telephone=" + telephone);
        System.out.println("otpCode=" + otpCode);

        return CommonReturnType.create(null);
    }

    /**
     * 功能：用户注册
     * @author matt
     * @date 2020/12/15
     * @param telephone
     * @param otpCode
     * @param name
     * @param gender
     * @param age
     * @param password
     * @return com.matt.project.seckill.response.CommonReturnType
    */
    @PostMapping(value = "/register")
    public CommonReturnType register(@RequestParam(name="telephone")String telephone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        Object otpCodeObj = redisTemplate.opsForValue().get(telephone);


        if (otpCodeObj == null || !(otpCode instanceof String) ||
            !((String)otpCodeObj).equals(otpCode)) {
            throw new BusinessException(EnumBusinessError.OPT_CODE_ERROR);

        }

        UserModel userModel = new UserModel();
        userModel.setTelephone(telephone);
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender)));
        userModel.setAge(age);
        userModel.setEncrptPassword(EncodeByMd5(password));

        userService.register(userModel);

        return CommonReturnType.create(null);
    }

     /**
      * 功能：密码加密
      * @author matt
      * @date 2020/12/15
      * @param str
      * @return java.lang.String
     */
     private String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    /**
     * 功能：用户登录
     * @author matt
     * @date 2020/12/15
     * @param telephone
     * @param password
     * @return com.matt.project.seckill.response.CommonReturnType
    */
    @PostMapping("/login")
    public CommonReturnType login(@RequestParam(name = "telephone")String telephone,
                                  @RequestParam(name = "password")String password)
            throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        // 校验提交信息
        if (StringUtils.isEmpty(telephone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(EnumBusinessError.USER_INFO_EMPTY);
        }

        // 用户名密码判断
        UserModel userModel = userService.validateLogin(telephone,EncodeByMd5(password));
        userModel.setEncrptPassword("");

        String uuidToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(uuidToken,userModel);
        redisTemplate.expire(uuidToken,30, TimeUnit.MINUTES);

        return CommonReturnType.create(uuidToken);

    }

}
