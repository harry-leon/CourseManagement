package dao;

import model.Course;

import java.util.List;

public interface ICourseDAO {

    Course findById(int id);
    int count();
    boolean insert(Course course);
    boolean update(Course course);
    boolean deleteById(int id);
    List<Course> findAll(int offset, int limit);
    List<Course> findAll();
}
