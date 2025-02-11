package com.foc.annotations.model.fields;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface FocString {
	public int size() default 50;
	public boolean mandatory() default false;
	public boolean dbResident() default true;
	public boolean compress() default false;
}
