package business;

import model.Student;

public interface IAuthService {
    // trả về Student nếu đăng nhập thành công, null nếu sai tài khoản/mật khẩu
    Student login(String email, String password);
    // đổi mật khẩu, có xác thực lại email/sđt + mật khẩu cũ
    boolean changePassword(int studentId, String oldPassword, String newPassword);
}
