# 🎟️ HỆ THỐNG BÁN VÉ SỰ KIỆN TRỰC TUYẾN - KIẾN TRÚc MICROSERVICES

## 📖 Mục lục

1. [🦩 Mô tả tổng quan](#-mô-tả-tổng-quan)
2. [👥 Đối tượng sử dụng](#-đối-tượng-sử-dụng)
3. [✨ Chức năng chính](#-ăc-năng-chính)
4. [🏗️ Kiến trúc hệ thống](#-kiến-trúc-hệ-thống)
5. [⚙️ Công nghệ sử dụng](#-ông-nghệ-sử-dụng)
6. [🧠 CQRS + Axon Framework](#-cqrs--axon-framework)
7. [🚀 Redis Cache Flow](#-redis-cache-flow)
8. [⚓️ Triển khai trên Kubernetes & HPA](#-triển-khai-trên-kubernetes--hpa)
9. [🧪 Kiểm thử hiệu năng với K6](#-ểm-thử-hiệu-năng-với-k6)
10. [📌 Định hướng production](#-định-hướng-production)

---

## 🦩 Mô tả tổng quan

Hệ thống bán vé sự kiện là nền tảng giúp người dùng **xem - chọn - đặt vé** cho các sự kiện trực tuyến mà không cần đến trực tiếp địa điểm.  
Hệ thống được xây dựng theo kiến trúc **Microservices**, hỗ trợ **scale linh hoạt**, **chịu tải cao**, dễ triển khai trên **Kubernetes**.

> 👉 Tham khảo giao diện thực tế: [ticketbox.vn](https://ticketbox.vn/)

---

## 👥 Đối tượng sử dụng

- **Người dùng**: Xem, đặt vé nhanh chóng, tiện lợi.
- **Ban tổ chức / Admin**: Quản lý sự kiện, theo dõi doanh thu, đơn hàng.

---

## ✨ Chức năng chính

### Đối với người dùng:

- Duyệt danh sách sự kiện.
- Nhận thông báo & quản lý vé.

### Đối với admin:

- CRUD sự kiện & loại vé.
- Quản lý doanh thu, đơn đặt.

---

## 🏗️ Kiến trúc hệ thống

Hệ thống bao gồm nhiều **microservices**, giao tiếp qua **Kafka** hoặc **Axon Server**.

### Tổng thể hệ thống:

![Tổng thể hệ thống](../images/architecture/overview.png)

| Thành phần              | Mô tả                                 |
| ----------------------- | ------------------------------------- |
| **API Gateway**         | Truy cập, định tuyến, xác thực (JWT). |
| **Eureka Discovery**    | Service Registry.                     |
| **Kafka / Axon Server** | Messaging/Event Bus.                  |
| **Redis**               | Caching dữ liệu.                      |
| **MySQL**               | Cơ sở dữ liệu chính.                  |

---

## ⚙️ Công nghệ sử dụng

| Thành phần          | Công nghệ                      |
| ------------------- | ------------------------------ |
| API Gateway         | Spring Cloud Gateway           |
| Service Discovery   | Eureka                         |
| Authentication      | Spring Security + JWT          |
| Messaging           | Kafka / Axon Server            |
| Database            | MySQL                          |
| Caching             | Redis                          |
| CI/CD               | GitHub Actions                 |
| Triển khai          | Docker, Kubernetes             |
| Monitoring          | Prometheus + Grafana (dự kiến) |
| Performance Testing | K6                             |

---

## 🧠 CQRS + Axon Framework

Các service sử dụng **CQRS** + **Event Sourcing** với Axon Framework, giúp phân tách rõ **Command** và **Query**.

![CQRS - Axon](../images/architecture/cqrs-axon.png)

---

## 🚀 Redis Cache Flow

Áp dụng cache theo pattern:

- Khi GET: Lấy từ Redis.
- Nếu miss → truy vấn DB → ghi lại Redis.

![Redis cache flow](../images/architecture/redis-cache-flow.png)

---

## ⚓️ Triển khai trên Kubernetes & HPA

### 🪡 Quá trình triển khai

- Mỗi service là 1 `Deployment` trong K8s.
- Expose qua `Service`, route qua `Ingress`.
- Cấu hình động với `ConfigMap`, `Secrets`.

![Triển khai trên Kubernetes](./docs/images/k8s/k8s-deploy.png)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: auth-service
spec:
  replicas: 1
  selector:
    matchLabels:
      app: auth
  template:
    metadata:
      labels:
        app: auth
    spec:
      containers:
        - name: auth
          image: your-repo/auth-service:latest
```

---

### 📊 HPA - Horizontal Pod Autoscaler

HPA giúp scale tự động pod theo CPU hoặc custom metrics.

![HPA Scaling](./docs/images/k8s/hpa-scaling.png)

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: auth-service-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: auth-service
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 60
```

> 📈 Khi CPU > 60%, pod được scale lên. Khi tải nhẹ, pod tự scale xuống.

---

## 🧪 Kiểm thử hiệu năng với K6

- Kiểm thử tải hệ thống với **K6**.
- Mô phỏng người dùng truy cập vào API.
- Kết hợp với **Prometheus** + **Grafana** để theo dõi hiệu suất và quá trình autoscale.

![K6 Testing](./docs/images/test/k6-testing.png)


