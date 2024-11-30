package com.movie.exceptions;

public class FileExistsException extends  RuntimeException{
    public FileExistsException(String msg){
        super(msg);
    }
}
