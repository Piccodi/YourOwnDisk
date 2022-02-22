package com.piccodi.yodisk.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
class GlobalDefaultExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "support";

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e){

        e.printStackTrace();
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("timestamp", new Date());
        if(e instanceof CustomResponseException){
            mav.addObject("status", ((CustomResponseException) e).getHttpStatus().value());
        }
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }
}