package org.example.springbootdemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MyController {
    @Autowired
    @Qualifier("hpPrinter")
    private Printer printer;

    @RequestMapping("/test")
    public String test() {
        printer.print("Hello World");
        return "Hello World";
    }

    @RequestMapping("/product")
    public Store product(){
        Store store = new Store();
        List<String> list = new ArrayList<>();
        list.add("蘋果");
        list.add("橘子");
        store.setProductLists(list);
        return store;
    }

    @RequestMapping("/user")
    public Student user(){
        Student student = new Student();
        student.setName("Judy");
        return student;
    }
}
