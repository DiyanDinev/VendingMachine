package com.vm.task.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vm.task.domain.Product;
import com.vm.task.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	// Add new product
	public Product addNewProduct(String name, int price, int qty) {
		Product p = new Product();
		p.setName(name);
		p.setPrice(price);
		p.setQty(qty);
		return productRepository.save(p);
	}
	
	//Update existing product
	public Product updateProduct(Long id, String name, int price, int qty) {
		Optional<Product> product = productRepository.findById(id);
		Product p = product.get();
		p.setName(name);
		p.setPrice(price);
		p.setQty(qty);
		return productRepository.save(p);
	}
	
	//Delete existing Product
	public boolean deleteProduct(Long id) {
		Optional<Product> product = productRepository.findById(id);
		if(product.isEmpty()) {
			return false;
		} else {
			 productRepository.delete(product.get());
			 return true;
		}
	}
	
	//Find specific product
	public Optional<Product> findProductById(Long id) {
		return productRepository.findById(id);
	}
	
	//select all products
	public Iterable<Product> findAllProducts() {
		return productRepository.findAll();
	}

}
