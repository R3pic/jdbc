package wisoft.student;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Student> getStudents(
    ) {

        return service.getStudents();
    }

    @GetMapping("/{no}")
    public Student getStudentByNo(
            @PathVariable("no") final String no
    ) {
        return this.service.getStudentByNo(no);
    }

    @GetMapping(params = "name")
    public Student getStudentByName(
            @RequestParam("name") final String name
    ) {
        return this.service.getStudentByName(name);
    }

    @GetMapping(params = "birthday")
    public Student getStudentByBirthday(
            @RequestParam("birthday") final String birthday
    ) {
        return this.service.getStudentByBirthday(birthday);
    }

    @PostMapping
    public String addStudent(@RequestBody final Student student) {
        final var count = this.service.insertStudent(student);

        return count + "명의 학생이 등록되었습니다.";
    }

    @PostMapping("/batch")
    public String addStudents(@RequestBody final List<Student> students) {
        final var count = this.service.insertStudentMultiBatch(students);

        return count + "명의 학생이 등록되었습니다.";
    }

    @PatchMapping
    public String updateStudent(@RequestBody final Student student) {
        final var count = this.service.updateStudent(student);

        return count + "명의 학생이 변경되었습니다.";
    }

    @PatchMapping("/batch")
    public String updateStudents(@RequestBody final List<Student> students) {
        final var count = this.service.updateStudentMulti(students);

        return count + "명의 학생이 변경되었습니다";
    }

    @DeleteMapping
    public String deleteStudent(@RequestBody final Student student) {
        final var count = this.service.deleteStudentByNo(student.getNo());

        return count + "명의 학생이 삭제되었습니다.";
    }

    @DeleteMapping("/batch")
    public String deleteStudents(@RequestBody final List<Student> students) {
        final var count = this.service.deleteStudentNoMulti(students);

        return count + "명의 학생이 삭제되었습니다";
    }
}
