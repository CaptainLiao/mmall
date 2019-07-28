package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

  @Autowired
  private UserMapper userMapper;

  @Override
  public ServerResponse<User> login(String username, String password) {

    int resultCount = userMapper.checkUsername(username);
    if (resultCount == 0) {
      return ServerResponse.createByErrorMessage("用户名不存在");
    }

    String md5password = MD5Util.MD5EncodeUtf8(password);
    User user = userMapper.selectLogin(username, md5password);
    if (user == null) {
      return ServerResponse.createByErrorMessage("用户密码错误");
    }

    user.setPassword(StringUtils.EMPTY);
    return ServerResponse.createBySuccess("登录成功", user);
  }

  public ServerResponse<String> register(User user) {
    ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
    if(!validResponse.isSuccess()) return validResponse;

    validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
    if(!validResponse.isSuccess())return validResponse;

    user.setRole(Const.Role.ROLE_CUSTOMER);

    // MD5 加密
    user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

    int resultCount = userMapper.insert(user);
    if (resultCount == 0) {
      return ServerResponse.createByErrorMessage("注册失败");
    }

    return ServerResponse.createBySuccessMessage("注册成功");
  }

  public ServerResponse<String> checkValid(String str, String type) {
    if(StringUtils.isNoneBlank(type) == false) {
      return ServerResponse.createByErrorMessage("参数不存在");
    }

    if (Const.USERNAME.equals(type)) {
      int resultCount = userMapper.checkUsername(str);
      if (resultCount > 0) {
        return ServerResponse.createByErrorMessage("用户名已存在");
      }
    }

    if (Const.EMAIL.equals(type)) {
      int resultCount = userMapper.checkEmail(str);
      if (resultCount > 0) {
        return ServerResponse.createByErrorMessage("email 已存在");
      }
    }

    return ServerResponse.createBySuccessMessage("校验成功");
  }

  public ServerResponse selectQuestion(String username) {
    ServerResponse validRes = checkValid(username, Const.USERNAME);

    if (validRes.isSuccess()) {
      return ServerResponse.createByErrorMessage("用户不存在");
    }

    String question = userMapper.selectQuestionByUsername(username);
    if (StringUtils.isNoneBlank(question)) {
      return ServerResponse.createBySuccess(question);
    }
    return ServerResponse.createByErrorMessage("密码问题是空的");
  }

  public ServerResponse<String> checkAnswer(String username, String question, String answer) {
    int resCount = userMapper.checkAnswer(username, question, answer);

    if (resCount > 0) {
      String token = UUID.randomUUID().toString();
      // 将 token 放入本地缓存
      TokenCache.setKey("token_" + username, token);
      return ServerResponse.createBySuccess(token);
    }

    return ServerResponse.createByErrorMessage("问题答案错误");
  }

  public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
    if (StringUtils.isBlank(forgetToken)) {
      return ServerResponse.createByErrorMessage("token 为空");
    }

    ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
    if (validResponse.isSuccess()) {
      return ServerResponse.createByErrorMessage("用户不存在");
    }

    String token = TokenCache.getKey("token_" + username);
    if (StringUtils.equals(forgetToken, token)) {
      String MD5Password = MD5Util.MD5EncodeUtf8(passwordNew);
      int rowCount = userMapper.updatePasswordByUsername(username, MD5Password);
      if (rowCount > 0) {
        return ServerResponse.createBySuccessMessage("修改密码成功");
      }
    } else {
      return ServerResponse.createBySuccessMessage("token 错误，请重新获取修改密码的 token");
    }

    return ServerResponse.createByErrorMessage("修改密码失败");
  }

  public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
    // 防止横向越权，要校验这个用户的旧密码，一定要指定是这个用户，
    // 因为我们会查询一个 count(1)，如果不指定 id，那么结果就是 true
    int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());

    if (resultCount == 0) {
      return ServerResponse.createByErrorMessage("用户名和旧密码不匹配");
    }
    String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
    resultCount = userMapper.updatePasswordByUsername(user.getUsername(), md5Password);

    if (resultCount > 0) {
      return ServerResponse.createBySuccessMessage("修改密码成功");
    }

    return ServerResponse.createByErrorMessage("修改密码失败");
  }

  public ServerResponse<User> updateUserInfo(User user) {
    // username 不能被更新
    // 新的 email 不能被其他用户使用
    int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
    if (resultCount > 0) {
      return ServerResponse.createByErrorMessage("email 已被使用，请更换 email 后再更新");
    }

    int updateCount = userMapper.updateByPrimaryKeySelective(user);
    if (updateCount >= 0) {
      return ServerResponse.createBySuccess("更新用户信息成功", user);
    }

    return ServerResponse.createByErrorMessage("更新用户信息失败");
  }

  public ServerResponse<User> getUserInfo(Integer userId) {
    User user = userMapper.selectByPrimaryKey(userId);

    if (user == null) {
      return ServerResponse.createByErrorMessage("找不到当前用户");
    }

    user.setPassword(null);
    return ServerResponse.createBySuccess(user);
  }

}





































