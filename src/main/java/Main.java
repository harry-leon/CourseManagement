import presentation.LoginView;
import utils.DBUtil;

public class Main {
    public static void main(String[] args) {
        System.out.println("Đang kiểm tra kết nối cơ sở dữ liệu...");
        if (!DBUtil.testConnection()) {
            System.out.println("Không thể kết nối PostgreSQL.");
            return;
        }
        System.out.println("Kết nối cơ sở dữ liệu thành công!");

        new LoginView().showMenu();
    }
}
