package com.vm.task.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vm.task.domain.VendingMachine;
import com.vm.task.repository.VendingMachineRepository;

@Service
public class VendingMachineService {
	
	@Autowired
	private VendingMachineRepository vendingMachineRepository;
	
	
	/**
	 * 
	 * @param coin
	 * @return new Vending Machine object
	 * 
	 * Basically the idea here is that we can get all VM records
	 * and use the 1st one and only use it and no other VM record.
	 * This allows us to use the same method to create a new VM record or update it if called multiple times
	 * which means we can insert as much coins as needed.
	 * 
	 */
	public VendingMachine insertInitialCoin(int coin) {
		Iterable<VendingMachine> vm = vendingMachineRepository.findAll();
		if(vm.iterator().hasNext()) {
			VendingMachine vend = vm.iterator().next();
			vend.setCoinsInserted(vend.getCoinsInserted() + coin);
			return vendingMachineRepository.save(vend);
		} else {
			VendingMachine vend = new VendingMachine();
			vend.setCoinsInserted(coin);
			return vendingMachineRepository.save(vend);
		}

	}
	
	/**
	 * 
	 * @param id, coin
	 * @return update VM object 
	 * 
	 * DEPRICATED - functionality moved to the insertInitialCoin method
	 * 
	 */
//	//Update the coins in the machine
//	public VendingMachine updateAmountInserted(Long id, int coin) {
//		Optional<VendingMachine> vm = vendingMachineRepository.findById(id);
//		VendingMachine updateVM = vm.get();
//		updateVM.setCoinsInserted(vm.get().getCoinsInserted() + coin);
//		return vendingMachineRepository.save(updateVM);
//	}
	
	
	//delete record when machine is reset 
	public boolean resetMachine(Long id) {
		Optional<VendingMachine> vm = vendingMachineRepository.findById(id);
		if(vm.isEmpty()) {
			return false;
		} else {
			vendingMachineRepository.delete(vm.get());
			return true;
		}
	}
	
	//Find specific product
	public Optional<VendingMachine> findProductById(Long id) {
		return vendingMachineRepository.findById(id);
	}
	
	public Iterable<VendingMachine> findAllVMs() {
		return vendingMachineRepository.findAll();
	}

}
