package business;

import model.Student;

import java.time.LocalDate;
import java.util.List;

public interface IStudentService {

    // Tổng số học viên
    int countAll();
    boolean add(String name, LocalDate dob, String email, boolean sex, String phone, String password);
    boolean update(Student student);
    boolean delete(int id);

    List<Student> getAll();
    // Phân trang
    List<Student> getAll(int page, int pageSize);
    List<Student> search(String keyword);
    List<Student> sort(String field, boolean asc);
}
