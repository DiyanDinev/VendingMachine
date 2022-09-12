package com.vm.task.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.vm.task.domain.VendingMachine;

@Repository
public interface VendingMachineRepository extends CrudRepository<VendingMachine, Long> {

}
