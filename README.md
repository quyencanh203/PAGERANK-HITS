# PageRank và HITS - Dự án Big Data

## Giới thiệu
Dự án này triển khai hai thuật toán quan trọng trong lĩnh vực xử lý dữ liệu lớn:
- **PageRank**: Xếp hạng các trang web dựa trên cấu trúc liên kết của mạng.
- **HITS (Hyperlink-Induced Topic Search)**: Phân loại các trang web thành các trang authority và hub dựa trên liên kết.

Dự án sử dụng Apache Hadoop để xử lý dữ liệu lớn và Maven để quản lý dự án Java.

---

## Yêu cầu
- **Hadoop**: Phiên bản 3.4.0 hoặc cao hơn.
- **Java**: Phiên bản 8 hoặc cao hơn.
- **Maven**: Để build dự án.
- **Hệ thống HDFS**: Để lưu trữ dữ liệu đầu vào và đầu ra.

---

## Cấu trúc Dự án
- `src/main/java/com/example/PageRank`: Chứa mã nguồn của thuật toán PageRank.
- `src/main/java/com/example/HITS`: Chứa mã nguồn của thuật toán HITS.
- `input/data.txt`: Tệp dữ liệu đầu vào chứa thông tin đồ thị (các liên kết giữa các nút).
- `output/`: Thư mục lưu kết quả đầu ra sau khi chạy.

---

## Hướng dẫn sử dụng

### 1. Chuẩn bị dữ liệu
- Đảm bảo rằng tệp dữ liệu đầu vào có định dạng chính xác và đặt trong thư mục `input/` với tên là `data.txt`.

### 2. Chạy thuật toán PageRank
Chạy lệnh sau trong terminal:
```bash
./run_pagerank.sh
```

Lệnh trên sẽ:
- Biên dịch mã nguồn bằng Maven.
- Tải dữ liệu đầu vào từ HDFS.
- Thực thi thuật toán PageRank trên Hadoop.
- Lưu kết quả đầu ra vào thư mục `output/pagerank/` trên HDFS.

**Ghi chú**: Tệp `run_pagerank.sh` cần được thiết lập quyền thực thi. Nếu chưa thiết lập, chạy lệnh sau:
```bash
chmod +x run_pagerank.sh
```

### 3. Chạy thuật toán HITS
Chạy lệnh sau trong terminal:
```bash
./run_hits.sh
```

Lệnh trên sẽ:
- Biên dịch mã nguồn bằng Maven.
- Tải dữ liệu đầu vào từ HDFS.
- Thực thi thuật toán HITS trên Hadoop.
- Lưu kết quả đầu ra vào thư mục `output/hits/` trên HDFS.

**Ghi chú**: Tương tự, cần thiết lập quyền thực thi cho tệp `run_hits.sh`:
```bash
chmod +x run_hits.sh
```

---

## Nội dung tệp `run_pagerank.sh`
Dưới đây là nội dung mẫu của tệp `run_pagerank.sh`:
```bash
#!/bin/bash
# Biên dịch dự án bằng Maven
mvn clean package

# Đưa tệp đầu vào vào HDFS
hdfs dfs -mkdir -p /user/input
hdfs dfs -put -f input/data.txt /user/input/

# Chạy PageRank trên Hadoop
hadoop jar target/bigdata-project-1.0.jar com.example.PageRank /user/input/data.txt /user/output/pagerank

# Hiển thị kết quả
hdfs dfs -cat /user/output/pagerank/part-00000
```

---

## Nội dung tệp `run_hits.sh`
Dưới đây là nội dung mẫu của tệp `run_hits.sh`:
```bash
#!/bin/bash
# Biên dịch dự án bằng Maven
mvn clean package

# Đưa tệp đầu vào vào HDFS
hdfs dfs -mkdir -p /user/input
hdfs dfs -put -f input/data.txt /user/input/

# Chạy HITS trên Hadoop
hadoop jar target/bigdata-project-1.0.jar com.example.HITS /user/input/data.txt /user/output/hits

# Hiển thị kết quả
hdfs dfs -cat /user/output/hits/part-00000
```

---

## Lưu ý
- Kiểm tra các đường dẫn trong mã để đảm bảo chúng phù hợp với cấu hình HDFS của bạn.
- Nếu gặp lỗi, kiểm tra nhật ký (`logs`) của Hadoop để xác định và sửa lỗi.

---

## Tài liệu tham khảo
- [Apache Hadoop Documentation](https://hadoop.apache.org/)
- [Apache Maven Documentation](https://maven.apache.org/)
- [Thuật toán PageRank](https://en.wikipedia.org/wiki/PageRank)
- [Thuật toán HITS](https://en.wikipedia.org/wiki/HITS_algorithm)
