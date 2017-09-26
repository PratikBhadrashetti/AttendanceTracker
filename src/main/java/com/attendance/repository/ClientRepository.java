package com.attendance.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.entity.Client;

/**
 * @author harsha.patil
 *
 */

@Transactional
@Repository
public interface ClientRepository extends CrudRepository<Client, Integer>, Serializable {
	Client findById(int id);
	public Client findByName(String name);
}
