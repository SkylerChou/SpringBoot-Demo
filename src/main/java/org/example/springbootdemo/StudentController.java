package org.example.springbootdemo;

import org.apache.coyote.Response;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StudentController {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @RequestMapping("/students/insert")
    public String insert(@RequestBody Student student) {
        String sql = "INSERT INTO student(id, name) VALUES (:studentId, :StudentName)";

        Map<String, Object> map = new HashMap<>();
        map.put("studentId", student.getId());
        map.put("StudentName", student.getName());

        namedParameterJdbcTemplate.update(sql, map);
        return "執行 INSERT sql";
    }

    @RequestMapping("/students/search")
    public ResponseEntity<Object> search(@RequestBody Student student) {
        String sql = "SELECT * FROM student WHERE id = :StudentId AND name = :StudentName";
        StudentRowMapper studentRowMapper = new StudentRowMapper();

        Map<String, Object> map = new HashMap<>();
        map.put("StudentName", student.getName());
        map.put("StudentId", student.getId());

        try {
            List<Student> students= namedParameterJdbcTemplate.query(sql, map, studentRowMapper);
            if (students.isEmpty()){
                return ResponseEntity.status(200).body(Map.of("message", "No students found", "status", "error"));
            }
            return ResponseEntity.ok(Map.of("message", "查詢資料庫成功", "data", students, "status", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Query failed", "error", e.getMessage()));
        }
    }


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
