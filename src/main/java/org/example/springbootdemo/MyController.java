package org.example.springbootdemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/test1")
    public String test1(@RequestParam Integer id){
        // TODO 匹配找到對應的 id 回傳資料
        return "Judy";
    }

    @RequestMapping("/test2")
    public String test2(@RequestBody Student student){
        System.out.println(student.getId());
        // TODO 更新內容之類的操作
        return "編輯成功 "+student.getName();
    }

    @RequestMapping("/test3")
    public String test3(@RequestHeader Integer info){
        System.out.println(info.equals(123));
        if (info.equals(123)){
            return "Success";
        }else {
            return "Failed";
        }
    }

    @RequestMapping("/test4/{id}")
    public String test4(@PathVariable Integer id){
        return "Hello "+id;
    }
}
