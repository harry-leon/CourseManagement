package dao.impl;

import dao.IEnrollmentDAO;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAOImpl implements IEnrollmentDAO {

    @Override
    public boolean exists(int studentId, int courseId) {
        String sql = "SELECT 1 FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi kiểm tra đăng ký trùng: " + e.getMessage(), e);
        }
    }

    @Override
    public String findStatus(int studentId, int courseId) {
        String sql = "SELECT status FROM enrollment WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy trạng thái đăng ký: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean insert(int studentId, int courseId) {
        String sql = "INSERT INTO enrollment (student_id, course_id) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đăng ký khóa học: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteIfWaiting(int studentId, int courseId) {
        String sql = "DELETE FROM enrollment WHERE student_id = ? AND course_id = ? AND status = 'WAITING'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi hủy đăng ký: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Object[]> findCoursesByStudent(int studentId) {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT c.id, c.name, c.duration, c.instructor, e.registered_at, e.status " +
                "FROM enrollment e JOIN course c ON e.course_id = c.id " +
                "WHERE e.student_id = ? ORDER BY e.registered_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(new Object[]{
                        rs.getInt(1), rs.getString(2), rs.getInt(3),
                        rs.getString(4), rs.getTimestamp(5), rs.getString(6)});
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy danh sách khóa đã đăng ký: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Object[]> findCoursesByStudent(int studentId, int offset, int limit) {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT c.id, c.name, c.duration, c.instructor, e.registered_at, e.status " +
                "FROM enrollment e JOIN course c ON e.course_id = c.id " +
                "WHERE e.student_id = ? ORDER BY e.registered_at DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(new Object[]{
                        rs.getInt(1), rs.getString(2), rs.getInt(3),
                        rs.getString(4), rs.getTimestamp(5), rs.getString(6)});
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn findCoursesByStudent(page): " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public int countByStudent(int studentId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE student_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đếm enrollment by student: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Object[]> findByCourse(int courseId) {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT e.id, s.id, s.name, e.registered_at, e.status " +
                "FROM enrollment e JOIN student s ON e.student_id = s.id " +
                "WHERE e.course_id = ? ORDER BY e.registered_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(new Object[]{
                        rs.getInt(1), rs.getInt(2), rs.getString(3),
                        rs.getTimestamp(4), rs.getString(5)});
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy danh sách đăng ký theo khóa: " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public List<Object[]> findByCourse(int courseId, int offset, int limit) {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT e.id, s.id, s.name, e.registered_at, e.status " +
                "FROM enrollment e JOIN student s ON e.student_id = s.id " +
                "WHERE e.course_id = ? ORDER BY e.registered_at DESC LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(new Object[]{
                        rs.getInt(1), rs.getInt(2), rs.getString(3),
                        rs.getTimestamp(4), rs.getString(5)});
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn findByCourse(page): " + e.getMessage(), e);
        }
        return result;
    }

    @Override
    public int countByCourse(int courseId) {
        String sql = "SELECT COUNT(*) FROM enrollment WHERE course_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đếm enrollment by course: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateStatus(int studentId, int courseId, String status) {
        String sql = "UPDATE enrollment SET status = ?::enrollment_status WHERE student_id = ? AND course_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, studentId);
            ps.setInt(3, courseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật trạng thái đăng ký: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(int enrollmentId) {
        String sql = "DELETE FROM enrollment WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa đăng ký: " + e.getMessage(), e);
        }
    }
}
