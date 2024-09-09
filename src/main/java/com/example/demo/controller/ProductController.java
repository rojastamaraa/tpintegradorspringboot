package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.Product;
import com.example.demo.models.ProductDto;
import com.example.demo.service.ProductRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products") 
public class ProductController {
	@Autowired
	private ProductRepository repo;
	
	@GetMapping({"", "/"})
	public String showProducts(Model model) {
		List<Product>products =repo.findAll();
		model.addAttribute("products", products);
		return "products/index";
	}
	
	@GetMapping("/create")
	public String createProduct(Model model) {
		ProductDto productDto = new ProductDto();
		model.addAttribute("productDto", productDto);
		return "products/CreateProduct";
	}
	
	@PostMapping("/create")
	public String createProduct(
			@Valid @ModelAttribute ProductDto productDto,
			BindingResult result
			) {
		if (result.hasErrors()) {
			return "products/CreateProduct";
		}
		
		Product product = new Product();
		product.setName(productDto.getName());
		product.setPrice(productDto.getPrice());
		repo.save(product);
		return "redirect:/products";
	}
	
	@GetMapping("/edit")
	public String editProduct(Model model, @RequestParam int id) {
		try {
			Product product = repo.findById(id).get();
			model.addAttribute("product", product);
			
			ProductDto productDto = new ProductDto();
			productDto.setName(product.getName());
			productDto.setPrice(product.getPrice());
			
			model.addAttribute("productDto", productDto);
			}
		catch(Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
			return "redirect:/products";
		}
		
		return "products/CreateProduct";
	}
}
