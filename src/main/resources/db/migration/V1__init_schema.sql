-- ==============================================================
-- DATABASE INITIALIZATION SCRIPT - PORTAL SINH VIEN DUT (IT2)
-- ==============================================================

-- 1. Bảng Sinh viên (Student Profile)
CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    student_code VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    class_name VARCHAR(100) NOT NULL,
    faculty VARCHAR(255) NOT NULL,
    gpa NUMERIC(4, 2) DEFAULT 0.00,
    training_point INTEGER DEFAULT 0,
    accumulated_credits INTEGER DEFAULT 0,
    total_credits INTEGER DEFAULT 135,
    email VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Bảng Văn bản Quy chế (Regulations & Legal Documents)
CREATE TABLE IF NOT EXISTS regulations (
    id BIGSERIAL PRIMARY KEY,
    document_number VARCHAR(100) NOT NULL,
    title TEXT NOT NULL,
    category VARCHAR(100) NOT NULL, -- HOC_BONG, HOC_VU, THU_TUC_MOT_CUA
    issuer VARCHAR(255) NOT NULL,
    issued_date DATE,
    effective_date DATE,
    pdf_url VARCHAR(500),
    summary TEXT,
    content_html TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Bảng Tiêu chuẩn Học bổng / Học vụ (Procedure Criteria)
CREATE TABLE IF NOT EXISTS procedure_criteria (
    id BIGSERIAL PRIMARY KEY,
    procedure_name VARCHAR(255) NOT NULL,
    grade_level VARCHAR(50) NOT NULL, -- LOAI_XUAT_SAC, LOAI_GIOI, LOAI_KHA
    min_gpa NUMERIC(4, 2) NOT NULL,
    max_gpa NUMERIC(4, 2),
    min_training_point INTEGER NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Bảng Đơn phản hồi / Khiếu nại (Procedure Tickets)
CREATE TABLE IF NOT EXISTS procedure_tickets (
    id BIGSERIAL PRIMARY KEY,
    ticket_code VARCHAR(100) UNIQUE NOT NULL, -- e.g. DUT-HB-23102
    student_code VARCHAR(50) NOT NULL,
    procedure_name VARCHAR(255) NOT NULL,
    reason TEXT NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING', -- PENDING, IN_PROGRESS, RESOLVED, REJECTED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Bảng Sự kiện & Hạn chót học vụ (Academic Events & Deadlines)
CREATE TABLE IF NOT EXISTS academic_events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    event_type VARCHAR(100) NOT NULL, -- HOC_BONG, HOC_VU, HOC_PHI, SU_KIEN_CHUNG
    event_date TIMESTAMP NOT NULL,
    is_deadline BOOLEAN DEFAULT FALSE,
    faculty_scope VARCHAR(255) DEFAULT 'ALL',
    class_scope VARCHAR(100) DEFAULT 'ALL',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================================
-- SEED DATA (DỮ LIỆU MẪU THEO BẢN MVP DUT)
-- ==============================================================

-- Sinh viên mẫu
INSERT INTO students (student_code, full_name, class_name, faculty, gpa, training_point, accumulated_credits, total_credits, email)
VALUES ('102230001', 'Nguyễn Văn An', '23T_DT1', 'Khoa Công nghệ Thông tin', 8.35, 86, 48, 135, '102230001@sv.dut.udn.vn')
ON CONFLICT (student_code) DO NOTHING;

-- Văn bản mẫu
INSERT INTO regulations (document_number, title, category, issuer, issued_date, pdf_url, summary)
VALUES 
('1414/QĐ-ĐHBK', 'Quy định về xét, cấp học bổng khuyến khích học tập cho sinh viên hệ chính quy tại Trường ĐHBK - ĐHĐN', 'HOC_BONG', 'Trường Đại học Bách khoa - ĐHĐN', '2022-10-15', '/tai-lieu-riel/QD_1414_DHBK.pdf', 'Quy định chi tiết điều kiện, tiêu chuẩn và mức cấp học bổng KKHT cho sinh viên hệ ĐH chính quy các khóa.'),
('28/VBHN-BGDĐT', 'Văn bản hợp nhất quy định về học bổng khuyến khích học tập đối với học sinh, sinh viên trong các cơ sở giáo dục', 'HOC_BONG', 'Bộ Giáo dục & Đào tạo', '2014-12-31', '/tai-lieu-riel/VBHN_28_BGDDT.pdf', 'Quy chế chung của Bộ GD&ĐT về định mức và nguồn kinh phí cấp học bổng.'),
('35/2011/TT-BGDĐT', 'Thông tư quy định về việc trao nhận học bổng cho sinh viên các trường đại học, cao đẳng', 'HOC_BONG', 'Bộ Giáo dục & Đào tạo', '2011-08-11', '/tai-lieu-riel/TT_35_2011_BGDDT.pdf', 'Hướng dẫn việc tổ chức xét duyệt và trao học bổng tài trợ, khuyến khích học tập.')
ON CONFLICT DO NOTHING;

-- Tiêu chuẩn học bổng QĐ 1414
INSERT INTO procedure_criteria (procedure_name, grade_level, min_gpa, max_gpa, min_training_point, description)
VALUES 
('Học bổng Khuyến khích Học tập', 'LOAI_XUAT_SAC', 9.00, 10.00, 90, 'Học bổng Loại Xuất sắc: ĐTBCHK >= 9.00 và ĐRL >= 90 điểm (100% học phí hoặc cao hơn).'),
('Học bổng Khuyến khích Học tập', 'LOAI_GIOI', 8.00, 8.99, 80, 'Học bổng Loại Giỏi: ĐTBCHK 8.00 - 8.99 và ĐRL >= 80 điểm.'),
('Học bổng Khuyến khích Học tập', 'LOAI_KHA', 7.00, 7.99, 65, 'Học bổng Loại Khá: ĐTBCHK 7.00 - 7.99 và ĐRL >= 65 điểm.')
ON CONFLICT DO NOTHING;

-- Mốc hạn chót mẫu
INSERT INTO academic_events (title, event_type, event_date, is_deadline, faculty_scope, class_scope, description)
VALUES 
('Hạn chót phản hồi sai lệch danh sách Học bổng KKHT kỳ 1', 'HOC_BONG', NOW() + INTERVAL '5 days', TRUE, 'Khoa Công nghệ Thông tin', '23T_DT1', 'Sinh viên kiểm tra điểm TBCHK và ĐRL dự kiến, nộp đơn khiếu nại qua cổng nếu có sai sót.'),
('Hạn rút bớt học phần tín chỉ đợt 2', 'HOC_VU', NOW() + INTERVAL '12 days', TRUE, 'ALL', 'ALL', 'Hạn cuối nộp đơn hủy/rút bớt học phần tại phòng Đào tạo không ghi điểm F.'),
('Hạn nộp học phí học kỳ hiện tại', 'HOC_PHI', NOW() + INTERVAL '20 days', TRUE, 'ALL', 'ALL', 'Hạn cuối đóng học phí qua cổng thanh toán sinh viên sv.dut.udn.vn.')
ON CONFLICT DO NOTHING;
