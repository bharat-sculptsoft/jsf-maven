package com.ss.product.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.ss.exception.ServiceLayerException;
import com.ss.message.Constant;
import com.ss.product.service.ProductService;
import com.ss.product.web.Product;

@ManagedBean
@SessionScoped
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
	private String sortField="code";
	private String sortOrder = "asc"; // Default sort order

	@PostConstruct
	public void init() {
		logger.info("CrudView init");
	//	this.products = this.productService.getClonedProducts(100);
		try {
			this.products=this.productService.findAll();
		} catch (ServiceLayerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if(selectedProduct!=null && selectedProduct.getCode()!=null) {
		logger.debug("selectProduct " + selectedProduct + " " + selectedProduct.getCode());
			this.selectedProduct = selectedProduct;
		}else {
			this.selectedProduct =new Product();
		}
		
	}
	public void editProduct(Product selectedProduct) {
		if(selectedProduct!=null && selectedProduct.getCode()!=null) {
		logger.debug("editProduct " + selectedProduct + " " + selectedProduct.getCode());
			this.selectedProduct = selectedProduct;
		}else {
			this.selectedProduct =new Product();
		}
		redirectToPage("editproduct.xhtml");
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

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
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
	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
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
		logger.debug("saveNewProduct "+selectedProduct.toString());
		if (this.selectedProduct.getId() == null) {
			//this.selectedProduct.setId(1233);
			try {
				productService.save(this.selectedProduct);
			} catch (ServiceLayerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.products.add(this.selectedProduct);
			this.filteredProducts.add(this.selectedProduct);
			filterProducts();
			this.selectedProduct=new Product();
			 // Add a success message
	        FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "New product added."));

	        // Redirect to the same page using PRG pattern
	       
	        redirectToPage(Constant.DataTable);
		}
	}
	public void saveOrAddProduct() {
		logger.debug("saveOrAddProduct "+selectedProduct.toString());
		//if new record then save
		if (this.selectedProduct.getId() == null) {
			this.selectedProduct.setId(1233L);
			this.products.add(this.selectedProduct);
			this.filteredProducts.add(this.selectedProduct);
			filterProducts();
			redirectToPage(Constant.DataTable);
		}else {
			filterProducts();
			redirectToPage(Constant.DataTable);
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
	 // Method to redirect back to the original page
    public void redirectToPage(String pageURL) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        try {
            // Redirect the user back to the original page
            externalContext.redirect(pageURL);
        } catch (IOException e) {
            // Handle the exception if redirect fails
            e.printStackTrace();
        }
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
		 // Apply sorting
		//sortField="code"; sortOrder="asc";
		logger.debug("filterProducts sortField"+sortField);
	    if (sortField != null) {
	        filteredProducts.sort((p1, p2) -> {
	            int result;
	            switch (sortField) {
	                case "code":
	                    result = p1.getCode().compareTo(p2.getCode());
	                    break;
	                case "name":
	                    result = p1.getName().compareTo(p2.getName());
	                    break;
	                case "price":
	                    result = ((Double)p1.getPrice()).compareTo((Double)p2.getPrice());
	                    break;
	                // Add more cases for other sortable fields as needed
	                default:
	                    result = 0; // Default to no sorting if the field is not recognized
	                    
	            }
	            return "asc".equals(sortOrder) ? result : -result; // Reverse order for descending
	        });
	    }
		int startIndex = (currentPage - 1) * rowsPerPage;
		int endIndex = Math.min(startIndex + rowsPerPage, filteredProducts.size());
		filteredProducts = new ArrayList<>(filteredProducts.subList(startIndex, endIndex));
	}
	 // Sorting logic for the data table
    public void sort(String field) {
        if (field == null || field.isEmpty()) {
            return; // Invalid field, do nothing
        }

        // Toggle sort order if the same field is clicked again
        if (field.equals(sortField)) {
            sortOrder = "asc".equals(sortOrder) ? "desc" : "asc";
        } else {
            // New field for sorting, default to ascending order
            sortOrder = "asc";
        }
        sortField = field;
        logger.info("sortOrder: "+sortOrder+"sortField: "+sortField);
        filterProducts();
    }
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
