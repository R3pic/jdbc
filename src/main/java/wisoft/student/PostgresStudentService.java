package wisoft.student;

import wisoft.common.PostgresAccess;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PostgresStudentService implements StudentService{
    @Override
    public List<Student> getStudents() {
        final var query = "SELECT * FROM student";
        final List<Student> students = new ArrayList<>();

        try (
                final Connection conn = PostgresAccess.setConnection();
                final PreparedStatement statement = conn.prepareStatement(query);
                final ResultSet resultSet = statement.executeQuery();
                ) {
            while (resultSet.next()) {
                Date date = resultSet.getDate(3);
                LocalDate localDate = null;
                if (date != null) {
                    localDate = date.toLocalDate();
                }
                
                final Student student = new Student(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        localDate
                );
                students.add(student);
            }
        } catch (final SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState    : " + e.getSQLState());
        }

        return students;
    }

    @Override
    public Student getStudentByNo(final String studentNo) {
        final String query = "SELECT * from student where no = ?";
        Student student = new Student();

        try (
             final Connection connection = PostgresAccess.setConnection();
             final PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, studentNo);

            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    student = new Student(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDate(3).toLocalDate()
                    );
                }
            }
        } catch (final SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState    : " + e.getSQLState());
        }

        return student;
    }

    @Override
    public Student getStudentByName(final String studentName) {
        final String query = "SELECT * from student where name = ?";
        Student student = new Student();

        try (
                final Connection connection = PostgresAccess.setConnection();
                final PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, studentName);

            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    student = new Student(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDate(3).toLocalDate()
                    );
                }
            }
        } catch (final SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState    : " + e.getSQLState());
        }

        return student;
    }

    @Override
    public Student getStudentByBirthday(final String studentBirthday) {
        final String query = "SELECT * from student where birthday = ?";
        Student student = new Student();

        try (
                final Connection connection = PostgresAccess.setConnection();
                final PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setDate(1, Date.valueOf(studentBirthday));

            try (final ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    student = new Student(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDate(3).toLocalDate()
                    );
                }
            }
        } catch (final SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState    : " + e.getSQLState());
        }

        return student;
    }

    @Override
    public Integer insertStudent(final Student student) {
        final String query = "INSERT INTO student (no, name, birthday) VALUES (?, ?, ?)";
        int result = 0;
        try (
                final Connection connection = PostgresAccess.setConnection();
                ) {
            connection.setAutoCommit(false);

            try (final PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, student.getNo());
                statement.setString(2, student.getName());

                if (student.getBirthday() == null) {
                    statement.setNull(3, Types.DATE);
                } else {
                    statement.setDate(3, Date.valueOf(student.getBirthday()));
                }

                result = statement.executeUpdate();
                connection.commit();
            } catch (final SQLException e) {
                connection.rollback();
                System.err.println("SQLException: " + e.getMessage());
                System.err.println("SQLState   : " + e.getSQLState());
            }

        } catch (final SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState   : " + e.getSQLState());
        }

        return result;
    }

    @Override
    public Integer insertStudentMulti(final List<Student> students) {
        final String query = "INSERT INTO student (no, name, birthday) VALUES (?, ?, ?)";
        int result = 0;
        try (
                final Connection connection = PostgresAccess.setConnection();
        ) {
            connection.setAutoCommit(false);

            try (final PreparedStatement statement = connection.prepareStatement(query)) {
                for (final Student student : students) {
                    if (student.getNo() == null && student.getName() == null) {
                        break;
                    }

                    statement.setString(1, student.getNo());
                    statement.setString(2, student.getName());

                    if (student.getBirthday() == null) {
                        statement.setNull(3, Types.DATE);
                    } else {
                        statement.setDate(3, Date.valueOf(student.getBirthday()));
                    }

                    result += statement.executeUpdate();
                    statement.clearParameters();
                }
                connection.commit();
            } catch (final SQLException e) {
                connection.rollback();
                System.err.println("SQLException: " + e.getMessage());
                System.err.println("SQLState   : " + e.getSQLState());
            }

        } catch (final SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState   : " + e.getSQLState());
        }

        return result;
    }

    @Override
    public Integer insertStudentMultiBatch(final List<Student> students) {
        final String query = "INSERT INTO student (no, name, birthday) VALUES (?, ?, ?)";
        int[] result = new int[10];
        try (
                final Connection connection = PostgresAccess.setConnection();
        ) {
            connection.setAutoCommit(false);

            try (final PreparedStatement statement = connection.prepareStatement(query)) {
                for (final Student student : students) {
                    if (student.getNo() == null && student.getName() == null) {
                        break;
                    }

                    statement.setString(1, student.getNo());
                    statement.setString(2, student.getName());

                    if (student.getBirthday() == null) {
                        statement.setNull(3, Types.DATE);
                    } else {
                        statement.setDate(3, Date.valueOf(student.getBirthday()));
                    }
                    statement.addBatch();
                    statement.clearParameters();
                }

                result = statement.executeBatch();
                connection.commit();
            } catch (final SQLException e) {
                connection.rollback();
                System.err.println("SQLException: " + e.getMessage());
                System.err.println("SQLState   : " + e.getSQLState());
            }

        } catch (final SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
            System.err.println("SQLState   : " + e.getSQLState());
        }

        return result.length;
    }

    @Override
    public Integer updateStudent(final Student student) {
        final String query = "UPDATE student SET birthday = ? WHERE no = ?";
        var result = 0;

        try (final Connection conn = PostgresAccess.setConnection()) {
            try {
                conn.setAutoCommit(false);

                try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setDate(1, Date.valueOf(student.getBirthday()));
                    pstmt.setString(2, student.getNo());

                    result = pstmt.executeUpdate();
                    conn.commit();
                }
            } catch (final SQLException sqle) {
                conn.rollback();
                System.err.println("SQLException: " + sqle.getMessage());
                System.out.println("SQLState: " + sqle.getSQLState());
            }
        } catch (final SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("SQLState: " + sqle.getSQLState());
        }
        return result;
    }

    @Override
    public Integer updateStudentMulti(final List<Student> students) {
        final String query = "UPDATE student SET birthday = ? WHERE no = ?";
        var result = new int[10];

        try (final Connection conn = PostgresAccess.setConnection()) {
            try {
                conn.setAutoCommit(false);
                try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
                    for (final Student student : students) {
                        if (student.getNo() == null && student.getBirthday() == null) {
                            break;
                        }

                        pstmt.setDate(1, Date.valueOf(student.getBirthday()));
                        pstmt.setString(2, student.getNo());

                        pstmt.addBatch();
                        pstmt.clearParameters();
                    }

                    result = pstmt.executeBatch();
                    conn.commit();
                }
            } catch (final SQLException sqle) {
                conn.rollback();
                System.err.println("SQLException: " + sqle.getMessage());
                System.out.println("SQLState: " + sqle.getSQLState());
            }
        } catch (SQLException sqex) {
            System.out.println("SQLException: " + sqex.getMessage());
            System.out.println("SQLState: " + sqex.getSQLState());
        }
        return result.length;
    }

    @Override
    public Integer deleteStudentByNo(final String no) {
        final String query = "DELETE FROM STUDENT WHERE NO = ?";
        var result = 0;

        try (final Connection conn = PostgresAccess.setConnection()) {
            try {
                conn.setAutoCommit(false);

                try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, no);

                    result = pstmt.executeUpdate();
                    conn.commit();
                }
            } catch (final SQLException sqle) {
                conn.rollback();
                System.err.println("SQLException: " + sqle.getMessage());
                System.out.println("SQLState: " + sqle.getSQLState());
            }
        } catch (final SQLException sqle) {
            System.out.println("SQLException: " + sqle.getMessage());
            System.out.println("SQLState: " + sqle.getSQLState());
        }
        return result;
    }

    @Override
    public Integer deleteStudentNoMulti(final List<Student> students) {
        final String query = "DELETE FROM STUDENT WHERE NO = ?";
        var result = new int[10];

        try (final Connection conn = PostgresAccess.setConnection()) {
            try {
                conn.setAutoCommit(false);
                try (final PreparedStatement pstmt = conn.prepareStatement(query)) {
                    for (final Student student : students) {
                        if (student.getNo() == null) {
                            break;
                        }

                        pstmt.setString(1, student.getNo());
                        pstmt.addBatch();
                        pstmt.clearParameters();
                    }

                    result = pstmt.executeBatch();
                    conn.commit();
                }
            } catch (final SQLException sqle) {
                conn.rollback();
                System.err.println("SQLException: " + sqle.getMessage());
                System.out.println("SQLState: " + sqle.getSQLState());
            }
        } catch (SQLException sqex) {
            System.out.println("SQLException: " + sqex.getMessage());
            System.out.println("SQLState: " + sqex.getSQLState());
        }
        return result.length;
    }
}
