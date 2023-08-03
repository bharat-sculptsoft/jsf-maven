package com.ss.product.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.ss.product.service.ProductService;
import com.ss.product.web.Product;

@ManagedBean
@ViewScoped
public class CrudView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Product> products;

	private Product selectedProduct;

	private List<Product> selectedProducts;
	private List<Product> filteredProducts; // New property for filtered list
	private int currentPage = 1;
	private int rowsPerPage = 10;
	private int filteredSize = 0;
	// @Inject
	@ManagedProperty(value = "#{productService}")
	private ProductService productService;
	private static final Logger logger = LogManager.getLogger(CrudView.class);
	private String searchQuery = "";

	@PostConstruct
	public void init() {
		logger.info("CrudView init");
		this.products = this.productService.getClonedProducts(100);
		filteredProducts = new ArrayList<>(products); // Initially, both lists are the same
		filterProducts();
	}

	public List<Product> getProducts() {
		return products;
	}

	public Product getSelectedProduct() {
		logger.debug(" GEt selectProduct " + selectedProduct);
		if (selectedProduct == null)
			selectedProduct = new Product();
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public void selectProduct(Product selectedProduct) {
		logger.debug("selectProduct " + selectedProduct + " " + selectedProduct.getCode());
		this.selectedProduct = selectedProduct;
	}

	public List<Product> getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(List<Product> selectedProducts) {
		this.selectedProducts = selectedProducts;
	}
	// ... Getter and setter for products ...

	public List<Product> getFilteredProducts() {
		return filteredProducts;
	}

	public void setFilteredProducts(List<Product> filteredProducts) {
		this.filteredProducts = filteredProducts;
	}

	public void openNew() {
		this.selectedProduct = new Product();
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	public String getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(String searchQuery) {
		this.searchQuery = searchQuery;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public void saveProduct() {
		if (this.selectedProduct.getCode() == null) {
			this.selectedProduct.setCode(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9));
			this.products.add(this.selectedProduct);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Added"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Updated"));
		}

		PrimeFaces.current().executeScript("PF('manageProductDialog').hide()");
		PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
	}

	public void saveNewProduct() {
		if (this.selectedProduct.getCode() == null) {
			this.selectedProduct.setCode(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9));
			this.products.add(this.selectedProduct);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Added"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Updated"));
		}
	}

	public void deleteProduct() {
		this.products.remove(this.selectedProduct);
		this.selectedProducts.remove(this.selectedProduct);
		this.selectedProduct = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Product Removed"));
		PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
	}

	public void deleteProduct(Product selectedProduct) {
		logger.debug("deleteProduct " + selectedProduct.toString());
		this.products.remove(selectedProduct);
		this.filteredProducts.remove(selectedProduct);
		logger.debug("deleteProduct Size " + products.size() + " " + filteredProducts.size());
		filterProducts();
	}

	public String getDeleteButtonMessage() {
		if (hasSelectedProducts()) {
			int size = this.selectedProducts.size();
			return size > 1 ? size + " products selected" : "1 product selected";
		}

		return "Delete";
	}

	public boolean hasSelectedProducts() {
		return this.selectedProducts != null && !this.selectedProducts.isEmpty();
	}

	public void deleteSelectedProducts() {
		this.products.removeAll(this.selectedProducts);
		this.selectedProducts = null;
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Products Removed"));
		PrimeFaces.current().ajax().update("form:messages", "form:dt-products");
		PrimeFaces.current().executeScript("PF('dtProducts').clearFilters()");
	}

	// Method to handle the filter action
	public void filterProducts() {
		// Perform filtering based on a search query or other criteria
		// For example, you can filter products by name containing a search query
		// Here, we'll assume the search query is stored in a property called
		// "searchQuery"
		logger.info("In filterProducts");
		filteredProducts = new ArrayList<>();
		for (Product product : products) {
			// logger.debug("filterProducts search called: "+searchQuery+ "product "+
			// product.getName().toLowerCase());
			if (product.getName().toLowerCase().contains(searchQuery.toLowerCase())
					|| product.getCategory().toLowerCase().contains(searchQuery.toLowerCase())) {
				filteredProducts.add(product);
			}
		}
		logger.debug("search called size1: " + filteredProducts.size());
		filteredSize = filteredProducts.size();
		int startIndex = (currentPage - 1) * rowsPerPage;
		int endIndex = Math.min(startIndex + rowsPerPage, filteredProducts.size());
		filteredProducts = new ArrayList<>(filteredProducts.subList(startIndex, endIndex));
		// logger.debug("search called size 2: " +filteredProducts.size());
	}
	// Getters and setters...

	public void nextPage() {
		if (currentPage < getTotalPages()) {
			currentPage++;
			filterProducts();
		}
	}

	public void previousPage() {
		if (currentPage > 1) {
			currentPage--;
			filterProducts();
		}
	}

	public void goToPage(int page) {
		if (page >= 1 && page <= getTotalPages()) {
			currentPage = page;
			filterProducts();
		}
	}

	public int getTotalPages() {
		// logger.debug("getTotalPages "+searchQuery);
		if (searchQuery == null || searchQuery == "")
			return (int) Math.ceil((double) products.size() / rowsPerPage);
		else
			return (int) Math.ceil((double) filteredSize / rowsPerPage);
	}

	@Override
	public String toString() {
		return "CrudView [products=" + products + ", selectedProduct=" + selectedProduct + ", selectedProducts="
				+ selectedProducts + ", filteredProducts=" + filteredProducts + ", currentPage=" + currentPage
				+ ", rowsPerPage=" + rowsPerPage + ", filteredSize=" + filteredSize + ", productService="
				+ productService + ", searchQuery=" + searchQuery + "]";
	}

}
