package presentation;

import business.ICourseService;
import business.IEnrollmentService;
import business.IStudentService;
import business.impl.CourseServiceImpl;
import business.impl.EnrollmentServiceImpl;
import business.impl.StudentServiceImpl;
import model.Course;
import model.Student;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AdminView {

    private final Scanner scanner;
    private final Student currentAdmin;
    private final ICourseService courseService = new CourseServiceImpl();
    private final IStudentService studentService = new StudentServiceImpl();
    private final IEnrollmentService enrollmentService = new EnrollmentServiceImpl();

    public AdminView(Scanner scanner, Student currentAdmin) {
        this.scanner = scanner;
        this.currentAdmin = currentAdmin;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("[1] Quản lý khóa học");
            System.out.println("[2] Quản lý học viên");
            System.out.println("[3] Quản lý đăng ký khóa học");
            System.out.println("[4] Đăng xuất");
            System.out.print("Chọn chức năng: ");

            switch (scanner.nextLine().trim()) {
                case "1": courseMenu(); break;
                case "2": studentMenu(); break;
                case "3": registrationMenu(); break;
                case "4": return;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    // ===================== QUẢN LÝ KHÓA HỌC =====================

    private void courseMenu() {
        while (true) {
            System.out.println("\n----- QUẢN LÝ KHÓA HỌC -----");
            System.out.println("[1] Xem danh sách");
            System.out.println("[2] Thêm mới");
            System.out.println("[3] Chỉnh sửa");
            System.out.println("[4] Xóa");
            System.out.println("[5] Tìm kiếm");
            System.out.println("[6] Sắp xếp");
            System.out.println("[7] Quay về menu chính");
            System.out.print("Chọn chức năng: ");

            switch (scanner.nextLine().trim()) {
                case "1": printCoursesWithPage(); break;
                case "2": addCourse(); break;
                case "3": editCourse(); break;
                case "4": deleteCourse(); break;
                case "5": searchCourse(); break;
                case "6": sortCourse(); break;
                case "7": return;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void addCourse() {
        System.out.print("Tên khóa học: ");
        String name = scanner.nextLine().trim();
        int duration = readInt("Thời lượng (giờ): ");
        System.out.print("Giảng viên: ");
        String instructor = scanner.nextLine().trim();

        boolean ok = courseService.add(name, duration, instructor);
        System.out.println(ok ? "Thêm khóa học thành công." : "Thêm thất bại, kiểm tra lại dữ liệu.");
    }

    private void editCourse() {
        int id = readInt("Nhập ID khóa học cần sửa: ");
        Course course = courseService.getById(id);
        if (course == null) {
            System.out.println("ID khóa học không tồn tại, vui lòng kiểm tra lại.");
            return;
        }
        System.out.println("Chọn thuộc tính cần sửa:");
        System.out.println("[1] Tên   [2] Thời lượng   [3] Giảng viên");
        switch (scanner.nextLine().trim()) {
            case "1":
                System.out.print("Tên mới: ");
                course.setName(scanner.nextLine().trim());
                break;
            case "2":
                course.setDuration(readInt("Thời lượng mới: "));
                break;
            case "3":
                System.out.print("Giảng viên mới: ");
                course.setInstructor(scanner.nextLine().trim());
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                return;
        }
        System.out.println(courseService.update(course) ? "Cập nhật thành công." : "Cập nhật thất bại.");
    }

    private void deleteCourse() {
        int id = readInt("Nhập ID khóa học cần xóa: ");
        if (courseService.getById(id) == null) {
            System.out.println("ID khóa học không tồn tại, vui lòng kiểm tra lại.");
            return;
        }
        System.out.print("Bạn có chắc chắn muốn xóa (Y/N)? ");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println(courseService.delete(id) ? "Xóa thành công." : "Xóa thất bại.");
        } else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }

    private void searchCourse() {
        System.out.print("Nhập từ khóa tên khóa học: ");
        printCourses(courseService.searchByName(scanner.nextLine()));
    }

    private void sortCourse() {
        System.out.print("Sắp xếp theo (id/name): ");
        String field = scanner.nextLine().trim();
        System.out.print("Thứ tự (1: tăng dần, 2: giảm dần): ");
        boolean asc = !"2".equals(scanner.nextLine().trim());
        printCourses(courseService.sort(field, asc));
    }

    private void printCourses(List<Course> courses) {
        System.out.println("-".repeat(70));
        System.out.printf("%-5s %-30s %-10s %-20s%n", "ID", "Tên khóa học", "Thời lượng", "Giảng viên");
        System.out.println("-".repeat(70));
        for (Course c : courses) System.out.println(c);
        System.out.println("-".repeat(70));
    }

    private void printCoursesWithPage() {
        final int PAGE_SIZE = 5;
        int total = courseService.countAll();
        if (total == 0) { System.out.println("Chưa có khóa học nào."); return; }
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        int page = 1;
        while (true) {
            printCourses(courseService.getAll(page, PAGE_SIZE));
            Integer next = navigatePage(page, totalPages);
            if (next == null) break;
            page = next;
        }
    }

    // ===================== QUẢN LÝ HỌC VIÊN =====================

    private void studentMenu() {
        while (true) {
            System.out.println("\n----- QUẢN LÝ HỌC VIÊN -----");
            System.out.println("[1] Xem danh sách");
            System.out.println("[2] Thêm mới");
            System.out.println("[3] Chỉnh sửa");
            System.out.println("[4] Xóa");
            System.out.println("[5] Tìm kiếm");
            System.out.println("[6] Sắp xếp");
            System.out.println("[7] Quay về menu chính");
            System.out.print("Chọn chức năng: ");

            switch (scanner.nextLine().trim()) {
                case "1": printStudentsWithPage(); break;
                case "2": addStudent(); break;
                case "3": editStudent(); break;
                case "4": deleteStudent(); break;
                case "5":
                    System.out.print("Nhập từ khóa (tên/email/id): ");
                    printStudents(studentService.search(scanner.nextLine()));
                    break;
                case "6":
                    System.out.print("Sắp xếp theo (id/name): ");
                    String field = scanner.nextLine().trim();
                    System.out.print("Thứ tự (1: tăng dần, 2: giảm dần): ");
                    boolean asc = !"2".equals(scanner.nextLine().trim());
                    printStudents(studentService.sort(field, asc));
                    break;
                case "7": return;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void addStudent() {
        System.out.print("Họ tên: ");
        String name = scanner.nextLine().trim();
        LocalDate dob = readDate("Ngày sinh (yyyy-MM-dd): ");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Giới tính (1: Nam, 0: Nữ): ");
        boolean sex = "1".equals(scanner.nextLine().trim());
        System.out.print("Số điện thoại (có thể để trống): ");
        String phone = scanner.nextLine().trim();
        System.out.print("Mật khẩu ban đầu: ");
        String password = scanner.nextLine().trim();

        boolean ok = studentService.add(name, dob, email, sex, phone.isEmpty() ? null : phone, password);
        System.out.println(ok ? "Thêm học viên thành công." : "Thêm thất bại, kiểm tra lại dữ liệu.");
    }

    private void editStudent() {
        int id = readInt("Nhập ID học viên cần sửa: ");
        Student student = studentService.getAll().stream()
                .filter(s -> s.getId() == id).findFirst().orElse(null);
        if (student == null) {
            System.out.println("ID học viên không tồn tại, vui lòng kiểm tra lại.");
            return;
        }

        System.out.println("Chọn thuộc tính cần sửa:");
        System.out.println("[1] Họ tên   [2] Email   [3] Số điện thoại");
        switch (scanner.nextLine().trim()) {
            case "1":
                System.out.print("Họ tên mới: ");
                student.setName(scanner.nextLine().trim());
                break;
            case "2":
                System.out.print("Email mới: ");
                student.setEmail(scanner.nextLine().trim());
                break;
            case "3":
                System.out.print("Số điện thoại mới: ");
                student.setPhone(scanner.nextLine().trim());
                break;
            default:
                System.out.println("Lựa chọn không hợp lệ.");
                return;
        }
        System.out.println(studentService.update(student) ? "Cập nhật thành công." : "Cập nhật thất bại.");
    }

    private void deleteStudent() {
        int id = readInt("Nhập ID học viên cần xóa: ");
        System.out.print("Bạn có chắc chắn muốn xóa (Y/N)? ");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println(studentService.delete(id) ? "Xóa thành công." : "Xóa thất bại.");
        } else {
            System.out.println("Đã hủy thao tác xóa.");
        }
    }

    private void printStudents(List<Student> students) {
        System.out.println("-".repeat(75));
        System.out.printf("%-5s %-20s %-25s %-5s %-15s%n", "ID", "Họ tên", "Email", "GT", "SĐT");
        System.out.println("-".repeat(75));
        for (Student s : students) System.out.println(s);
        System.out.println("-".repeat(75));
    }

    private void printStudentsWithPage() {
        final int PAGE_SIZE = 5;
        int total = studentService.countAll();
        if (total == 0) { System.out.println("Chưa có học viên nào."); return; }
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        int page = 1;
        while (true) {
            printStudents(studentService.getAll(page, PAGE_SIZE));
            Integer next = navigatePage(page, totalPages);
            if (next == null) break;
            page = next;
        }
    }

    // ===================== QUẢN LÝ ĐĂNG KÝ =====================

    private void registrationMenu() {
        while (true) {
            System.out.println("\n----- QUẢN LÝ ĐĂNG KÝ KHÓA HỌC -----");
            System.out.println("[1] Xem danh sách đăng ký theo khóa");
            System.out.println("[2] Duyệt đăng ký");
            System.out.println("[3] Từ chối đăng ký");
            System.out.println("[4] Xóa đăng ký");
            System.out.println("[5] Quay về menu chính");
            System.out.print("Chọn chức năng: ");

            switch (scanner.nextLine().trim()) {
                case "1": viewEnrollments(); break;
                case "2": processEnrollment("CONFIRM"); break;
                case "3": processEnrollment("DENIED"); break;
                case "4": deleteEnrollment(); break;
                case "5": return;
                default: System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    private void viewEnrollments() {
        int courseId = readInt("Nhập ID khóa học: ");
        final int PAGE_SIZE = 5;
        int total = enrollmentService.countEnrollmentsByCourse(courseId);
        if (total == 0) { System.out.println("Chưa có đăng ký cho khóa học này."); return; }
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);
        int page = 1;
        while (true) {
            List<Object[]> list = enrollmentService.getEnrollmentsByCourse(courseId, page, PAGE_SIZE);
            System.out.printf("%-5s %-10s %-20s %-22s %-10s%n", "ID", "StudentID", "Tên học viên", "Ngày đăng ký", "Trạng thái");
            System.out.println("-".repeat(72));
            for (Object[] row : list)
                System.out.printf("%-5d %-10d %-20s %-22s %-10s%n", row[0], row[1], row[2], row[3], row[4]);
            System.out.println("-".repeat(72));
            Integer next = navigatePage(page, totalPages);
            if (next == null) break;
            page = next;
        }
    }

    private void processEnrollment(String action) {
        int courseId = readInt("Nhập ID khóa học: ");
        int studentId = readInt("Nhập ID học viên: ");
        String label = "CONFIRM".equals(action) ? "duyệt" : "từ chối";
        System.out.print("Bạn có chắc chắn muốn " + label + " đăng ký? (Y/N): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println("Đã hủy thao tác.");
            return;
        }
        boolean ok = "CONFIRM".equals(action)
                ? enrollmentService.approveEnrollment(studentId, courseId)
                : enrollmentService.denyEnrollment(studentId, courseId);
        System.out.println(ok ? ("Đã " + label + " thành công.") : "Thao tác thất bại, kiểm tra lại ID.");
    }

    private void deleteEnrollment() {
        int enrollmentId = readInt("Nhập ID đăng ký cần xóa: ");
        System.out.print("Bạn có chắc chắn muốn xóa đăng ký này? (Y/N): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println("Đã hủy thao tác.");
            return;
        }
        System.out.println(enrollmentService.deleteEnrollment(enrollmentId) ? "Xóa thành công." : "Xóa thất bại.");
    }

    // ===================== HELPER =====================

    /**
     * Hiển thị thanh điều hướng phân trang, trả về trang tiếp theo hoặc null nếu thoát.
     */
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

    private LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Sai định dạng ngày (yyyy-MM-dd), vui lòng nhập lại.");
            }
        }
    }
}
