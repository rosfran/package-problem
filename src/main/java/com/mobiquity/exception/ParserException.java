package com.mobiquity.exception;

/**
 * Exception thrown during file and String parsing
 */
public class ParserException extends Exception {

  public ParserException(String message, Exception e) {
    super(message, e);
  }

  public ParserException(String message) {
    super(message);
  }
}
