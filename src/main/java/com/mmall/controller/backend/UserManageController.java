package com.mmall.controller.backend;

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
@RequestMapping("/manage/user")
public class UserManageController {

  @Autowired
  IUserService iUserService;

  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ServerResponse<User> login(String username, String password, HttpSession session) {
    ServerResponse response = iUserService.login(username, password);

    if (response.isSuccess()) {
      User user = (User) response.getData();
      if (user.getRole() == Const.Role.ROLE_ADMIN) {
        session.setAttribute(Const.CURRENT_USER, user);
        return response;
      } else {
        return ServerResponse.createByErrorMessage("不是管理员");
      }
    }

    return response;
  }
}
