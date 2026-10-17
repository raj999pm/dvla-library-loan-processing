package com.dvla.library.service;

import com.dvla.library.model.Loan;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoanReportService {

    public List<Loan> getBooksOnLoanForDate(
            List<Loan> loans,
            LocalDate targetDate) {

        return loans.stream()
                .filter(loan ->
                        !loan.loanDate().isAfter(targetDate)
                                &&
                                (
                                        loan.returnedDate() == null
                                                ||
                                                loan.returnedDate()
                                                        .isAfter(targetDate)
                                ))
                .toList();
    }

    public List<Loan> getOutstandingLoans(
            List<Loan> loans) {

        return loans.stream()
                .filter(loan ->
                        loan.returnedDate() == null)
                .toList();
    }

    public Map<String, Long> getLoanCountPerBranch(
            List<Loan> loans) {

        return loans.stream()
                .collect(Collectors.groupingBy(
                        Loan::branch,
                        Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry
                                .<String, Long>comparingByValue()
                                .reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
