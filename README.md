# Number Blocking System

A production-ready Spring Boot application that implements an automatic number blocking system with lazy evaluation and PostgreSQL. Numbers automatically unblock after the specified duration without requiring a scheduler or cron job.

## Features

✅ **Automatic Unblocking** - Numbers unblock exactly after the specified duration  
✅ **Lazy Evaluation** - No background scheduler needed  
✅ **PostgreSQL Native** - Uses INTERVAL arithmetic for precision  
✅ **Real-time Checks** - Blocks expire at access time  
✅ **Auto-cleanup** - Maintains DB cleanliness automatically  
✅ **Production Ready** - Fully tested and documented  
✅ **REST API** - Complete API for block management  
✅ **Swagger Documentation** - Interactive API docs  

## Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: PostgreSQL 14+
- **Migrations**: Flyway
- **Build**: Maven
- **Documentation**: OpenAPI/Swagger

## Quick Start

### Prerequisites

- Java 17+
- PostgreSQL 14+
- Maven 3.8+

### Setup

1. **Clone the repository**
```bash
git clone https://github.com/abdmohanta/number-blocking-system.git
cd number-blocking-system
```

2. **Create PostgreSQL database**
```sql
CREATE DATABASE blocking_system;
```

3. **Configure application.yml**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/blocking_system
    username: postgres
    password: your_password
```

4. **Run the application**
```bash
mvn spring-boot:run
```

5. **Access Swagger UI**
```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

### Check if Number is Blocked
```http
GET /api/v1/blocks/check?phoneNumber=9876543210
```

### Block a Number
```http
POST /api/v1/blocks
Content-Type: application/json

{
  "phoneNumber": "9876543210",
  "blockDurationDays": 1,
  "reason": "Suspicious activity"
}
```

### Unblock a Number
```http
DELETE /api/v1/blocks?phoneNumber=9876543210
```

### Get Block Details
```http
GET /api/v1/blocks/details?phoneNumber=9876543210
```

### Get Statistics
```http
GET /api/v1/blocks/stats
```

### Manual Cleanup
```http
POST /api/v1/blocks/cleanup
```

## How It Works

### Timeline Example

Block created: **July 21, 2026 at 07:00 AM** for **1 day**

```
July 21, 07:00:00 AM → Block created
July 22, 06:59:59 AM → Still blocked ✓
July 22, 07:00:00 AM → Auto-unblocked! 🎯
July 22, 07:00:01 AM → Not blocked ✓
```

## Database Schema

```sql
CREATE TABLE blocked_numbers (
    id BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    blocked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    block_duration_days INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    block_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License Details

MIT License

## Author 

**abdmohanta** - [GitHub Profile](https://github.com/abdmohanta)
