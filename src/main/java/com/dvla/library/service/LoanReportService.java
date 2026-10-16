package com.dvla.library.service;

import com.dvla.library.model.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class LoanReportService {

    public List<Loan> getBooksOnLoanForDate(
            List<Loan> loans,
            LocalDate targetDate) {

        return List.of();
    }

    public List<Loan> getOutstandingLoans(
            List<Loan> loans) {

        return List.of();
    }

    public Map<String, Long> getLoanCountPerBranch(
            List<Loan> loans) {

        return Map.of();
    }
}
