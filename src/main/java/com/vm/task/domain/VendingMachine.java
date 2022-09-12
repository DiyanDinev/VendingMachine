package com.vm.task.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="VENDING_MACHINE")
public class VendingMachine {

	@Id
	@GeneratedValue
	@Column(name="id")
	public Long id;
	@Column(name="coinsInserted")
	public int coinsInserted; //going with int to make it a bit more simple - price set as 10 will be 10st and 100 will be 1lv (to not spent time with currency)
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getCoinsInserted() {
		return coinsInserted;
	}
	public void setCoinsInserted(int coinsInserted) {
		this.coinsInserted = coinsInserted;
	}	
	
}
