package com.pim.annotations;


import com.pim.enums.CategoryType;
import com.pim.enums.Modules;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface PimFrameworkAnnotation {

    public Modules module();
    public CategoryType[] category();
    


}
