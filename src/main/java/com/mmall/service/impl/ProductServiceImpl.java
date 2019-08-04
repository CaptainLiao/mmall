package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private CategoryMapper categoryMapper;

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

  public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId) {
    if (productId == null) {
      return ServerResponse.createByErrorCodeMessage(
          ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
    }

    Product product = productMapper.selectByPrimaryKey(productId);

    if (product == null)
      return ServerResponse.createBySuccessMessage("产品已下架或被删除");

    ProductDetailVo productDetailVo = assembleProductDetailVo(product);
    return ServerResponse.createBySuccess(productDetailVo);
  }

  private ProductDetailVo assembleProductDetailVo(Product product) {
    ProductDetailVo productDetailVo = new ProductDetailVo();
    productDetailVo.setId(product.getId());
    productDetailVo.setSubtitle(product.getSubtitle());
    productDetailVo.setPrice(product.getPrice());
    productDetailVo.setMainImage(product.getMainImage());
    productDetailVo.setSubImages(product.getSubImages());
    productDetailVo.setCategoryId(product.getCategoryId());
    productDetailVo.setDetail(product.getDetail());
    productDetailVo.setName(product.getName());
    productDetailVo.setStatus(product.getStatus());
    productDetailVo.setStock(product.getStock());

    productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

    Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
    if (category == null) {
      // 根节点
      productDetailVo.setParentCategoryId(0);
    } else {
      productDetailVo.setParentCategoryId(category.getParentId());
    }

    productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
    productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

    return productDetailVo;
  }

}


















