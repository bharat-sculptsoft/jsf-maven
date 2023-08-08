package com.ss.product.web;

import java.io.Serializable;

public class Product implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String code;
	private String name;
	private String description;
	private String image;
	private double price;
	private String category;
	private int quantity;
	private int rating;

	public Product() {
	}

	public Product(Long id, String code, String name, String description, String image, double price, String category,
			int quantity, int rating) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.description = description;
		this.image = image;
		this.price = price;
		this.category = category;
		this.quantity = quantity;
		this.rating = rating;
	}

	@Override
	public Product clone() {
		return new Product(getId(), getCode(), getName(), getDescription(), getImage(), getPrice(), getCategory(),
				getQuantity(), getRating());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Product other = (Product) obj;
		if (code == null) {
			return other.code == null;
		} else {
			return code.equals(other.code);
		}
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", code=" + code + ", name=" + name + ", description=" + description + ", image="
				+ image + ", price=" + price + ", category=" + category + ", quantity=" + quantity + ", rating="
				+ rating + "]";
	}

}
