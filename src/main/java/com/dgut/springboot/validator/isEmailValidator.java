package com.dgut.springboot.validator;
import com.dgut.springboot.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class isEmailValidator implements ConstraintValidator<isEmail,String> {
    private boolean required = false;

    @Override
    public void initialize(isEmail constraintAnnotation) {
        required = constraintAnnotation.required();
    }
//    在这个方法里做校验
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
//      如果不可以为空
            return ValidatorUtil.isEmail(s);
        }else{
//      如果可为空
            if(StringUtils.isEmpty(s)){
                return true;
            }else{
                return ValidatorUtil.isEmail(s);
            }
        }
    }
}
