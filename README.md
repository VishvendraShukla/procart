# ProCart

ProCart is created with a vision to cater the needs of a small or emerging product based companies to have a flexible and robust backend ready to use.

The ProCart system is designed to efficiently manage inventory and cart functionalities, supporting product management, user roles, orders, transactions, and audit logs. It integrates with Stripe for seamless checkout sessions and supports multiple currencies for pricing. The system includes an admin dashboard for comprehensive management.

## Features

* **Product Management**: Create, update, and delete products with multi-currency pricing.

* **Cart Management**: Add, update, and remove products from the cart.

* **User Management**: Manage company users, including admins who are stored in the database.

* **Order Processing**: Handle customer orders and order statuses.

* **Transaction Management**:
  - Process checkout sessions via Stripe.
  - Maintain transaction logs for auditing.

* **Audit Logs**: Track changes and actions performed in the system.

* **Charge Management**: Apply charges for transactions and products.

* **Multi-Currency Support**: Products can be priced in different currencies.

* **Admin Dashboard**: A comprehensive interface for admins to monitor and manage the system.

## Tech Stack

* **Backend Framework**: Spring Boot with Spring Security.

* **Database**: PostgreSQL

* **Authentication**: Json Web Token

* **Payment Integration**: Stripe

* **Logging & Monitoring**: Logs are captured in DB and can be viewed using admin dashboard.

* **Deployment**: Provided as per requirement.

# Installation

## Clone the repository

```sh
git clone https://github.com/VishvendraShukla/procart
cd procart
```

## Install dependencies
```sh
mvn install
```

## Start the development server
```sh
mvn spring-boot:run
```
