# Ethereum Deposit Tracker

## Overview
The Ethereum Deposit Tracker is a Spring Boot application designed to monitor and record Ethereum (ETH) deposits made to the Beacon Deposit Contract following MVC pattern. It uses the Web3j library to interact with the Ethereum blockchain and Spring Boot's mailing features to notify users of successful deposit tracking. The system tracks deposits by:

- **RPC Integration**: Establishing a connection to an Ethereum node (via Alchemy) and using Ethereum RPC methods to fetch deposit data.
- **Deposit Tracking Logic**: Monitoring the Beacon Deposit Contract (address `0x00000000219ab540356cBB839Cbe05303d7705Fa`) for incoming ETH deposits and recording details such as amount, sender address, timestamp, and handling multiple deposits in a single transaction.
- **Error Handling and Logging**: Implementing comprehensive error handling for API calls and RPC interactions, along with logging mechanisms to track errors and important events.


## Video Sample
https://drive.google.com/file/d/1J8_WHY7Em-oXvDcBc2FFebXjaK8zzFdA/view?usp=drive_link


## Technologies Used
- **Java**: Programming language
- **Spring Boot**: Framework for creating the application
- **MySQL**: Database for storing deposit records
- **Spring Mail**: For sending notification emails
- **Web3j**: Java library for integrating with Ethereum blockchain
- **Alchemy**: Ethereum RPC provider
- **JPA**: For interacting with the MySQL database


## Project Components

### EthereumService
- **Purpose**: Handles interactions with the Ethereum blockchain.
- **Functions**:
  - `getLatestBlock()`: Fetches the latest block from the Ethereum blockchain.
  - `getTransactionReceipt(String txHash)`: Retrieves the receipt for a given transaction hash.
  - `saveDeposit(String txHash)`: Processes and saves deposit information to the database.
  - `startBlockListener()`: Listens for new blocks and identifies deposits made to the Beacon Deposit Contract.
  - `separate(String inputData)`: Processes a comma-separated list of transaction hashes and saves each deposit.

### DepositController
- **Purpose**: Provides RESTful endpoints for interacting with the deposit tracker.
- **Endpoints**:
  - `GET /deposits/track/`: Tracks a deposit based on a transaction hash and sends a notification email.
  - `GET /deposits/`: Retrieves all deposits from the database.
  - `GET /deposits/{txHash}`: Retrieves a deposit by its transaction hash.
  - `GET /deposits/start-tracking`: Starts real-time tracking of new Ethereum deposits.

### EmailService
- **Purpose**: Manages the sending of notification emails.
- **Functions**:
  - `sendSuccessEmail(String recipientEmail, String messageBody)`: Sends an email notifying the user of a successful deposit tracking.

### Deposit Entity
- **Purpose**: Represents the deposit data model.
- **Fields**:
  - `id`: Unique identifier for the deposit.
  - `blockNumber`: The block number in which the deposit was recorded.
  - `blockTimestamp`: The timestamp when the block was recorded.
  - `fee`: The fee associated with the deposit.
  - `hash`: The transaction hash of the deposit.
  - `pubkey`: The public key extracted from the deposit transaction data.
 

## Prerequisites

- **Java 17 or higher**
- **Maven**


## Setup Instructions
1. **Clone the Repository**
   ```bash
   git clone https://github.com/adtiio/-Ethereum-Deposit-Tracker.git
   cd '-Ethereum-Deposit-Tracker'

2. **Configure the Application**:
    Update application.properties and EthereumService(optional) with your MySQL database details and Alchemy secret key.
   
3. **Build and Run the Application**:
    ```bash
     ./mvnw clean package
     ./mvnw spring-boot:run
  OR
Go to `\src\main\java\com\ethereumTracker\EthereumDepositeTrackerApplication.java` and run the file directly from your IDE.


## Usage

- **Start the Application**
  - Launch the application and navigate to `http://localhost:8081/home`.

