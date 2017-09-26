package com.attendance.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.entity.Client;
import com.attendance.entity.User;
import com.attendance.exception.AttendanceException;
import com.attendance.repository.ClientRepository;
import com.attendance.repository.UserRepository;

/**
 * @author harsha.patil
 *
 */

@RestController
@RequestMapping(value = "/client")
public class ClientController implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

	@Resource
	ClientRepository clientRepository;

	@Resource
	UserRepository userRepository;

	@RequestMapping(value = "/clients/{userid}", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Client>> getClients(@PathVariable("userid") int userid) throws AttendanceException {
		logger.debug("************************************************************************");
		logger.debug("Entry : getClients::" + "::" + System.currentTimeMillis());
		logger.debug("************************************************************************");
		try {
			Iterable<User> users = userRepository.findAll();
			List<Client> clientsList = new ArrayList<Client>();

			for (User user : users) {

				if (user.getId() == userid) {
					clientsList.addAll(user.getClients());
				}

			}

			Iterable<Client> clients = clientsList;
			return new ResponseEntity<Iterable<Client>>(clients, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (e instanceof AttendanceException) {
				throw (AttendanceException) e;
			}
		} finally {
			logger.debug("************************************************************************");
			logger.debug("Exit : getClients::" + "::" + System.currentTimeMillis());
			logger.debug("************************************************************************");
		}
		throw new AttendanceException("Unexpected Error While executing getClients", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<Client> createEmployee(@RequestBody Client client) {
		HttpHeaders headers = new HttpHeaders();
		if (client == null) {
			return new ResponseEntity<Client>(HttpStatus.BAD_REQUEST);
		}
		clientRepository.save(client);
		headers.add("Client Created  - ", String.valueOf(client.getId()));
		return new ResponseEntity<Client>(client, headers, HttpStatus.CREATED);
	}

}
