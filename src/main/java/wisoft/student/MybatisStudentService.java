package wisoft.student;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import wisoft.common.MybatisAccess;

import java.util.ArrayList;
import java.util.List;

@Service
public class MybatisStudentService implements StudentService {

    final SqlSessionFactory sqlSessionFactory = MybatisAccess.getSqlSessionFactory();
    @Override
    public List<Student> getStudents() {
        final List<Student> students;

        try (final SqlSession session = sqlSessionFactory.openSession()) {
            final var studentService = session.getMapper(StudentService.class);
            students = studentService.getStudents();
            return students;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<Student>();
        }
    }

    @Override
    public Student getStudentByNo(final String studentNo) {
        final Student student;

        try (final SqlSession session = sqlSessionFactory.openSession()) {
            final var studentService = session.getMapper(StudentService.class);
            student = studentService.getStudentByNo(studentNo);
            return student;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return new Student();
        }
    }

    @Override
    public Student getStudentByName(final String studentName) {
        final Student student;

        try (final SqlSession session = sqlSessionFactory.openSession()) {
            final var studentService = session.getMapper(StudentService.class);
            student = studentService.getStudentByName(studentName);
            return student;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return new Student();
        }
    }

    @Override
    public Student getStudentByBirthday(final String studentBirthday) {
        final Student student;

        try (final SqlSession session = sqlSessionFactory.openSession()) {
            final var studentService = session.getMapper(StudentService.class);
            student = studentService.getStudentByBirthday(studentBirthday);
            return student;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return new Student();
        }
    }

    @Override
    public Integer insertStudent(final Student student) {
        try (final SqlSession session = sqlSessionFactory.openSession()) {
            final var studentService = session.getMapper(StudentService.class);
            int result = studentService.insertStudent(student);
            session.commit();
            return result;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer insertStudentMulti(final List<Student> students) {
        try (final SqlSession session = sqlSessionFactory.openSession()) {
            int result = 0;
            final var studentService = session.getMapper(StudentService.class);
            result = studentService.insertStudentMulti(students);
            session.commit();
            return result;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer insertStudentMultiBatch(final List<Student> students) {
        try (final SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            final var studentService = session.getMapper(StudentService.class);
            for (Student student : students) {
                studentService.insertStudent(student);
            }
            session.commit();
            return students.size();
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer updateStudent(final Student student) {
        try (final SqlSession session = sqlSessionFactory.openSession()) {
            final var studentService = session.getMapper(StudentService.class);
            int result = studentService.updateStudent(student);
            session.commit();
            return result;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer updateStudentMulti(final List<Student> students) {
        try (final SqlSession session = sqlSessionFactory.openSession()) {
            final var studentService = session.getMapper(StudentService.class);
            for (Student student : students) {
                studentService.updateStudent(student);
            }
            session.commit();
            return students.size();
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer deleteStudentByNo(final String no) {
        try (final SqlSession session = sqlSessionFactory.openSession()) {
            final var studentService = session.getMapper(StudentService.class);
            int result = studentService.deleteStudentByNo(no);
            session.commit();
            return result;
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    @Override
    public Integer deleteStudentNoMulti(final List<Student> students) {
        try (final SqlSession session = sqlSessionFactory.openSession()) {
            final var studentService = session.getMapper(StudentService.class);
            for (Student student : students) {
                studentService.deleteStudentByNo(student.getNo());
            }
            session.commit();
            return students.size();
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }
}
