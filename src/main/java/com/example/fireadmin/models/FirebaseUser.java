package com.example.fireadmin.models;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FirebaseUser {

	private String uid;
	private String displayName;
	private String email;
	private String phoneNumber;
	private String photoUrl;
	private Map<String, Object> customClaims;
	
}
