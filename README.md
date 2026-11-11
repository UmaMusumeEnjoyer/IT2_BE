# IT2 Backend — Cổng Dịch vụ và Hỗ trợ Sinh viên DUT

Hệ thống Backend viết bằng **Java Spring Boot 3** và **PostgreSQL (pgvector)** phục vụ cho Cổng Dịch vụ và Hỗ trợ Sinh viên Trường Đại học Bách khoa — Đại học Đà Nẵng.

---

## 1. Yêu cầu Môi trường

- **Cách 1 (Khuyên dùng cho nhóm):** Cài đặt [Docker Desktop](https://www.docker.com/).
- **Cách 2 (Chạy trực tiếp trên máy):**
  - Java JDK 21 hoặc 23
  - Apache Maven 3.8+
  - PostgreSQL 16+

---

## 2. Hướng dẫn Khởi chạy nhanh bằng Docker (Dành cho Thành viên Nhóm)

1. Sao chép file cấu hình môi trường:
   ```bash
   cp .env.example .env
   ```

2. Khởi động toàn bộ Database, Backend và pgAdmin:
   ```bash
   docker compose up --build -d
   ```

3. Kiểm tra các dịch vụ đang chạy:
   - **Backend API & Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
   - **PostgreSQL Database**: `localhost:5432` (User: `postgres`, Pass: `postgres_dut_secret`, DB: `it2_db`)
   - **pgAdmin (Quản trị DB trực quan)**: [http://localhost:5050](http://localhost:5050) (Login: `admin@dut.udn.vn` / `admin`)

4. Dừng hệ thống:
   ```bash
   docker compose down
   ```

---

## 3. Hướng dẫn Khởi chạy cục bộ (Local Development không qua Docker Backend)

Nếu bạn muốn chạy Spring Boot trực tiếp từ IntelliJ IDEA hoặc VS Code nhưng vẫn muốn dùng Database trên Docker:

1. Chỉ chạy riêng Database PostgreSQL:
   ```bash
   docker compose up -d postgres
   ```

2. Chạy ứng dụng Spring Boot:
   ```bash
   mvn clean spring-boot:run
   ```

---

## 4. Danh mục API chính (RESTful)

- `GET /api/students/me` — Lấy thông tin cá nhân hóa sinh viên (Academic Status Strip).
- `GET /api/regulations` — Tìm kiếm và lọc danh mục văn bản quy chế, pháp quy.
- `GET /api/regulations/{id}` — Lấy chi tiết văn bản chuẩn hóa.
- `GET /api/procedures/criteria` — Bảng tiêu chuẩn phân hạng học bổng/thủ tục.
- `GET /api/procedures/evaluate/{studentCode}` — Tự động so khớp hồ sơ điểm số sinh viên với tiêu chuẩn.
- `POST /api/procedures/tickets` — Nộp phản hồi sai lệch, tự động sinh mã biên nhận `DUT-HB-xxxx`.
- `POST /api/ai-chat/query` — Hỏi đáp quy chế học vụ với AI, trả lời kèm trích dẫn văn bản pháp quy.
- `GET /api/events` — Lịch sự kiện và hạn chót cá nhân hóa theo Khoa/Lớp.
