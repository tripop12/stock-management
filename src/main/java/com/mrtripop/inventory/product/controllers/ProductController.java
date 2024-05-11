package com.mrtripop.inventory.product.controllers;

import com.mrtripop.inventory.constant.BaseStatusCode;
import com.mrtripop.inventory.exception.GlobalThrowable;
import com.mrtripop.inventory.model.ResponseBody;
import com.mrtripop.inventory.product.constant.ErrorCode;
import com.mrtripop.inventory.product.constant.SuccessCode;
import com.mrtripop.inventory.product.interfaces.ProductService;
import com.mrtripop.inventory.product.models.ProductDTO;
import com.mrtripop.inventory.product.services.ProductServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/inventory/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductServiceImpl productService) {
    this.productService = productService;
  }

  @GetMapping
  public ResponseEntity<Object> getProducts(
      @RequestParam(name = "page", defaultValue = "1", required = false)
          @Min(value = 1, message = "Page query param must not less than one")
          @NotNull(message = "Page query param must not be null")
          Integer page,
      @RequestParam(name = "size", defaultValue = "200", required = false)
          @Min(value = 1, message = "Size query param must not less than one")
          @NotNull(message = "Size query param must not be null")
          Integer size,
      @RequestParam(name = "order_by", defaultValue = "ASC", required = false)
          @NotNull(message = "Order by query param must not be null")
          Sort.Direction orderBy)
      throws GlobalThrowable {
    try {
      List<ProductDTO> products = this.productService.getProducts(page, size, orderBy);
      BaseStatusCode successCode = SuccessCode.PRO2001_GET_ALL_PRODUCTS_IS_SUCCESS;
      return ResponseBody.builder()
          .code(successCode.getCode())
          .message(successCode.getMessage())
          .data(products)
          .build()
          .buildResponseEntity(HttpStatus.OK);
    } catch (Exception e) {
      log.error("Cannot get all products: {}", e.getMessage());
      throw new GlobalThrowable(
          ErrorCode.PRO1001_CANNOT_GET_ALL_PRODUCTS, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{product_id}")
  public ResponseEntity<Object> getProductById(
      @PathVariable(name = "product_id")
          @Min(value = 1, message = "Product ID must not less than one")
          @NotNull(message = "Product ID must not be null")
          Long productId)
      throws GlobalThrowable {
    try {
      ProductDTO product = this.productService.getProductById(productId);
      BaseStatusCode successCode = SuccessCode.PRO2002_GET_PRODUCTS_BY_ID_IS_SUCCESS;
      return ResponseBody.builder()
          .code(successCode.getCode())
          .message(successCode.getMessage())
          .data(product)
          .build()
          .buildResponseEntity(HttpStatus.OK);
    } catch (Exception e) {
      log.error("Cannot get product by ID: {}", e.getMessage());
      throw new GlobalThrowable(
          ErrorCode.PRO1003_CANNOT_GET_PRODUCT_BY_ID, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping
  public ResponseEntity<Object> createNewProduct(@RequestBody @Valid ProductDTO product)
      throws GlobalThrowable {
    try {
      ProductDTO createdProduct = this.productService.createProduct(product);
      BaseStatusCode successCode = SuccessCode.PRO2003_CREATE_NEW_PRODUCT_IS_SUCCESS;
      return ResponseBody.builder()
          .code(successCode.getCode())
          .message(successCode.getMessage())
          .data(createdProduct)
          .build()
          .buildResponseEntity(HttpStatus.CREATED);
    } catch (Exception e) {
      log.error("Cannot create a new product: {}", e.getMessage());
      throw new GlobalThrowable(
          ErrorCode.PRO1002_CANNOT_CREATE_NEW_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/{product_id}")
  public ResponseEntity<Object> updateProductById(
      @PathVariable(name = "product_id")
          @Min(value = 1, message = "Product ID must not less than one")
          @NotNull(message = "Product ID must not be null")
          Long productId,
      @RequestBody @Valid ProductDTO product)
      throws GlobalThrowable {
    try {
      ProductDTO updatedProduct = productService.updateProduct(productId, product);
      BaseStatusCode successCode = SuccessCode.PRO2004_UPDATE_PRODUCT_IS_SUCCESS;
      return ResponseBody.builder()
          .code(successCode.getCode())
          .message(successCode.getMessage())
          .data(updatedProduct)
          .build()
          .buildResponseEntity(HttpStatus.OK);
    } catch (Exception e) {
      log.error("Cannot update the product by ID: {}", e.getMessage());
      throw new GlobalThrowable(
          ErrorCode.PRO1004_CANNOT_UPDATE_EXISTING_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/{product_id}")
  public ResponseEntity<Object> deleteProductById(
      @PathVariable(name = "product_id")
          @Min(value = 1, message = "Product ID must not less than one")
          @NotNull(message = "Product ID must not be null")
          Long productId)
      throws GlobalThrowable {
    try {
      productService.deleteProduct(productId);
      BaseStatusCode successCode = SuccessCode.PRO2005_DELETE_PRODUCT_IS_SUCCESS;
      return ResponseBody.builder()
          .code(successCode.getCode())
          .message(successCode.getMessage())
          .build()
          .buildResponseEntity(HttpStatus.OK);
    } catch (Exception e) {
      log.error("Cannot delete the product by ID: {}", e.getMessage());
      throw new GlobalThrowable(
          ErrorCode.PRO1005_CANNOT_DELETE_EXISTING_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
