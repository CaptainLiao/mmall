package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

  @Autowired
  IUserService iUserService;
  @Autowired
  IProductService iProductService;
  @Autowired
  IFileService iFileService;

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

  @RequestMapping(value = "/list")
  public ServerResponse getList(HttpSession session,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
    }

    if (!iUserService.checkAdminRole(user).isSuccess()) {
      return ServerResponse.createByErrorMessage("用户无权限");
    }
    return iProductService.getProductList(pageNum, pageSize);
  }

  @RequestMapping(value = "/search")
  public ServerResponse getList(HttpSession session,
                                String productName,
                                Integer productId,
                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
    }

    if (!iUserService.checkAdminRole(user).isSuccess()) {
      return ServerResponse.createByErrorMessage("用户无权限");
    }
    return iProductService.searchProductList(productName, productId, pageNum, pageSize);
  }

  // 文件上传
  @RequestMapping(value = "/upload")
  public ServerResponse upload(HttpSession session,
                               @RequestParam(value = "upload_file") MultipartFile file,
                               HttpServletRequest request) {
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录");
    }

    if (!iUserService.checkAdminRole(user).isSuccess()) {
      return ServerResponse.createByErrorMessage("用户无权限");
    }

    String path = request.getSession().getServletContext().getRealPath("upload");
    String targetFileName = iFileService.upload(file, path);
    String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

    Map<String, String> map = Maps.newHashMap();
    map.put("uri", targetFileName);
    map.put("url", url);
    return ServerResponse.createBySuccess(map);
  }

  // 富文本中图片上传
  @RequestMapping(value = "/richTextImgUpload")
  public Map richTextImgUpload(HttpSession session,
                               @RequestParam(value = "upload_file") MultipartFile file,
                               HttpServletResponse response,
                               HttpServletRequest request) {

    Map resMap = Maps.newHashMap();
    User user = (User)session.getAttribute(Const.CURRENT_USER);
    if (user == null) {
      resMap.put("success", false);
      resMap.put("msg", "请登录管理员");
      return resMap;
    }

    if (!iUserService.checkAdminRole(user).isSuccess()) {
      resMap.put("success", false);
      resMap.put("msg", "用户无权限");
      return resMap;
    }

    // 富文本编辑器使用 simditor
    // 所以返回的响应需要符合 simditor 的要求：{"success"; true, "msg": "", "file_path": []}
    String path = request.getSession().getServletContext().getRealPath("upload");
    String targetFileName = iFileService.upload(file, path);
    if (StringUtils.isBlank(targetFileName)) {
      resMap.put("success", false);
      resMap.put("msg", "上传文件失败");
      return resMap;
    }

    response.addHeader("Access-Allow-Control-Headers", "X-File-Name");
    String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;
    resMap.put("success", true);
    resMap.put("file_path", url);
    return resMap;
  }

}

















