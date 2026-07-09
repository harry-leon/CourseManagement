DROP TABLE IF EXISTS enrollment;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS student;
DROP TYPE IF EXISTS user_role;
DROP TYPE IF EXISTS enrollment_status;

CREATE TYPE user_role AS ENUM ('ADMIN', 'STUDENT');
CREATE TYPE enrollment_status AS ENUM ('WAITING', 'DENIED', 'CANCER', 'CONFIRM');

CREATE TABLE student (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    sex BIT(1) NOT NULL,
    phone VARCHAR(20),
    role user_role NOT NULL DEFAULT 'STUDENT',
    password VARCHAR(255) NOT NULL,
    create_at DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    duration INT NOT NULL,
    instructor VARCHAR(100) NOT NULL,
    create_at DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE enrollment (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL REFERENCES student(id) ON DELETE CASCADE,
    course_id INT NOT NULL REFERENCES course(id) ON DELETE CASCADE,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status enrollment_status NOT NULL DEFAULT 'WAITING',
    UNIQUE (student_id, course_id)
);

INSERT INTO student (name, dob, email, sex, phone, role, password)
VALUES ('Quản trị viên', '1990-01-01', 'admin@example.com', B'1', '0900000000', 'ADMIN',
        '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK');

INSERT INTO student (name, dob, email, sex, phone, role, password) VALUES
('Nguyễn Thị Bảo Châu',  '2001-03-15', 'baochau@gmail.com',     B'0', '0901234561', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Trần Minh Khoa',        '2000-07-22', 'minhkhoa@gmail.com',    B'1', '0912345672', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Lê Thị Thu Hà',         '2002-11-05', 'thuha@gmail.com',       B'0', '0923456783', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Phạm Quốc Hùng',        '1999-01-30', 'quochung@gmail.com',    B'1', '0934567894', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Hoàng Thị Lan Anh',     '2001-09-18', 'lananh@gmail.com',      B'0', '0945678905', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Võ Văn Tài',            '2000-04-25', 'vovantai@gmail.com',    B'1', '0956789016', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Đặng Thị Mỹ Linh',      '2002-06-12', 'mylinh@gmail.com',      B'0', '0967890127', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Bùi Đức Thịnh',         '1998-12-03', 'ducthinh@gmail.com',    B'1', '0978901238', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Ngô Thị Hồng Nhung',    '2001-08-20', 'hongnhung@gmail.com',   B'0', '0989012349', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Dương Văn Long',         '2000-02-14', 'vanlong@gmail.com',     B'1', '0990123450', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Trịnh Thị Cẩm Tú',      '2003-05-08', 'camtu@gmail.com',       B'0', '0901112233', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Nguyễn Văn Đạt',        '2001-10-17', 'nguyenvdat@gmail.com',  B'1', '0912223344', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Phan Thị Ngọc Mai',     '2000-08-25', 'ngocmai@gmail.com',     B'0', '0923334455', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Lý Minh Quân',          '1999-12-11', 'minhquan@gmail.com',    B'1', '0934445566', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Vũ Thị Thu Thảo',       '2002-04-02', 'thuthao@gmail.com',     B'0', '0945556677', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Đinh Công Sơn',         '2001-07-19', 'congson@gmail.com',     B'1', '0956667788', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Trần Thị Hương Giang',  '2000-01-28', 'huonggiang@gmail.com',  B'0', '0967778899', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Nguyễn Bá Thành',       '1998-06-14', 'bathanh@gmail.com',     B'1', '0978889900', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Lê Thị Phương Uyên',    '2003-09-30', 'phuonguyen@gmail.com',  B'0', '0989990011', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK'),
('Mai Văn Khải',           '2000-11-06', 'vanakhai@gmail.com',    B'1', '0990001122', 'STUDENT', '$2a$10$bWc6aShlvr2WIfnMXJx7TOHiaKeLqaYCAr03duZRTcn/a1GIpL9vK');

INSERT INTO course (name, duration, instructor) VALUES
('Java Core',                40, 'Nguyễn Văn Bình'),
('PostgreSQL cơ bản',        20, 'Trần Thị Cẩm'),
('Spring Boot nâng cao',     60, 'Lê Văn Đức'),
('HTML & CSS cơ bản',        30, 'Nguyễn Thị Hoa'),
('JavaScript nâng cao',      50, 'Trần Văn Phúc'),
('ReactJS thực chiến',       60, 'Lê Minh Tuấn'),
('Python for Data Science',  45, 'Phạm Thị Lan'),
('Docker & Kubernetes',      35, 'Hoàng Văn Nam'),
('Git & GitHub',             15, 'Võ Thị Mai'),
('RESTful API Design',       25, 'Đặng Quốc Việt'),
('NodeJS & Express',         40, 'Bùi Văn Hải'),
('TypeScript cơ bản',        20, 'Ngô Thị Xuân'),
('Flutter Mobile App',       55, 'Đinh Văn Phong'),
('AWS Cloud Fundamentals',   30, 'Trịnh Minh Hiếu'),
('Clean Code & Design Patterns', 25, 'Phan Thị Bích');
