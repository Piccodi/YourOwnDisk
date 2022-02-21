package com.piccodi.yodisk.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class ErrorResponse {

    public static String messageForException(Exception e){
        if(e instanceof MalformedURLException) return "Broken deep data access";
        else if(e instanceof FileNotFoundException) return "File does not exist";
        else if(e instanceof IOException) return "Failed to save file";
        else if(e instanceof InvalidLinkException) return "Invalid link";
        else {return "Unexpected error";}
    }

}
