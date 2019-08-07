package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  IProductService iProductService;

  @RequestMapping("/detail")
  public ServerResponse<ProductDetailVo> getProductDetail(Integer productId) {
    return iProductService.getProductDetail(productId);
  }

  @RequestMapping("/list")
  public ServerResponse<PageInfo> getProductList(
      @RequestParam(value = "keyword", required = false) String keyword,
      @RequestParam(value = "categoryId", required = false) Integer categoryId,
      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
      @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
    return iProductService.getProductListByOpts(keyword, categoryId, pageNum, pageSize, orderBy);
  }
}
