package org.example.springbootdemo;


import org.springframework.stereotype.Component;

@Component
public class CanonPrinter implements Printer {
    @Override
    public void print(String message) {
        System.out.println("Canon 列印機: "+message);
    }
}
