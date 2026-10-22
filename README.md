# DVLA Library Loan Processing

## Overview

This application processes library loan and closure data from XML files and generates reports based on the library dataset.

The solution implements the requirements from the DVLA Senior Software Engineer coding exercise.

## Features

### Feature 1 - Parse Loan XML

- Reads loan records from an XML file.
- Converts records into Java domain objects.
- Validates mandatory fields.
- Skips invalid records while continuing processing.

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

### Feature 5 - Parse Closure XML

- Reads closure records from a separate XML file.
- Converts records into Closure domain objects.
- Validates closure data.
- Skips invalid records while continuing processing.

### Feature 6 - Overdue Days Calculation

Calculates overdue days for outstanding loans using a report date of:

```text
2026-06-05
```

Rules:

- Overdue days start after the due date.
- Closure days are excluded.
- Returned books are not included.

Example:

```text
Due Date: 2026-05-28

2026-05-29
2026-05-30
2026-05-31
2026-06-01 (closed)
2026-06-02
2026-06-03
2026-06-04
2026-06-05

Result: 7 overdue days
```

### Feature 7 - Fines Report

Calculates fines for overdue books.

Rules:

- Fine rate is £0.20 per overdue day.
- Closure days are excluded.
- Uses overdue day calculations from Feature 6.

Example:

```text
7 overdue days × £0.20 = £1.40
```

### Feature 8 - Genre Popularity Report

Calculates loan totals per genre.

Report:

- Groups loans by genre.
- Counts total loans per genre.
- Sorts genres from most borrowed to least borrowed.

### Feature 9 - Outlier Branch Report

Identifies branches with unusually high loan activity.

Rules:

- Calculates the average number of loans per branch.
- Identifies branches whose loan count is more than double the organisation-wide average.

## Assumptions

- Empty returned date means the book is still on loan.
- Dates are provided in ISO format (`yyyy-MM-dd`).
- Invalid loan records are skipped and processing continues.
- Invalid closure records are skipped and processing continues.
- Closure dates apply across the entire organisation.
- The report date for overdue and fine calculations is `2026-06-05`.

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
│           │   ├── Loan.java
│           │   └── Closure.java
│           │
│           ├── parser
│           │   ├── LoanXmlParser.java
│           │   └── ClosureXmlParser.java
│           │
│           ├── service
│           │   ├── LoanReportService.java
│           │   ├── OverdueCalculator.java
│           │   └── FineCalculator.java
│           │
│           └── Application.java
│
└── test
    └── java
        └── LoanReportServiceTest.java
```

## Input Files

### Loan Data

```text
coding-part1.xml
```

### Closure Data

```text
coding-part2.xml
```

## Running the Application

Run:

```text
Application.java
```

The application:

1. Loads loan data from `coding-part1.xml`
2. Loads closure data from `coding-part2.xml`
3. Generates books-on-loan reports
4. Generates outstanding loan reports
5. Generates branch summary reports
6. Calculates overdue days
7. Calculates overdue fines
8. Generates genre popularity reports
9. Generates outlier branch reports

## Running Tests

Run:

```text
LoanReportServiceTest
```

The test suite covers:

- Loan XML parsing
- Closure XML parsing
- Books on loan for a specified date
- Outstanding loans
- Branch loan summaries
- Overdue day calculations
- Fine calculations
- Genre popularity calculations
- Outlier branch calculations