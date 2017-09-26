package com.attendance.controller;

import java.io.Serializable;
import java.util.Iterator;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.entity.User;
import com.attendance.exception.AttendanceException;
import com.attendance.repository.LoginRepository;

/**
 * @author harsha.patil
 *
 */

@RestController
@RequestMapping(value = "/login")
public class LoginController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

	@Resource
	LoginRepository loginRepository;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<User> authenticateLogin(@RequestBody User user) throws AttendanceException {
		logger.debug("************************************************************************");
		logger.debug("Entry : authenticateLogin::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");
		try {
			//Iterable<User> users = loginRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());
			User users = loginRepository.findByUsernameAndPassword(user.getUsername(),user.getPassword());
			if(users!=null)
			{
				return new ResponseEntity<User>(users, HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
			}
			//Iterable<User> users = loginRepository.findAll();
			/*for (User userr : users) {
				logger.info("UserName "+userr.getUsername()+" Password "+userr.getPassword());
				boolean un = userr.getUsername().equalsIgnoreCase(user.getUsername());
				boolean pwd = userr.getPassword().equalsIgnoreCase(user.getPassword());
				if (un && pwd) {
					return new ResponseEntity<User>(userr, HttpStatus.OK);
				} else {
					return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
				}
			}
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);*/
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof AttendanceException) {
				throw (AttendanceException) e;
			}
		} finally {
			logger.debug("************************************************************************");
			logger.debug("Exit : authenticateLogin::" + "::" + System.currentTimeMillis());
			logger.debug("************************************************************************");
		}
		throw new AttendanceException("Unexpected Error While executing authenticateLogin",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
