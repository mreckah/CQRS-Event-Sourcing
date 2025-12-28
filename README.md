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
_All events are stored and can be replayed_

### Search & Query

![Event Search](screenshots/image-2.png)
_Searchable logs for auditing and debugging specific events._

![alt text](screenshots/image-10.png)
_Detailed drill-down of event payloads and metadata._

---

## Swagger UI - Interactive API Documentation

**URL**: http://localhost:8081/swagger-ui/index.html

### API Overview

![Swagger UI Home](screenshots/image-3.png)

_Interactive documentation for discovering available API endpoints._

### Command Endpoints (Write Side)

![Command APIs](screenshots/image-4.png)

_Write-side endpoints for executing business commands._

---

## Testing with Swagger

### 1. Create Account

![Create Account](screenshots/image-5.png)
_Initiating a new account creation command._

### 2. Credit Account

![Credit Account](screenshots/image-7.png)
_Successfully crediting an account via a command._

### 3. Debit Account

![Debit Account](screenshots/image-6.png)
_Processing a debit request through the domain aggregate._

### 5. List All Accounts

![List Accounts](screenshots/image-8.png)
_Querying the read-model to retrieve all existing accounts._

---

### Tables Overview

#### Accounts Table

<img width="698" height="406" alt="image" src="https://github.com/user-attachments/assets/3362e06b-9b22-4a1e-ad13-63a741507121" />

_Relational database view of the current account states (Read Model)._

#### Account Transactions Table

<img width="1034" height="391" alt="image" src="https://github.com/user-attachments/assets/4cbd0a63-e084-45de-858a-45a791d3fcb6" />
*Transaction history table tracking all individual movements.*

<img width="579" height="221" alt="image" src="https://github.com/user-attachments/assets/b09aea3e-83c7-46bf-91b9-c3a6a8fbb6db" />

_Aggregated transaction data for analytics and reporting._

---

## Real-time Analytics Dashboard

Access the analytics dashboard at: [http://localhost:8084/chart.html](http://localhost:8084/chart.html)

The Real-time Analytics Dashboard provides a comprehensive view of account activities through an interactive interface. It visualizes transaction trends, current account balances, and event streams in real-time. The dashboard automatically updates to reflect new transactions, with detailed information available on hover. The intuitive design features color-coded charts for easy differentiation between account balances, debits, and credits.

![Analytics Dashboard](screenshots/![alt text](screenshots/image--.png))
*Live dashboard displaying real-time account activities and transaction analytics*


---

## Conclusion

This project successfully implements **CQRS** and **Event Sourcing** using the **Axon Framework**. It provides a scalable, auditable, and resilient architecture for modern banking microservices, ensuring data consistency and a complete history of all transactions.
