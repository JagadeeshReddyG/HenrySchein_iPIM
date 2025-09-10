package com.pim.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.pim.enums.TestCaseSheet;

@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface JiraAnnotation {
	public String[] TestCaseIds();
}
