package com.foc.db;

@SuppressWarnings("serial")
public class SequenceDoesNotExistException extends Exception {
	public SequenceDoesNotExistException(){
		super();
	}
	
	public SequenceDoesNotExistException(String message){
		super(message);
	}
}
