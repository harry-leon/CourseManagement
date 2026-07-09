package business.impl;

import business.IStudentService;
import dao.IStudentDAO;
import dao.impl.StudentDAOImpl;
import model.Student;
import utils.PasswordUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentServiceImpl implements IStudentService {

    private final IStudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public List<Student> getAll() {
        return studentDAO.findAllStudents();
    }

    @Override
    public List<Student> getAll(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return studentDAO.findAllStudents(offset, pageSize);
    }

    @Override
    public int countAll() {
        return studentDAO.countStudents();
    }

    @Override
    public boolean add(String name, LocalDate dob, String email, boolean sex, String phone, String password) {
        if (name == null || name.trim().isEmpty()) return false;
        if (email == null || email.trim().isEmpty()) return false;
        if (password == null || password.trim().isEmpty()) return false;

        String hashed = PasswordUtil.hash(password);
        Student s = new Student(name.trim(), dob, email.trim(), sex, phone, Student.Role.STUDENT, hashed);
        return studentDAO.insert(s);
    }

    @Override
    public boolean update(Student s) {
        return studentDAO.update(s);
    }

    @Override
    public boolean delete(int id) {
        return studentDAO.deleteById(id);
    }

    @Override
    public List<Student> search(String keyword) {
        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        return studentDAO.findAllStudents().stream().filter(s -> s.getName().toLowerCase().contains(kw) || s.getEmail().toLowerCase().contains(kw) || String.valueOf(s.getId()).equals(kw)).collect(Collectors.toList());
    }

    @Override
    public List<Student> sort(String field, boolean asc) {
        Comparator<Student> comparator = "name".equalsIgnoreCase(field) ? Comparator.comparing(Student::getName, String.CASE_INSENSITIVE_ORDER) : Comparator.comparingInt(Student::getId);
        if (!asc) {
            comparator = comparator.reversed();
        }
        return studentDAO.findAllStudents().stream().sorted(comparator).collect(Collectors.toList());
    }
}
