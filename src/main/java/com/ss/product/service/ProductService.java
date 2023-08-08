package com.ss.product.service;


import java.util.List;

import com.ss.exception.ServiceLayerException;
import com.ss.product.web.Product;

public interface ProductService {

	public List<Product> getProducts();

	public List<Product> getProducts(int size); 

	public List<Product> getClonedProducts(int size);
	
	public List<Product> findAll() throws ServiceLayerException;
	
	//public List<Product> getAllProducts(List<ProductDetails> productDetails);
	
	public void save(Product product) throws ServiceLayerException;
}
