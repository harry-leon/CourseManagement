package business.impl;

import business.IEnrollmentService;
import dao.ICourseDAO;
import dao.IEnrollmentDAO;
import dao.impl.CourseDAOImpl;
import dao.impl.EnrollmentDAOImpl;
import model.Course;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EnrollmentServiceImpl implements IEnrollmentService {

    private final IEnrollmentDAO enrollmentDAO = new EnrollmentDAOImpl();
    private final ICourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public RegisterResult register(int studentId, int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            return new RegisterResult(false, "ID khóa học không tồn tại, vui lòng kiểm tra lại");
        }
        if (enrollmentDAO.exists(studentId, courseId)) {
            return new RegisterResult(false, "Bạn đã đăng ký khóa học này rồi!");
        }
        boolean ok = enrollmentDAO.insert(studentId, courseId);
        return new RegisterResult(ok, ok ? "Đăng ký thành công" : "Đăng ký thất bại, vui lòng thử lại");
    }

    @Override
    public RegisterResult unregister(int studentId, int courseId) {
        String status = enrollmentDAO.findStatus(studentId, courseId);
        if (status == null) {
            return new RegisterResult(false, "Bạn chưa đăng ký khóa học này.");
        }
        if ("CONFIRM".equals(status)) {
            return new RegisterResult(false, "Khóa học đã được xác nhận, không thể hủy đăng ký.");
        }
        if ("DENIED".equals(status)) {
            return new RegisterResult(false, "Đăng ký đã bị từ chối, không cần hủy.");
        }
        boolean ok = enrollmentDAO.deleteIfWaiting(studentId, courseId);
        return new RegisterResult(ok, ok ? "Hủy đăng ký thành công." : "Hủy thất bại, vui lòng thử lại.");
    }

    @Override
    public List<Object[]> getRegisteredCourses(int studentId, String sortField, boolean asc) {
        List<Object[]> rows = enrollmentDAO.findCoursesByStudent(studentId);
        Comparator<Object[]> comparator = "date".equalsIgnoreCase(sortField)
                ? Comparator.comparing(r -> (java.sql.Timestamp) r[4])
                : Comparator.comparing(r -> ((String) r[1]).toLowerCase());
        if (!asc) comparator = comparator.reversed();
        return rows.stream().sorted(comparator).collect(Collectors.toList());
    }

    @Override
    public List<Object[]> getRegisteredCourses(int studentId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return enrollmentDAO.findCoursesByStudent(studentId, offset, pageSize);
    }

    @Override
    public int countRegisteredCourses(int studentId) {
        return enrollmentDAO.countByStudent(studentId);
    }

    @Override
    public List<Object[]> getEnrollmentsByCourse(int courseId) {
        return enrollmentDAO.findByCourse(courseId);
    }

    @Override
    public List<Object[]> getEnrollmentsByCourse(int courseId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return enrollmentDAO.findByCourse(courseId, offset, pageSize);
    }

    @Override
    public int countEnrollmentsByCourse(int courseId) {
        return enrollmentDAO.countByCourse(courseId);
    }

    @Override
    public boolean approveEnrollment(int studentId, int courseId) {
        return enrollmentDAO.updateStatus(studentId, courseId, "CONFIRM");
    }

    @Override
    public boolean denyEnrollment(int studentId, int courseId) {
        return enrollmentDAO.updateStatus(studentId, courseId, "DENIED");
    }

    @Override
    public boolean deleteEnrollment(int enrollmentId) {
        return enrollmentDAO.deleteById(enrollmentId);
    }
}
