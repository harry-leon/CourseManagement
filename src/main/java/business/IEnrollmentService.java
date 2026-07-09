package business;

import java.util.List;

public interface IEnrollmentService {

    class RegisterResult {
        public final boolean success;
        public final String message;
        public RegisterResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    // Đăng ký khóa học
    RegisterResult register(int studentId, int courseId);

    // Hủy đăng ký (chỉ khi chưa được Admin xác nhận)
    RegisterResult unregister(int studentId, int courseId);

    // Xem khóa đã đăng ký, sắp xếp theo tên hoặc ngày đăng ký
    List<Object[]> getRegisteredCourses(int studentId, String sortField, boolean asc);

    // Phân trang danh sách khóa đã đăng ký
    List<Object[]> getRegisteredCourses(int studentId, int page, int pageSize);

    // Tổng số khóa đã đăng ký
    int countRegisteredCourses(int studentId);

    // Admin: Xem danh sách đăng ký theo khóa học
    List<Object[]> getEnrollmentsByCourse(int courseId);

    // Phân trang danh sách đăng ký theo khóa
    List<Object[]> getEnrollmentsByCourse(int courseId, int page, int pageSize);

    // Tổng số đăng ký theo khóa
    int countEnrollmentsByCourse(int courseId);

    // Admin: Duyệt đăng ký → CONFIRM
    boolean approveEnrollment(int studentId, int courseId);

    // Admin: Từ chối đăng ký → DENIED
    boolean denyEnrollment(int studentId, int courseId);

    //Admin: Xóa đăng ký theo enrollment_id
    boolean deleteEnrollment(int enrollmentId);
}
