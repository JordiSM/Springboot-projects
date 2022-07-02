package com.jordism.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jordism.app.entity.User;
import com.jordism.app.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	//Create a new user
	@PostMapping
	public ResponseEntity<?> create (@RequestBody User user){
		return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);		
	}
	
	//Read an user
	@GetMapping("/{id}")
	public ResponseEntity<?> read(@PathVariable(value = "id") Long userId) {
		Optional<User> oUser = userService.findById(userId);
		
		if(!oUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(oUser, HttpStatus.OK);
	}
	
	//Update an user
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@RequestBody User userDetails, @PathVariable(value ="id") Long userId){
		Optional<User> oUser = userService.findById(userId);
		
		if(!oUser.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		User _user = oUser.get();
		
		_user.setName(userDetails.getName());
		_user.setEmail(userDetails.getEmail());
		_user.setSurname(userDetails.getSurname());
		_user.setEnabled(userDetails.getEnabled());

		return new ResponseEntity<>(userService.save(_user), HttpStatus.OK);
	}
	
	//Delete an user
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable(value="id") Long userId){
		if(!userService.findById(userId).isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		userService.deleteById(userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	//Read all Users
	@GetMapping
	public ResponseEntity<List<User>> readAll(){
		try {
			List<User> users = new ArrayList<User>();
			
			userService.findAll().forEach(users::add);
			
			if(users.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
