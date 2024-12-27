package org.example.springbootdemo;

import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {
    // 範例 - CRUD 學生資料 - 可參考教學 => https://ithelp.ithome.com.tw/articles/10335689

    // 實作 POST /students
    @PostMapping("/students")
    public String create(@RequestBody Student student) {
        return "執行資料庫的 Create 操作";
    }

    // 實作 GET /students/123
    @GetMapping("/students/{studentId}")
    public String read(@PathVariable Integer studentId) {
        return "執行資料庫 Read 操作";
    }

    // 實作 PUT /students/123
    @PutMapping("/students/{studentId}")
    public String update(@PathVariable Integer studentId, @RequestBody Student student) {
        return "執行資料庫 Update 操作";
    }

    // 實作 DELETE /students/123
    @DeleteMapping("/students/{studentId}")
    public String delete(@PathVariable Integer studentId) {
        return "執行資料庫的 Delete 操作";
    }
}
