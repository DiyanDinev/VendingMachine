package com.vm.task.controller;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vm.task.domain.Product;
import com.vm.task.domain.VendingMachine;
import com.vm.task.service.ProductService;
import com.vm.task.service.VendingMachineService;

@RestController
public class IndexController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private VendingMachineService vendingMachineService;
	
	/**
	 * Add new Product - requires name, price and quantity of items - H2 generates the id .
	 * 
	 * Max quantity allowed is 10, so if it is more just return null/do not create object.
	 * If we had UI we would return initial page with error msg like "Max possible qty is 10" 
	 * else reload page with like a vending machine list of available items with the new one in it.
	 * 
	 * 
	 * Used null here and in other methods kind of as a error msg, if we had some UI(return string/page) we could return a page with model error to display or object to use and display
	 * 
	 **/
	@RequestMapping("/AddProduct/{name}/{price}/{quantity}")
	Product addNewProduct(@PathVariable ("name") String name, @PathVariable ("price") int price, @PathVariable ("quantity") int qty) {
		if(qty > 10 || qty < 1) { 
			return 	null;
		} else return 	productService.addNewProduct(name, price, qty);
	}
	
	/**
	 * Update product updates all the values of a given product as to not spread it much - as we 
	 * could have had updateProduct(Name/Price/Qty) as separate requests.
	 * Max quantity allowed is 10, so if it is more just return null/do not create object.
	 * If we had UI we would return initial page with error msg like "Max possible qty is 10" 
	 * else reload page with like a vending machine list with the updated items.
	 * 
	 **/
	@RequestMapping("/UpdateProduct/{id}/{name}/{price}/{quantity}")
	Product updateProduct(@PathVariable ("id") Long id, @PathVariable ("name") String name, @PathVariable ("price") int price, @PathVariable ("quantity") int qty) {
		if(qty > 10 || qty < 1) { 
			return 	null;
		} else return 	productService.updateProduct(id, name, price, qty);
	}
	
	/**
	 * Delete Product using its Id 
	 * 
	 **/
	@RequestMapping("/DeleteProduct/{id}")
	boolean deleteProduct(@PathVariable ("id") Long id) {
		return 	productService.deleteProduct(id);
	}
	
	/**
	 * View Product using its Id 
	 * 
	 **/
	@RequestMapping("/ViewProduct/{id}")
	Product viewProduct(@PathVariable ("id") Long id) {
		return 	productService.findProductById(id).orElseThrow(EntityNotFoundException::new);
	}
	
	/**
	 * View a list of All Product 
	 * 
	 **/
	@RequestMapping("/ViewAllProducts")
	Iterable<Product> viewAllProducts() {
		return 	productService.findAllProducts();
	}
	
	/**
	 * Insert Coin - with the amount of the coin.
	 * Generates a new Vending Machine record or updates existing one
	 * 
	 * If we had UI - it would display amount available for that record
	 * 
	 * Matches Vending requirements of only 1 insert method (the insertMoreCoin method can be used if needed)
	 * 
	 **/
	@RequestMapping("/InsertCoin/{coin}")
	VendingMachine insertCoin(@PathVariable ("coin") int coin) {
		if(coin < 1) {
			return null;
		} else
		return vendingMachineService.insertInitialCoin(coin);
	}
	
//	/**
//	 * DEPRICATED - function moved to insertInitialCoin in VM service
//	 * Insert More Coins - with Id of the Vending Machine record we want to add the coins to and with the amount of the coin.
//	 * Updates a Vending Machine Record
//	 * 
//	 * Every execute of this, if we had UI would increase the amount of coins we had and display the new value.
//	 * 
//	 **/
//	@RequestMapping("/InsertMoreCoins/{id}/{coin}")
//	VendingMachine insertMoreCoin(Model model, @PathVariable ("id") Long id, @PathVariable ("coin") int coin) {
//		return vendingMachineService.updateAmountInserted(id, coin);
//	}
	
	/**
	 * Reset Vending machine by using id. Reset is literary delete the Vending Machine record as to make it simple 
	 * We are using its Id as to identify the H2 generated record and also because we can technically have more than 1 Vending Machine records at the moment
	 * 
	 * If we had UI it would based on the result from the resetMachine method display if money was returned or not
	 * 
	 **/
	@RequestMapping("/ResetVM/{id}")
	boolean resetVM(@PathVariable ("id") Long id) {
		return vendingMachineService.resetMachine(id);
	}
	
	@RequestMapping("/ViewAllVMs")
	Iterable<VendingMachine> viewAllVMs() {
		return 	vendingMachineService.findAllVMs();
	}
	
	/**
	 * Buy Product - using the VM machine ID that we want to take money from and the ID of the item we want to buy
	 * 
	 * If we have enough money then buy the product, else show customer they don't have enough money.
	 * 
	 * After item is sold remove 1 from Quantity - if Qty is 0 after that - delete product record, else just update Quantity.
	 * Also reset Vending Machine record as the VM does not return change.
	 * 
	 **/
	@RequestMapping("/BuyProduct/{id}/{vmId}")
	String buyProduct(@PathVariable ("id") Long id, @PathVariable ("vmId") Long vmId) {
		Optional<Product> p = productService.findProductById(id);
		Optional<VendingMachine> vm = vendingMachineService.findProductById(vmId);
		if(p.isPresent() && vm.isPresent()) {
			if (vm.get().getCoinsInserted() >= p.get().getPrice()) {
				vendingMachineService.resetMachine(vmId);
				if(p.get().getQty() -1 == 0) {
					productService.deleteProduct(id);
				} else {
					productService.updateProduct(id, p.get().getName(), p.get().getPrice(), p.get().getQty()-1);
				}
				return "You bought " + p.get().getName();
			} else {
			 	return "Not enough money";
			}
		} else {
			if(p.isEmpty()) {
				return "Item Unavailable";
			} else if(vm.isEmpty()) {
				return "Not enough money";
			} else {
				return "Vending Machine is broken";
			}
		}
	}
	
	
}
