package com.ss.product.service;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ss.exception.DaoLayerException;
import com.ss.exception.ServiceLayerException;
import com.ss.exception.ValidationFailedException;
import com.ss.message.MessageConstant;
import com.ss.message.MessageProvider;
import com.ss.product.dao.ProductDao;
import com.ss.product.entity.ProductDetails;
import com.ss.product.web.Product;
import com.ss.user.entity.User;
import com.ss.util.HibernateUtil;

import lombok.Data;

@ManagedBean(name = "productService")
@ApplicationScoped
@Data
public class ProductServiceImpl implements ProductService {

	private List<Product> products;
	private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);
	@ManagedProperty(value = "#{productDao}")
	private ProductDao productDao;
	
	public ProductDao getProductDao() {
		return productDao;
	}
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}
	
	@PostConstruct
	public void init() {
		logger.info("ProductService init");
		products = new ArrayList<>();
		products.add(new Product(1000L, "f230fh0g3", "Bamboo Watch", "Product Description", "bamboo-watch.jpg", 65,
				"Accessories", 24, 5));
//		products.add(new Product(1001, "nvklal433", "Black Watch", "Product Description", "black-watch.jpg", 72,
//				"Accessories", 61, 4));
//		
//		products.add(new Product(1013, "fldsmn31b", "Grey T-Shirt", "Product Description", "grey-t-shirt.jpg", 48,
//				"Clothing", 0, 3));
//		products.add(new Product(1014, "waas1x2as", "Headphones", "Product Description", "headphones.jpg", 175,
//				"Electronics", 8, 5));
//		products.add(new Product(1015, "vb34btbg5", "Light Green T-Shirt", "Product Description",
//				"light-green-t-shirt.jpg", 49, "Clothing", 34, 4));
//		products.add(new Product(1016, "k8l6j58jl", "Lime Band", "Product Description", "lime-band.jpg", 79, "Fitness",
//				12, 3));
//		products.add(new Product(1017, "v435nn85n", "Mini Speakers", "Product Description", "mini-speakers.jpg", 85,
//				"Clothing", 42, 4));
	}
	@Override
	public List<Product> getProducts() {
		logger.debug("ProductService getProducts " + products.size());
		return new ArrayList<>(products);
	}
	@Override
	public List<Product> getProducts(int size) {
		if (size > products.size()) {
			Random rand = new Random();

			List<Product> randomList = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				int randomIndex = rand.nextInt(products.size());
				randomList.add(products.get(randomIndex));
			}
			logger.debug("ProductService getProducts int " + randomList.size());
			return randomList;
		} else {
			return new ArrayList<>(products.subList(0, size));
		}

	}
	@Override
	public List<Product> getClonedProducts(int size) {
		List<Product> results = new ArrayList<>();
		List<Product> originals = getProducts(size);
		for (Product original : originals) {
			results.add(original.clone());
		}

		// make sure to have unique codes
		for (Product product : results) {
			logger.debug("getClonedProducts " + results.size());
			product.setCode(UUID.randomUUID().toString().replace("-", "").substring(0, 8));
		}

		return results;
	}

	@Override
	public List<Product> findAll() throws ServiceLayerException {
		// TODO Auto-generated method stub
		EntityManager entityManager = null;
		try {
			entityManager = HibernateUtil.getEntityManager();
			List<ProductDetails> productDetails=productDao.findAll(entityManager);
			return getAllProducts(productDetails);
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof ValidationFailedException || e instanceof DaoLayerException) {
				throw new ServiceLayerException(e.getMessage());
			}
			throw new ServiceLayerException(
					MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));
		} finally {
			HibernateUtil.closeEntityManager(entityManager);
		}
	}
	//@Override
	private List<Product> getAllProducts(List<ProductDetails> productDetails) {
		List<Product> products = new ArrayList<>();
		try {
			for (ProductDetails p:productDetails) {
				Product pd=new Product();
				BeanUtils.copyProperties(pd,p);
				products.add(pd);
			}
			
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return products;
	}
	public void save(Product product) throws ServiceLayerException
	{
		EntityManager entityManager = null;
		try {
			entityManager = HibernateUtil.getEntityManager();
			// begin transaction
			HibernateUtil.beginTransaction(entityManager);
			ProductDetails p=new ProductDetails();
			logger.debug("ProductServiceImpl: Save: "+product.toString());
			BeanUtils.copyProperties(p,product);
			//Need to add the code for checking already exist by code 
			productDao.save(entityManager, p);

			// commit transaction
			HibernateUtil.commitTransaction(entityManager);

		} catch (Exception e) {
			e.printStackTrace();
			// rollback transaction
			if(entityManager!=null)
				HibernateUtil.rollbackTransaction(entityManager);

			if (e instanceof ValidationFailedException || e instanceof DaoLayerException) {
				throw new ServiceLayerException(e.getMessage());
			}
			throw new ServiceLayerException(
					MessageProvider.getMessageString(MessageConstant.INTERNAL_SERVER_ERROR, null));
		} finally {
			HibernateUtil.closeEntityManager(entityManager);
		}
	}
}
