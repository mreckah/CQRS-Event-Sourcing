# Banking Microservices - CQRS & Event Sourcing

A distributed banking system built with **Spring Boot**, **Axon Framework**, and **Apache Kafka** demonstrating CQRS and Event Sourcing patterns.

---

## Architecture Overview

![Architecture Diagram](screenshots/image.png))

*Multi-module Maven project with separate Command and Query sides*

---

## Axon Server Dashboard

**URL**: http://localhost:8024

### Overview
![Axon Server Overview](screenshots/image-1.png)

### Event Store
![Event Store](screenshots/axon-events.png)

*All events are stored and can be replayed*

### Search & Query
![Event Search](screenshots/image-2.png)
![alt text](screenshots/image-10.png)


---

## Swagger UI - Interactive API Documentation

**URL**: http://localhost:8081/swagger-ui/index.html

### API Overview
![Swagger UI Home](screenshots/image-3.png)

### Command Endpoints (Write Side)
![Command APIs](screenshots/image-4.png)


---

## Testing with Swagger

### 1. Create Account
![Create Account](screenshots/image-5.png)

### 2. Credit Account
![Credit Account](screenshots/image-7.png)

### 3. Debit Account
![Debit Account](screenshots/image-6.png)

### 5. List All Accounts
![List Accounts](screenshots/image-8.png)

---
### Tables Overview

#### Accounts Table
<img width="698" height="406" alt="image" src="https://github.com/user-attachments/assets/3362e06b-9b22-4a1e-ad13-63a741507121" />

#### Account Transactions Table
<img width="1034" height="391" alt="image" src="https://github.com/user-attachments/assets/4cbd0a63-e084-45de-858a-45a791d3fcb6" />
<img width="579" height="221" alt="image" src="https://github.com/user-attachments/assets/b09aea3e-83c7-46bf-91b9-c3a6a8fbb6db" />



