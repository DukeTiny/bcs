//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.interview.bcs.common;

import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.constant.RespCode;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResp bizExceptionHandler( HttpServletRequest request, HttpServletResponse response, Exception e ) {
        printErrorMsg( request, e );
        return BaseResp.fail( getCode( e ), getErrorMsg( e ) );
    }


    private void printErrorMsg( HttpServletRequest request, Exception e ) {
        log.error( "Request[{}] error.", request.getRequestURI(), e );
    }


    private String getErrorMsg( Exception e ) {
        if ( e instanceof MethodArgumentNotValidException ) {
            return doGetErrorMsg( ( ( MethodArgumentNotValidException ) e ).getBindingResult().getAllErrors() );
        }
        else if ( e instanceof BindException ) {
            return doGetErrorMsg( ( ( BindException ) e ).getBindingResult().getAllErrors() );
        }
        return getErrorDetail( e );
    }

    private String doGetErrorMsg( List<ObjectError> allErrors ) {
        Objects.requireNonNull( allErrors );
        StringBuilder sb = new StringBuilder();
        for ( ObjectError error : allErrors ) {
            if ( error instanceof FieldError ) {
                sb.append( "[" ).append( ( ( FieldError ) error ).getField() ).append( error.getDefaultMessage() ).append( "]" );
            }
            else {
                sb.append( "[" ).append( error.getObjectName() ).append( error.getDefaultMessage() ).append( "]" );
            }
        }
        return sb.toString();
    }

    private static String getErrorDetail( Throwable e ) {
        String errorMsg = "";
        if ( e != null ) {
            while ( e.getCause() != null ) {
                e = e.getCause();
            }
            errorMsg = e.getMessage();
        }
        return errorMsg;
    }

    private static String getCode( Exception e ) {
        String code = "";
        if ( e instanceof BizException ) {
            code = ( ( BizException ) e ).getCode();
        }
        return StringUtils.hasText( code ) ? code : RespCode.FAIL;
    }


}
