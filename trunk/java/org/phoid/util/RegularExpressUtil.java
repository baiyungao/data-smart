package org.phoid.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressUtil {
	
	/** isEmailValid: Validate email address using Java reg ex.
	* This method checks if the input string is a valid email address.
	* @param email String. Email address to validate
	* @return boolean: true if email address is valid, false otherwise.
	*/

	public static boolean isEmailValid(String email){
	boolean isValid = false;

	/*
	Email format: A valid email address will have following format:
	        [\\w\\.-]+: Begins with word characters, (may include periods and hypens).
		@: It must have a '@' symbol after initial characters.
		([\\w\\-]+\\.)+: '@' must follow by more alphanumeric characters (may include hypens.).
	This part must also have a "." to separate domain and subdomain names.
		[A-Z]{2,4}$ : Must end with two to four alaphabets.
	(This will allow domain names with 2, 3 and 4 characters e.g pa, com, net, wxyz)

	Examples: Following email addresses will pass validation
	abc@xyz.net; ab.c@tx.gov
	*/

	//Initialize reg ex for email.
	//String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	                    //"^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)";

	CharSequence inputStr = email;
	//Make the comparison case-insensitive.
	Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
	Matcher matcher = pattern.matcher(inputStr);
	if(matcher.matches()){
	isValid = true;
	}
	return isValid;
	}
	
	
	/** isPhoneNumberValid: Validate phone number using Java reg ex.
	* This method checks if the input string is a valid phone number.
	* @param email String. Phone number to validate
	* @return boolean: true if phone number is valid, false otherwise.
	*/
	public static boolean isPhoneNumberValid(String phoneNumber){
	boolean isValid = false;
	/* Phone Number formats: (nnn)nnn-nnnn; nnnnnnnnnn; nnn-nnn-nnnn
		^\\(? : May start with an option "(" .
		(\\d{3}): Followed by 3 digits.
		\\)? : May have an optional ")"
		[- ]? : May have an optional "-" after the first 3 digits or after optional ) character.
		(\\d{3}) : Followed by 3 digits.
		 [- ]? : May have another optional "-" after numeric digits.
		 (\\d{4})$ : ends with four digits.

	         Examples: Matches following <a href="http://mylife.com">phone numbers</a>:
	         (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890

	*/
	//Initialize reg ex for phone number. 
	String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
	CharSequence inputStr = phoneNumber;
	Pattern pattern = Pattern.compile(expression);
	Matcher matcher = pattern.matcher(inputStr);
	if(matcher.matches()){
	isValid = true;
	}
	return isValid;
	}
	

}
