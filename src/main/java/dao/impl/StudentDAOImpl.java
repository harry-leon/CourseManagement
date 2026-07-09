package dao.impl;

import dao.IStudentDAO;
import model.Student;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Tầng DAO Impl - cài đặt thao tác DB bằng JDBC cho bảng student.
 * KHÔNG chứa logic nghiệp vụ (validate, kiểm tra trùng...) - việc đó thuộc tầng business.
 */
public class StudentDAOImpl implements IStudentDAO {

    @Override
    public Student findByEmail(String email) {
        String sql = "SELECT * FROM student WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn findByEmail: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Student findById(int id) {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn findById: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Student> findAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE role = 'STUDENT' ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn findAllStudents: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Student> findAllStudents(int offset, int limit) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE role = 'STUDENT' ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn findAllStudents(page): " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public int countStudents() {
        String sql = "SELECT COUNT(*) FROM student WHERE role = 'STUDENT'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đếm student: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean insert(Student s) {
        String sql = "INSERT INTO student (name, dob, email, sex, phone, role, password) " +
                "VALUES (?, ?, ?, ?::bit, ?, ?::user_role, ?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setDate(2, Date.valueOf(s.getDob()));
            ps.setString(3, s.getEmail());
            ps.setString(4, s.isSex() ? "1" : "0");
            ps.setString(5, s.getPhone());
            ps.setString(6, s.getRole().name());
            ps.setString(7, s.getPassword());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thêm học viên: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Student s) {
        String sql = "UPDATE student SET name = ?, dob = ?, email = ?, sex = ?::bit, phone = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setDate(2, Date.valueOf(s.getDob()));
            ps.setString(3, s.getEmail());
            ps.setString(4, s.isSex() ? "1" : "0");
            ps.setString(5, s.getPhone());
            ps.setInt(6, s.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật học viên: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updatePassword(int studentId, String newPassword) {
        String sql = "UPDATE student SET password = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, studentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đổi mật khẩu: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa học viên: " + e.getMessage(), e);
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("id"));
        s.setName(rs.getString("name"));
        s.setDob(rs.getDate("dob").toLocalDate());
        s.setEmail(rs.getString("email"));
        s.setSex("1".equals(rs.getString("sex")));
        s.setPhone(rs.getString("phone"));
        s.setRole(Student.Role.valueOf(rs.getString("role")));
        s.setPassword(rs.getString("password"));
        Date createAt = rs.getDate("create_at");
        s.setCreateAt(createAt != null ? createAt.toLocalDate() : null);
        return s;
    }
}
