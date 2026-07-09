package dao.impl;

import dao.ICourseDAO;
import model.Course;
import utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements ICourseDAO {

    @Override
    public List<Course> findAll() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn findAll course: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Course> findAll(int offset, int limit) {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course ORDER BY id LIMIT ? OFFSET ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn findAll(page) course: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM course";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi đếm course: " + e.getMessage(), e);
        }
    }

    @Override
    public Course findById(int id) {
        String sql = "SELECT * FROM course WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn findById course: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean insert(Course c) {
        String sql = "INSERT INTO course (name, duration, instructor) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getDuration());
            ps.setString(3, c.getInstructor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi thêm khóa học: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Course c) {
        String sql = "UPDATE course SET name = ?, duration = ?, instructor = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getDuration());
            ps.setString(3, c.getInstructor());
            ps.setInt(4, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi cập nhật khóa học: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM course WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi xóa khóa học: " + e.getMessage(), e);
        }
    }

    private Course mapRow(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setDuration(rs.getInt("duration"));
        c.setInstructor(rs.getString("instructor"));
        Date createAt = rs.getDate("create_at");
        c.setCreateAt(createAt != null ? createAt.toLocalDate() : null);
        return c;
    }
}
