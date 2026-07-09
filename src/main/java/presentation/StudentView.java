package presentation;

import business.IAuthService;
import business.ICourseService;
import business.IEnrollmentService;
import business.impl.AuthServiceImpl;
import business.impl.CourseServiceImpl;
import business.impl.EnrollmentServiceImpl;
import model.Course;
import model.Student;

import java.util.List;
import java.util.Scanner;

public class StudentView {

    private final Scanner scanner;
    private final Student currentStudent;
    private final ICourseService courseService = new CourseServiceImpl();
    private final IEnrollmentService enrollmentService = new EnrollmentServiceImpl();
    private final IAuthService authService = new AuthServiceImpl();

    public StudentView(Scanner scanner, Student currentStudent) {
        this.scanner = scanner;
        this.currentStudent = currentStudent;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== MENU HỌC VIÊN =====");
            System.out.println("[1] Xem khóa học đang có");
            System.out.println("[2] Đăng ký học");
            System.out.println("[3] Xem khóa đã đăng ký");
            System.out.println("[4] Hủy đăng ký");
            System.out.println("[5] Đổi mật khẩu");
            System.out.println("[6] Đăng xuất");
            System.out.print("Chọn chức năng: ");

            switch (scanner.nextLine().trim()) {
                case "1": viewCourses(); break;
                case "2": registerCourse(); break;
                case "3": viewRegisteredCourses(); break;
                case "4": unregisterCourse(); break;
                case "5": changePassword(); break;
                case "6": return;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void viewCourses() {
        System.out.print("Nhập từ khóa tìm kiếm (Enter để xem tất cả có phân trang): ");
        String kw = scanner.nextLine().trim();
        if (!kw.isEmpty()) {
            printCourseTable(courseService.searchByName(kw));
            return;
        }
        final int PAGE_SIZE = 5;
        int total = courseService.countAll();
        if (total == 0) { System.out.println("Chưa có khóa học nào."); return; }
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        int page = 1;
        while (true) {
            printCourseTable(courseService.getAll(page, PAGE_SIZE));
            Integer next = navigatePage(page, totalPages);
            if (next == null) break;
            page = next;
        }
    }

    private void printCourseTable(List<Course> courses) {
        System.out.println("-".repeat(70));
        System.out.printf("%-5s %-30s %-10s %-20s%n", "ID", "Tên khóa học", "Thời lượng", "Giảng viên");
        System.out.println("-".repeat(70));
        for (Course c : courses) System.out.println(c);
        System.out.println("-".repeat(70));
    }

    private void registerCourse() {
        int courseId = readInt("Nhập ID khóa học muốn đăng ký: ");
        var result = enrollmentService.register(currentStudent.getId(), courseId);
        System.out.println(result.message);
    }

    private void unregisterCourse() {
        int courseId = readInt("Nhập ID khóa học muốn hủy đăng ký: ");
        var result = enrollmentService.unregister(currentStudent.getId(), courseId);
        System.out.println(result.message);
    }

    private void viewRegisteredCourses() {
        final int PAGE_SIZE = 5;
        int total = enrollmentService.countRegisteredCourses(currentStudent.getId());
        if (total == 0) { System.out.println("Bạn chưa đăng ký khóa học nào."); return; }
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        int page = 1;
        while (true) {
            List<Object[]> rows = enrollmentService.getRegisteredCourses(currentStudent.getId(), page, PAGE_SIZE);
            System.out.printf("%-5s %-30s %-20s %-20s %-10s%n", "ID", "Tên khóa học", "Ngày đăng ký", "Giảng viên", "Trạng thái");
            System.out.println("-".repeat(90));
            for (Object[] r : rows)
                System.out.printf("%-5s %-30s %-20s %-20s %-10s%n", r[0], r[1], r[4], r[3], r[5]);
            System.out.println("-".repeat(90));
            Integer next = navigatePage(page, totalPages);
            if (next == null) break;
            page = next;
        }
    }

    private void changePassword() {
        System.out.print("Xác nhận email hoặc số điện thoại: ");
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase(currentStudent.getEmail())
                && !confirm.equals(currentStudent.getPhone())) {
            System.out.println("Thông tin xác thực không khớp, từ chối truy cập.");
            return;
        }

        System.out.print("Mật khẩu cũ: ");
        String oldPw = scanner.nextLine();
        System.out.print("Mật khẩu mới (tối thiểu 6 ký tự): ");
        String newPw = scanner.nextLine();

        boolean ok = authService.changePassword(currentStudent.getId(), oldPw, newPw);
        System.out.println(ok ? "Đổi mật khẩu thành công." : "Đổi mật khẩu thất bại, kiểm tra lại thông tin.");
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập một số nguyên hợp lệ.");
            }
        }
    }

    private Integer navigatePage(int page, int totalPages) {
        System.out.printf("--- Trang %d/%d --- ", page, totalPages);
        if (page < totalPages) System.out.print("[n] Tiếp  ");
        else System.out.print("           ");
        if (page > 1) System.out.print("[p] Trước  ");
        else System.out.print("            ");
        System.out.print("[q] Thoát: ");

        String nav = scanner.nextLine().trim().toLowerCase();
        if ("n".equals(nav)) {
            if (page < totalPages) return page + 1;
            System.out.println("Đây là trang cuối.");
            return page;
        }
        if ("p".equals(nav)) {
            if (page > 1) return page - 1;
            System.out.println("Đây là trang đầu.");
            return page;
        }
        if ("q".equals(nav)) return null;
        System.out.println("Lựa chọn không hợp lệ.");
        return page;
    }
}
