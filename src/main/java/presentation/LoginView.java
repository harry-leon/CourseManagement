package presentation;

import business.IAuthService;
import business.impl.AuthServiceImpl;
import model.Student;
import java.util.Scanner;

public class LoginView {

    private final Scanner scanner = new Scanner(System.in);
    private final IAuthService authService = new AuthServiceImpl();

    public void showMenu() {
        while (true) {
            System.out.println("\n===== HỆ THỐNG QUẢN LÝ KHÓA HỌC =====");
            System.out.println("[1] Đăng nhập");
            System.out.println("[2] Thoát");
            System.out.print("Chọn chức năng: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    doLogin();
                    break;
                case "2":
                    System.out.println("Tạm biệt!");
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ, vui lòng chọn lại.");
            }
        }
    }

    private void doLogin() {
        String email = readNonEmpty("Nhập email: ");
        String password = readNonEmpty("Nhập mật khẩu: ");
        Student student = authService.login(email, password);
        if (student == null) {
            System.out.println("Tài khoản hoặc mật khẩu không chính xác. Vui lòng thử lại.");
            return;
        }
        System.out.println("Đăng nhập thành công! Xin chào " + student.getName() + " (" + student.getRole() + ")");
        if (student.getRole() == Student.Role.ADMIN) {
            new AdminView(scanner, student).showMenu();
        } else {
            new StudentView(scanner, student).showMenu();
        }
    }

    private String readNonEmpty(String prompt) {
        String value;
        while (true) {
            System.out.print(prompt);
            value = scanner.nextLine();
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
            System.out.println("Không được để trống, vui lòng nhập lại.");
        }
    }
}
