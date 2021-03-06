package com.example.fireadmin.services;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.fireadmin.models.FirebaseUser;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.auth.UserRecord.UpdateRequest;

@Service
public class FirebaseUserService {
	
//	public void setClaim(FirebaseUser u) throws FirebaseAuthException {
//			 
//		FirebaseAuth.getInstance().setCustomUserClaims(u.getUid(), u.getCustomClaims());
//		
//	}
//	
	public void setClaim(UserRecord u) throws FirebaseAuthException {
		 
		FirebaseAuth.getInstance().setCustomUserClaims(u.getUid(), u.getCustomClaims());
		
	}
	
	
	
	public UserRecord updateDisplayName(UserRecord u) throws FirebaseAuthException{
	   	
    	UpdateRequest ur = new UpdateRequest(u.getUid());
    	UserRecord user = null;

		user = FirebaseAuth.getInstance().updateUser(ur.setDisplayName(u.getDisplayName()));
		return user;
    }
	
//	public UserRecord updateDisplayName(FirebaseUser u) throws FirebaseAuthException{
//		   	
//    	UpdateRequest ur = new UpdateRequest(u.getUid());
//    	UserRecord user = null;
//
//		user = FirebaseAuth.getInstance().updateUser(ur.setDisplayName(u.getDisplayName()));
//		return user;
//    }
//	
	
	public ArrayList<ExportedUserRecord> getAllUsers(Map<String, String> headers) throws FirebaseAuthException{
    	
    	ListUsersPage page;
    	ArrayList<ExportedUserRecord> userList = new ArrayList<ExportedUserRecord>();
    	
		page = FirebaseAuth.getInstance().listUsers(null);
		
    	for (ExportedUserRecord user : page.iterateAll()) {
      	  //System.out.println("User: " + user.getUid());
      	  userList.add(user);
      	}
    	
		return userList;

    }
	
	public UserRecord getUserByUID(String uid) throws FirebaseAuthException {
	   	
    	UserRecord user = null;	
    	user = FirebaseAuth.getInstance().getUser(uid);
		return user;
	}
	
	public ResponseEntity<String> deleteUser(String uid) throws URISyntaxException{
        HttpHeaders httpHeaders = new HttpHeaders();

        FirebaseAuth.getInstance().deleteUserAsync(uid);

        return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
    }
	
    public void disableUser(String uid) throws FirebaseAuthException{
    	
    	UpdateRequest ur = new UpdateRequest(uid);
    	FirebaseAuth.getInstance().updateUser(ur.setDisabled(true));
    	
    }
    
    public void enableUser(String uid) throws FirebaseAuthException{

    	UpdateRequest ur = new UpdateRequest(uid);
    	FirebaseAuth.getInstance().updateUser(ur.setDisabled(false));
    	   	
    }
    
    public void resetPassword(String email) throws FirebaseAuthException{
    	
    	String link = FirebaseAuth.getInstance().generatePasswordResetLink(email);
    	
    	System.out.println(link);

    }
    
    public void createUser(UserRecord u) throws FirebaseAuthException{
    	
    	String defaultPass = System.getenv("default-pass");
    	
    	CreateRequest cr = new CreateRequest();
    	
    	cr.setDisplayName(u.getDisplayName());
    	cr.setEmail(u.getEmail());
    	cr.setPassword(defaultPass);

		UserRecord user = FirebaseAuth.getInstance().createUser(cr);
		FirebaseAuth.getInstance().setCustomUserClaims(user.getUid(), u.getCustomClaims());

    }
    
    public List<String> getUserLogs(String uid) {
    	ArrayList<String> userLogs = new ArrayList<String>();
    	
        try {
            FileInputStream myObj = new FileInputStream("./logs/spring-boot-logger.log");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              
              if (data.contains(uid)) {
            	  userLogs.add(data);
              }
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
        
        return userLogs.subList(userLogs.size() - 3, userLogs.size());
    	
    }

}
