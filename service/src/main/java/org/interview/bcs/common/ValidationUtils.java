package org.interview.bcs.common;

import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author : wanghu
 * @since: 2025/5/15 18:26
 */
public class ValidationUtils {

    public static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static void validate( Object obj ) {
        ValidationResult validationResult = validateEntity( obj, Default.class );
        if ( validationResult.isHasErrors() ) {
            throw new BizException( validationResult.getErrorMessage() );
        }
    }


    public static <T> ValidationResult validateEntity( T obj, Class clazz ) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate( obj, new Class[]{clazz} );
        if ( !CollectionUtils.isEmpty( set ) ) {
            result.setHasErrors( true );
            Map<String, String> errorMsg = new HashMap();

            for ( ConstraintViolation<T> cv : set ) {
                errorMsg.put( cv.getPropertyPath().toString(), cv.getMessage() );
            }

            result.setErrorMsg( errorMsg );
        }

        return result;
    }
}
