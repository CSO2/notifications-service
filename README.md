# 🔔 Notifications Service

> Multi-channel notification system for the CS02 E-Commerce Platform

## 📋 Overview

The Notifications Service handles all notification delivery across multiple channels including email, in-app notifications, push notifications (Firebase), and SMS (Twilio). It consumes events from Kafka and delivers notifications based on user preferences.

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 4.0.0 |
| Database | MongoDB | Latest |
| Message Queue | Apache Kafka | Latest |
| Email | Spring Mail / JavaMail | Latest |
| Push Notifications | Firebase Admin SDK | 9.2.0 |
| SMS | Twilio SDK | 9.14.0 |
| Template Engine | Thymeleaf | Latest |
| Build Tool | Maven | 3.x |

## 🚀 API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/notifications` | Yes | Get user's notifications |
| `GET` | `/api/notifications/unread` | Yes | Get unread notifications count |
| `PUT` | `/api/notifications/{id}/read` | Yes | Mark notification as read |
| `PUT` | `/api/notifications/read-all` | Yes | Mark all notifications as read |
| `DELETE` | `/api/notifications/{id}` | Yes | Delete a notification |
| `GET` | `/api/notifications/preferences` | Yes | Get notification preferences |
| `PUT` | `/api/notifications/preferences` | Yes | Update notification preferences |

## 📬 Notification Channels

### 1. In-App Notifications
- Stored in MongoDB
- Retrieved via REST API
- Real-time updates via polling

### 2. Email Notifications
- SMTP/Gmail integration
- HTML email templates (Thymeleaf)
- Support for attachments

### 3. Push Notifications (Firebase)
- Firebase Cloud Messaging (FCM)
- Mobile and web push support
- Topic-based broadcasting

### 4. SMS Notifications (Twilio)
- Twilio SMS API
- Order confirmations
- Critical alerts

## 📊 Data Models

### Notification

```java
{
  "id": "string",
  "userId": "string",
  "type": "ORDER_CONFIRMATION | SHIPPING_UPDATE | STOCK_ALERT | PROMOTIONAL",
  "title": "string",
  "message": "string",
  "data": { },
  "channel": "IN_APP | EMAIL | PUSH | SMS",
  "read": boolean,
  "createdAt": "datetime"
}
```

### NotificationLog

```java
{
  "id": "string",
  "notificationId": "string",
  "channel": "string",
  "status": "SENT | FAILED | PENDING",
  "sentAt": "datetime",
  "errorMessage": "string"
}
```

### NotificationPreferences

```java
{
  "userId": "string",
  "emailEnabled": boolean,
  "pushEnabled": boolean,
  "smsEnabled": boolean,
  "orderUpdates": boolean,
  "promotions": boolean,
  "stockAlerts": boolean
}
```

## 🔧 Configuration

### Application Properties

```yaml
server:
  port: 8087

spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/CSO2_notifications_service
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: notifications-group
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}

twilio:
  account-sid: ${TWILIO_ACCOUNT_SID}
  auth-token: ${TWILIO_AUTH_TOKEN}
  phone-number: ${TWILIO_PHONE_NUMBER}

firebase:
  credentials-path: ${FIREBASE_CREDENTIALS_PATH}
```

### Environment Variables

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `SPRING_DATA_MONGODB_URI` | No | `mongodb://localhost:27017` | MongoDB URI |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | No | `localhost:9092` | Kafka brokers |
| `MAIL_USERNAME` | Yes* | - | SMTP username |
| `MAIL_PASSWORD` | Yes* | - | SMTP password/app password |
| `TWILIO_ACCOUNT_SID` | No | - | Twilio account SID |
| `TWILIO_AUTH_TOKEN` | No | - | Twilio auth token |
| `TWILIO_PHONE_NUMBER` | No | - | Twilio phone number |
| `FIREBASE_CREDENTIALS_PATH` | No | - | Path to Firebase credentials JSON |
| `SERVER_PORT` | No | `8087` | Service port |

## 📦 Kafka Events Consumed

| Topic | Event | Action |
|-------|-------|--------|
| `order-events` | `ORDER_CREATED` | Send order confirmation |
| `order-events` | `ORDER_SHIPPED` | Send shipping notification |
| `order-events` | `ORDER_DELIVERED` | Send delivery notification |
| `stock-events` | `LOW_STOCK` | Alert admin users |
| `stock-events` | `BACK_IN_STOCK` | Notify wishlisted users |
| `user-events` | `USER_REGISTERED` | Send welcome email |

## 📦 Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>com.twilio.sdk</groupId>
        <artifactId>twilio</artifactId>
        <version>9.14.0</version>
    </dependency>
    <dependency>
        <groupId>com.google.firebase</groupId>
        <artifactId>firebase-admin</artifactId>
        <version>9.2.0</version>
    </dependency>
</dependencies>
```

## 🏃 Running the Service

### Local Development

```bash
cd backend/notifications-service

# Using Maven Wrapper
./mvnw spring-boot:run

# Or with Maven
mvn spring-boot:run
```

### Docker

```bash
cd backend/notifications-service

# Build JAR
./mvnw clean package -DskipTests

# Build Docker image
docker build -t cs02/notifications-service .

# Run container
docker run -p 8087:8087 \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/CSO2_notifications_service \
  -e SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092 \
  -e MAIL_USERNAME=your-email@gmail.com \
  -e MAIL_PASSWORD=your-app-password \
  cs02/notifications-service
```

## 🗄️ Database Requirements

- **MongoDB** running on port `27017`
- Database: `CSO2_notifications_service`
- Collections: `notifications`, `notification_logs`, `notification_preferences`

## ✅ Features - Completion Status

| Feature | Status | Notes |
|---------|--------|-------|
| In-app notifications | ✅ Complete | CRUD operations |
| Mark as read | ✅ Complete | Single and bulk |
| Email notifications | ✅ Complete | SMTP integration |
| Push notifications (Firebase) | ✅ Complete | FCM integration |
| SMS notifications (Twilio) | ✅ Complete | Twilio SDK |
| Kafka event consumption | ✅ Complete | Event-driven |
| Notification preferences | ✅ Complete | User settings |
| HTML email templates | ⚠️ Partial | Basic templates |
| Notification history | ✅ Complete | MongoDB logging |

### **Overall Completion: 90%** ✅

## ❌ Not Implemented / Future Enhancements

| Feature | Priority | Notes |
|---------|----------|-------|
| WebSocket real-time delivery | Medium | Currently uses polling |
| Email template customization | Low | Admin-configurable templates |
| Notification scheduling | Low | Send at specific times |
| Batch notification sending | Low | Bulk promotional emails |
| Notification analytics | Low | Open rates, click tracking |
| Unsubscribe links | Medium | Email compliance |
| Retry mechanism | Medium | Failed notification retry |

## 📁 Project Structure

```
notifications-service/
├── src/
│   ├── main/
│   │   ├── java/com/cs02/notifications/
│   │   │   ├── NotificationsServiceApplication.java
│   │   │   ├── controller/
│   │   │   │   └── NotificationController.java
│   │   │   ├── model/
│   │   │   │   ├── Notification.java
│   │   │   │   ├── NotificationLog.java
│   │   │   │   └── NotificationPreferences.java
│   │   │   ├── repository/
│   │   │   │   └── NotificationRepository.java
│   │   │   ├── service/
│   │   │   │   ├── NotificationService.java
│   │   │   │   ├── EmailService.java
│   │   │   │   ├── PushNotificationService.java
│   │   │   │   └── SMSService.java
│   │   │   └── kafka/
│   │   │       └── NotificationEventConsumer.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── templates/
│   │           └── email/
│   └── test/
├── pom.xml
├── Dockerfile
└── README.md
```

## 🧪 Testing

```bash
# Run unit tests
./mvnw test

# Test endpoints
curl -H "X-User-Id: user123" http://localhost:8087/api/notifications
curl -X PUT http://localhost:8087/api/notifications/123/read
```

## 🔗 Related Services

- [API Gateway](../api-gateway/README.md) - Routes `/api/notifications/*`
- [Order Service](../order-service/README.md) - Produces order events
- [Product Catalogue Service](../product-catalogue-service/README.md) - Produces stock events
- [User Identity Service](../user-identity-service/README.md) - Produces user events

## 📝 Notes

- Service runs on port **8087**
- Requires **Kafka** for event-driven notifications
- External services (Twilio, Firebase) are optional
- Falls back gracefully if external services are unavailable
- Uses MongoDB for notification persistence
