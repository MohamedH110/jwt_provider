package com.soa.demo.users.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.soa.demo.entites.User;
import com.soa.demo.entites.Role;
import com.soa.demo.users.repos.RoleRepository;
import com.soa.demo.users.repos.UserRepository;
import com.soa.demo.users.service.exceptions.EmailAlreadyExistsException;
import com.soa.demo.users.service.exceptions.ExpiredTokenException;
import com.soa.demo.users.service.exceptions.InvalidTokenException;
import com.soa.demo.users.service.resgister.RegistrationRequest;
import com.soa.demo.users.service.resgister.VerificationToken;
import com.soa.demo.users.service.resgister.VerificationTokenRepository;
import com.soa.demo.users.util.EmailSender;



@Transactional
@Service
public class UserServiceImpl  implements UserService{

	@Autowired
	UserRepository userRep;
	
	@Autowired
	RoleRepository roleRep;
	
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@Autowired
	EmailSender emailSender;
	
	@Autowired
	VerificationTokenRepository verificationTokenRepo;
	
	
	@Override
	public User saveUser(User user) {
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		return userRep.save(user);
	}

	@Override
	public User addRoleToUser(String username, String rolename) {
		User usr = userRep.findByUsername(username);
		Role r = roleRep.findByRole(rolename);
		
		usr.getRoles().add(r);
		return usr;
	}

	
	@Override
	public Role addRole(Role role) {
		return roleRep.save(role);
	}

	@Override
	public User findUserByUsername(String username) {	
		return userRep.findByUsername(username);
	}
	@Override
	public List<User> findAllUsers() {
		return userRep.findAll();
	}

	@Override
	public User registerUser(RegistrationRequest request) {

		Optional<User>  optionalUser = userRep.findByEmail(request.getEmail());
		if(optionalUser.isPresent())
			{throw new EmailAlreadyExistsException("Email déjà existant!");}
		else {
		User newUser = new User();
		newUser.setUsername(request.getUsername());
		newUser.setEmail(request.getEmail());
		
		newUser.setPassword( bCryptPasswordEncoder.encode( request.getPassword() )  );
		newUser.setEnabled(false);
		
		userRep.save(newUser);
		
		Role r = roleRep.findByRole("USER");
		List<Role> roles = new ArrayList<>();
		roles.add(r);
		newUser.setRoles(roles);
		
		//génére le code secret
		 String code = this.generateCode();

		 VerificationToken token = new VerificationToken(code, newUser);
		 verificationTokenRepo.save(token);
		 
		 //envoyer le code par email à l'utilisateur
		  sendEmailUser(newUser,token.getToken());
		

		return userRep.save(newUser);
	}


	}
	@Override
	public void sendEmailUser(User u, String code) {
		 String emailBody ="Bonjour "+ "<h1>"+u.getUsername() +"</h1>" +
		 " Votre code de validation est "+"<h1>"+code+"</h1>";
		 emailSender.sendEmail(u.getEmail(), emailBody);
		}
	
	private String generateCode() {
		 Random random = new Random();
		 Integer code = 100000 + random.nextInt(900000);

		 return code.toString();

	}
	
	@Override
	public User validateToken(String code) {
		VerificationToken token = verificationTokenRepo.findByToken(code);
		
		if(token == null){
			throw new InvalidTokenException("Invalid Token !!!!!!!");
		}

		User user = token.getUser();
		
		Calendar calendar = Calendar.getInstance();
		
		if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
			verificationTokenRepo.delete(token);
			throw new ExpiredTokenException("expired Token");
		}
		
		user.setEnabled(true);
		userRep.save(user);
		return user;
	}

}