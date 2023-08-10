package com.ss.product.service;

import java.util.ArrayList;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import com.ss.product.web.Product;

@Path("/product")
public class ProductRest {
	
	@GET
    //@Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHelloMessage() {
		List<Product> products = new ArrayList<>();
		products.add(new Product(1000l, "f230fh0g3", "Bamboo Watch", "Product Description", "bamboo-watch.jpg", 65,
				"Accessories", 24, 5));
		products.add(new Product(1001l, "nvklal433", "Black Watch", "Product Description", "black-watch.jpg", 72,
				"Accessories", 61, 4));
		products.add(new Product(1002l, "zz21cz3c1", "Blue Band", "Product Description", "blue-band.jpg", 79, "Fitness",
				2, 3));
		products.add(new Product(1003l, "244wgerg2", "Blue T-Shirt", "Product Description", "blue-t-shirt.jpg", 29,
				"Clothing", 25, 5));
		products.add(new Product(1004l, "h456wer53", "Bracelet", "Product Description", "bracelet.jpg", 15,
				"Accessories", 73, 4));
		products.add(new Product(1005l, "av2231fwg", "Brown Purse", "Product Description", "brown-purse.jpg", 120,
				"Accessories", 0, 4));
    	 return Response
         .status(Response.Status.OK)
         .entity(products)
         .type(MediaType.APPLICATION_JSON)
         .build();

    }

}
