package business;

import model.Course;

import java.util.List;

public interface ICourseService {
    int countAll();
    Course getById(int id);
    boolean add(String name, int duration, String instructor);
    boolean update(Course course);
    boolean delete(int id);

    // Tổng số khóa học
    List<Course> getAll();
    // Phân trang
    List<Course> getAll(int page, int pageSize);
    List<Course> searchByName(String keyword);
    List<Course> sort(String field, boolean asc);
}
