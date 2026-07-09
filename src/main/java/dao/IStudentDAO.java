package dao;

import model.Student;

import java.util.List;

public interface IStudentDAO {

    int countStudents();
    boolean insert(Student student);
    boolean update(Student student);
    boolean updatePassword(int studentId, String newPassword);
    boolean deleteById(int id);
    Student findByEmail(String email);
    Student findById(int id);
    List<Student> findAllStudents();
    List<Student> findAllStudents(int offset, int limit);
}
