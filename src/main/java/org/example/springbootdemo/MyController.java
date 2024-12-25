package org.example.springbootdemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String product(){
      return "第一個是蘋果、第二個是橘子";
    }
}
