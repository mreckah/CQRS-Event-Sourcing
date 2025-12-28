# Banking Microservices - CQRS & Event Sourcing

A distributed banking system built with **Spring Boot**, **Axon Framework**, and **Apache Kafka** demonstrating CQRS and Event Sourcing patterns.

---

## Architecture Overview

<div align="center">
  <img src="screenshots/image.png" alt="Architecture Diagram" width="800" style="border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);">
  <p><i>High-level visualization of the CQRS and Event Sourcing flow.</i></p>
</div>

---

## Axon Server Dashboard

**URL**: http://localhost:8024

### Overview
![Axon Server Overview](screenshots/image-1.png)
*All events are stored and can be replayed*

### Search & Query
![Event Search](screenshots/image-2.png)
*Searchable logs for auditing and debugging specific events.*

![alt text](screenshots/image-10.png)
*Detailed drill-down of event payloads and metadata.*

---

## Swagger UI - Interactive API Documentation

**URL**: http://localhost:8081/swagger-ui/index.html

### API Overview
![Swagger UI Home](screenshots/image-3.png)

*Interactive documentation for discovering available API endpoints.*

### Command Endpoints (Write Side)
![Command APIs](screenshots/image-4.png)

*Write-side endpoints for executing business commands.*


---

## Testing with Swagger

### 1. Create Account
![Create Account](screenshots/image-5.png)
*Initiating a new account creation command.*

### 2. Credit Account
![Credit Account](screenshots/image-7.png)
*Successfully crediting an account via a command.*

### 3. Debit Account
![Debit Account](screenshots/image-6.png)
*Processing a debit request through the domain aggregate.*

### 5. List All Accounts
![List Accounts](screenshots/image-8.png)
*Querying the read-model to retrieve all existing accounts.*

---
### Tables Overview

#### Accounts Table
<img width="698" height="406" alt="image" src="https://github.com/user-attachments/assets/3362e06b-9b22-4a1e-ad13-63a741507121" />

*Relational database view of the current account states (Read Model).*

#### Account Transactions Table
<img width="1034" height="391" alt="image" src="https://github.com/user-attachments/assets/4cbd0a63-e084-45de-858a-45a791d3fcb6" />
*Transaction history table tracking all individual movements.*

<img width="579" height="221" alt="image" src="https://github.com/user-attachments/assets/b09aea3e-83c7-46bf-91b9-c3a6a8fbb6db" />

*Aggregated transaction data for analytics and reporting.*

---

## Conclusion

This project successfully implements **CQRS** and **Event Sourcing** using the **Axon Framework**. It provides a scalable, auditable, and resilient architecture for modern banking microservices, ensuring data consistency and a complete history of all transactions.
