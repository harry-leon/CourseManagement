package dao;

import java.util.List;

public interface IEnrollmentDAO {

    int countByCourse(int courseId);
    int countByStudent(int studentId);
    boolean exists(int studentId, int courseId);
    boolean insert(int studentId, int courseId);
    boolean deleteIfWaiting(int studentId, int courseId);
    boolean updateStatus(int studentId, int courseId, String status);
    boolean deleteById(int enrollmentId);
    String findStatus(int studentId, int courseId);
    List<Object[]> findCoursesByStudent(int studentId);
    List<Object[]> findCoursesByStudent(int studentId, int offset, int limit);
    List<Object[]> findByCourse(int courseId);
    List<Object[]> findByCourse(int courseId, int offset, int limit);
}
