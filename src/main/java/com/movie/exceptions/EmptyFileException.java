package com.movie.exceptions;

public class EmptyFileException extends RuntimeException {
    public EmptyFileException(String msg) {
        super(msg);
    }
}
