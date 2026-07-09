package business.impl;

import business.ICourseService;
import dao.ICourseDAO;
import dao.impl.CourseDAOImpl;
import model.Course;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CourseServiceImpl implements ICourseService {
    private final ICourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public List<Course> getAll() {
        return courseDAO.findAll();
    }

    @Override
    public List<Course> getAll(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return courseDAO.findAll(offset, pageSize);
    }

    @Override
    public int countAll() {
        return courseDAO.count();
    }

    @Override
    public Course getById(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public boolean add(String name, int duration, String instructor) {
        if (name == null || name.trim().isEmpty()) return false;
        if (duration <= 0) return false;
        if (instructor == null || instructor.trim().isEmpty()) return false;
        return courseDAO.insert(new Course(name.trim(), duration, instructor.trim()));
    }

    @Override
    public boolean update(Course course) {
        return courseDAO.update(course);
    }

    @Override
    public boolean delete(int id) {
        return courseDAO.deleteById(id);
    }

    @Override
    public List<Course> searchByName(String keyword) {
        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        return courseDAO.findAll().stream().filter(c -> c.getName().toLowerCase().contains(kw)).collect(Collectors.toList());
    }

    @Override
    public List<Course> sort(String field, boolean asc) {
        Comparator<Course> comparator = "name".equalsIgnoreCase(field) ? Comparator.comparing(Course::getName, String.CASE_INSENSITIVE_ORDER) : Comparator.comparingInt(Course::getId);
        if (!asc) {
            comparator = comparator.reversed();
        }
        return courseDAO.findAll().stream().sorted(comparator).collect(Collectors.toList());
    }
}
