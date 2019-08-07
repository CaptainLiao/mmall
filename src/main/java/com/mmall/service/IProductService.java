package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {
  ServerResponse saveOrUpdateProduct(Product product);
  ServerResponse<String> setSaleStatus(Integer productId, Integer status);
  ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
  ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);
  ServerResponse<PageInfo> searchProductList(String productName,
                                             Integer productId,
                                             Integer pageNum,
                                             Integer pageSize);

  ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
}
