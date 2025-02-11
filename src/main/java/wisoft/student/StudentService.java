package wisoft.student;

import java.util.List;

public interface StudentService {

    List<Student> getStudents();

    Student getStudentByNo(final String studentNo);

    Student getStudentByName(final String studentName);

    Student getStudentByBirthday(final String studentBirthday);

    Integer insertStudent(final Student student);

    Integer insertStudentMulti(final List<Student> students);

    Integer insertStudentMultiBatch(final List<Student> students);

    Integer updateStudent(final Student student);

    Integer updateStudentMulti(final List<Student> students);

    Integer deleteStudentByNo(final String no);

    Integer deleteStudentNoMulti(final List<Student> students);

}