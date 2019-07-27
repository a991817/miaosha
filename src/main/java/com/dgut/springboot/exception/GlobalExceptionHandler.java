package com.dgut.springboot.exception;
import com.dgut.springboot.result.CodeMsg;
import com.dgut.springboot.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(Exception e){
        e.printStackTrace();
//       假如说是全局异常
        if( e instanceof GlobalException ){
            GlobalException globalException = (GlobalException) e;
            return Result.error(globalException.getCodeMsg());
        }
//       假如说是绑定异常，即是校验异常
        else if( e instanceof BindException ){
            BindException bindException = (BindException) e;
            List<ObjectError> objectErrors = bindException.getAllErrors();
            ObjectError objectError = objectErrors.get(0);
            String msg = objectError.getDefaultMessage();
//            绑定错误信息
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else{
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
