package com.attendance.repository;


import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.attendance.entity.User;

/**
 * @author harsha.patil
 *
 */

@Transactional
@Repository
public interface LoginRepository extends CrudRepository<User, Integer>,Serializable
{
	User findByUsername(String username);
	User findByUsernameAndPassword(String username, String password);
}
