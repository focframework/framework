package com.foc.db;

@SuppressWarnings("serial")
public class FocDBException extends Exception {
	public FocDBException(){
		super();
	}
	
	public FocDBException(String message){
		super(message);
	}	
}
