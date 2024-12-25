package org.example.springbootdemo;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {
    @Before("execution(* org.example.springbootdemo.HpPrinter.*(..))")
    public void before() {
        System.out.println("I'm before");
    }
}
