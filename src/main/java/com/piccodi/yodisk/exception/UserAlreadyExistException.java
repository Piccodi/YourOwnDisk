package com.piccodi.yodisk.exception;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException(String massage){super(massage);}
    //todo сделать глобальную обработку ошибок(на пост возвращать ерор паге, на гет ошибку в шаблоне писать по возможности )
}
