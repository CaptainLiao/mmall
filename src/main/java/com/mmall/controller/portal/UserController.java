package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

  @Autowired
  IUserService iUserService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  /**
   * @Param username:
   * @Param password:
   * @Param httpSession:
   * @return com.mmall.common.ServerResponse<com.mmall.pojo.User>
   */
  public ServerResponse<User> login(String username, String password, HttpSession session) {
    // service -> mybatis -> dao
    ServerResponse<User> response = iUserService.login(username, password);
    if (response.isSuccess()) {
      session.setAttribute(Const.CURRENT_USER, response.getData());
    }

    return response;
  }

  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public ServerResponse<String> logout(HttpSession session) {
    session.removeAttribute(Const.CURRENT_USER);
    return ServerResponse.createBySuccessMessage("退出登录");
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  public ServerResponse<String> register(User user) {
    return iUserService.register(user);
  }

  @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
  public ServerResponse<User> getUserInfo(HttpSession session) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorMessage("用户未登录");
    }

    return ServerResponse.createBySuccess(user);
  }

  @RequestMapping(value = "/forgetQuestion", method = RequestMethod.GET)
  public ServerResponse<String> forgetQuestion(String username) {
    return iUserService.selectQuestion(username);
  }

  @RequestMapping(value = "/forgetCheckAnswer", method = RequestMethod.POST)
  public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
    return  iUserService.checkAnswer(username, question, answer);
  }

  @RequestMapping(value = "/forgetResetPassword", method = RequestMethod.POST)
  public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
    return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
  }

  @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
  public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorMessage("用户未登录");
    }
    return iUserService.resetPassword(passwordOld, passwordNew, user);
  }

  @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
  public ServerResponse<User> updateUserInfo(HttpSession session, User user) {
    User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
    if (currentUser == null) {
      return ServerResponse.createByErrorMessage("用户未登录");
    }

    user.setId(currentUser.getId());
    user.setUsername(currentUser.getUsername());
    ServerResponse<User> response = iUserService.updateUserInfo(user);
    if (response.isSuccess()) {
      session.setAttribute(Const.CURRENT_USER, response.getData());
    }

    return response;
  }

}













