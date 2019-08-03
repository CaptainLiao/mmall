package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {
  @Autowired
  IUserService iUserService;

  @Autowired
  ICategoryService iCategoryService;

  @RequestMapping(value = "/addCategory", method = RequestMethod.POST)
  public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
    }

    if (iUserService.checkAdminRole(user).isSuccess()) {
      return iCategoryService.addCategory(categoryName, parentId);
    }
    return ServerResponse.createByErrorMessage("无操作权限");
  }

  @RequestMapping(value = "/updateCategoryName", method = RequestMethod.POST)
  public ServerResponse updateCategoryName(HttpSession session, String categoryName, Integer categoryId) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
    }

    if (iUserService.checkAdminRole(user).isSuccess()) {
      return iCategoryService.updateCategoryName(categoryId, categoryName);
    }
    return ServerResponse.createByErrorMessage("无操作权限");
  }

  @RequestMapping(value = "/getCategoryById", method = RequestMethod.POST)
  public ServerResponse getChildParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
    }

    if (iUserService.checkAdminRole(user).isSuccess()) {
      return iCategoryService.getChildParallelCategory(categoryId);
    }
    return ServerResponse.createByErrorMessage("无操作权限");
  }

  // 查询节点并递归它的子节点
  @RequestMapping(value = "/getAllCategoryId", method = RequestMethod.POST)
  public ServerResponse getAndRecurseCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
    User user = (User) session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
    }

    if (iUserService.checkAdminRole(user).isSuccess()) {
      return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
    return ServerResponse.createByErrorMessage("无操作权限");
  }
}











