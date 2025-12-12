package com.common.auth.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String msg)
    {
        super(msg);
    }

    ResourceNotFoundException()
    {
        super("Resource not found!");
    }
}
