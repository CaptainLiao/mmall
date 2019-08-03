package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

  @Autowired
  ProductMapper productMapper;

  public ServerResponse saveOrUpdateProduct(Product product) {
    if (product == null)
      return ServerResponse.createByErrorMessage("新增或更新产品参数不正确");

    // 设置主图
    if (StringUtils.isNotBlank(product.getSubImages())) {
      String[] subImages = product.getSubImages().split(",");
      if (subImages.length > 0) {
        product.setMainImage(subImages[0]);
      }
    }

    if (product.getId() == null) {
      int resCount = productMapper.insert(product);
      if (resCount > 0)
        return ServerResponse.createBySuccessMessage("新增产品成功");
      return ServerResponse.createBySuccessMessage("新增产品失败");
    }

    int resCount = productMapper.updateByPrimaryKey(product);
    if (resCount > 0)
      return ServerResponse.createBySuccessMessage("更新产品成功");
    return ServerResponse.createBySuccessMessage("更新产品失败");
  }

  public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
    if (productId == null || status == null) {
      return ServerResponse.createByErrorCodeMessage(
          ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }

    Product product = new Product();
    product.setId(productId);
    product.setStatus(status);

    int resCount = productMapper.updateByPrimaryKeySelective(product);
    if (resCount > 0)
      return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
    return ServerResponse.createBySuccessMessage("修改产品销售状态失败");
  }

}


















