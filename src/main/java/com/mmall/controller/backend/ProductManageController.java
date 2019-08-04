package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

  @Autowired
  IUserService iUserService;

  @Autowired
  IProductService iProductService;

  @RequestMapping(value = "/save", method = RequestMethod.POST)
  public ServerResponse productSave(HttpSession session, Product product) {
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
    }

    if (!iUserService.checkAdminRole(user).isSuccess()) {
      return ServerResponse.createByErrorMessage("用户无权限");
    }
    // 保存产品
    return iProductService.saveOrUpdateProduct(product);
  }

  @RequestMapping(value = "/setSaleStatus", method = RequestMethod.POST)
  public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
    }

    if (!iUserService.checkAdminRole(user).isSuccess()) {
      return ServerResponse.createByErrorMessage("用户无权限");
    }
    return iProductService.setSaleStatus(productId, status);
  }

  @RequestMapping(value = "/detail")
  public ServerResponse getDetail(HttpSession session, Integer productId) {
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
    }

    if (!iUserService.checkAdminRole(user).isSuccess()) {
      return ServerResponse.createByErrorMessage("用户无权限");
    }
    return iProductService.manageProductDetail(productId);
  }
}

















