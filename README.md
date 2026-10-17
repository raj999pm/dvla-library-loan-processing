# DVLA Library Loan Processing

## Overview

This application processes library loan data from an XML file and provides reports based on the loan information.

The solution implements the requirements from the DVLA Senior Software Engineer coding exercise.

## Features

### Feature 1 - Parse XML

- Reads loan records from an XML file.
- Converts records into Java domain objects.
- Handles invalid loan records gracefully.

### Feature 2 - Books on Loan for a Specified Date

Returns books that were on loan for a given date.

A book is considered on loan when:

- Loan date is on or before the specified date.
- Returned date is empty or later than the specified date.

### Feature 3 - Outstanding Loans

Returns loans that have not yet been returned.

Report includes:

- Loan ID
- Book Title
- Member Name
- Branch
- Due Date

### Feature 4 - Branch Loan Summary

Calculates total loans per branch and displays the results sorted by highest loan count.

## Technology

- Java 21
- JUnit 5
- DOM XML Parser
- IntelliJ IDEA

## Project Structure

```text
src
├── main
│   └── java
│       └── com.dvla.library
│           ├── model
│           │   └── Loan.java
│           ├── parser
│           │   └── LoanXmlParser.java
│           ├── service
│           │   └── LoanReportService.java
│           └── Application.java
│
└── test
    └── java
        └── LoanReportServiceTest.java
```

## Running the Application

Run:

```text
Application.java
```

The application:

1. Loads loan data from `coding-part1.xml`
2. Displays books on loan for a specified date
3. Displays outstanding loans
4. Displays branch loan summaries

## Running Tests

Run:

```text
LoanReportServiceTest
```