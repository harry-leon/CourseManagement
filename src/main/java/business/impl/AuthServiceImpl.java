package business.impl;

import business.IAuthService;
import dao.IStudentDAO;
import dao.impl.StudentDAOImpl;
import model.Student;
import utils.PasswordUtil;

public class AuthServiceImpl implements IAuthService {
    private final IStudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public Student login(String email, String password) {
        Student student = studentDAO.findByEmail(email);
        if (student == null) return null;
        if (!PasswordUtil.matches(password, student.getPassword())) return null;
        return student;
    }

    @Override
    public boolean changePassword(int studentId, String oldPassword, String newPassword) {
        Student student = studentDAO.findById(studentId);
        if (student == null) return false;
        if (!PasswordUtil.matches(oldPassword, student.getPassword())) return false;
        if (newPassword == null || newPassword.trim().length() < 6) return false;
        return studentDAO.updatePassword(studentId, PasswordUtil.hash(newPassword));
    }
}