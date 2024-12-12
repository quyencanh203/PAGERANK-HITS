# PageRank và HITS - Dự án Big Data

## Giới thiệu
Dự án này triển khai hai thuật toán quan trọng trong lĩnh vực xử lý dữ liệu lớn:
- **PageRank**: Xếp hạng các trang web dựa trên cấu trúc liên kết của mạng.
- **HITS (Hyperlink-Induced Topic Search)**: Phân loại các trang web thành các trang authority và hub dựa trên liên kết.

Dự án sử dụng Apache Hadoop để xử lý dữ liệu lớn và Maven để quản lý dự án Java.

---

## Yêu cầu
- **Hadoop**: Phiên bản 3.4.0 hoặc cao hơn.
- **Java**: Phiên bản 8 hoặc cao hơn (trong dự án này là java 11).
- **Maven**: Để build dự án.
- **Hệ thống HDFS**: Để lưu trữ dữ liệu đầu vào và đầu ra.

---

## Cấu trúc Dự án
- `PAGERANK/src/main/java/com/example/demo`: Chứa mã nguồn của thuật toán PageRank.
- `HITS/src/main/java/com/example/demo`: Chứa mã nguồn của thuật toán HITS.
- `input/data.txt`: Tệp dữ liệu đầu vào chứa thông tin đồ thị (các liên kết giữa các nút).
- `output/`: Thư mục lưu kết quả đầu ra sau khi chạy.

---

## Hướng dẫn sử dụng

### 1. Chuẩn bị dữ liệu
- Đảm bảo rằng tệp dữ liệu đầu vào có định dạng chính xác và đặt trong thư mục `input/` với tên là `data.txt`.
- Dữ liệu lớn https://www.limfinity.com/ir/

### 2. Chạy thuật toán PageRank
Chạy lệnh sau trong terminal:
```bash
./run.sh (trong thư mục code của PageRank)
```

Lệnh trên sẽ:
- Biên dịch mã nguồn bằng Maven.
- Tải dữ liệu đầu vào từ HDFS.
- Thực thi thuật toán PageRank trên Hadoop.
- Lưu kết quả đầu ra vào thư mục `output` trên HDFS.

**Ghi chú**: Tệp `run.sh` cần được thiết lập quyền thực thi. Nếu chưa thiết lập, chạy lệnh sau:
```bash
chmod +x run.sh
```

### 3. Chạy thuật toán HITS
Chạy lệnh sau trong terminal:
```bash
./run.sh (trong thư mục code của Hits)
```

Lệnh trên sẽ:
- Biên dịch mã nguồn bằng Maven.
- Tải dữ liệu đầu vào từ HDFS.
- Thực thi thuật toán HITS trên Hadoop.
- Lưu kết quả đầu ra vào thư mục `output` trên HDFS.

**Ghi chú**: Tương tự, cần thiết lập quyền thực thi cho tệp `run.sh`:
```bash
chmod +x run.sh
```
### 4. đóng góp bởi.
- [quyencanh](https://github.com/quyencanh203)
- [ngocnhat]()
- [quanpham]()
- [huydinh]()

## Tài liệu tham khảo
- [Apache Hadoop Documentation](https://hadoop.apache.org/)
- [Apache Maven Documentation](https://maven.apache.org/)
- [Thuật toán PageRank](https://en.wikipedia.org/wiki/PageRank)
- [Thuật toán HITS](https://en.wikipedia.org/wiki/HITS_algorithm)
