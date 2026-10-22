package com.dvla.library.service;

import com.dvla.library.model.Closure;
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


    public void generateFineReport(
            List<Loan> loans,
            List<Closure> closures) {

        FineCalculator fineCalculator =
                new FineCalculator();

        LocalDate reportDate =
                LocalDate.of(2026, 6, 5);

        System.out.println("=== Fines Report ===");

        for (Loan loan : loans) {

            if (loan.returnedDate() != null) {
                continue;
            }

            double fine =
                    fineCalculator.calculateFine(
                            loan,
                            closures,
                            reportDate);

            if (fine > 0) {

                System.out.printf(
                        "%s | %s | £%.2f%n",
                        loan.loanId(),
                        loan.title(),
                        fine
                );
            }
        }
    }


    public void generateGenrePopularityReport(
            List<Loan> loans) {

        System.out.println("=== Genre Popularity Report ===");

        loans.stream()
                .collect(Collectors.groupingBy(
                        Loan::genre,
                        Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry.<String, Long>comparingByValue()
                                .reversed())
                .forEach(entry ->
                        System.out.printf(
                                "%s : %d%n",
                                entry.getKey(),
                                entry.getValue()));
    }

    public void generateOutlierBranchesReport(
            List<Loan> loans) {

        Map<String, Long> branchCounts =
                loans.stream()
                        .collect(Collectors.groupingBy(
                                Loan::branch,
                                Collectors.counting()));

        double averageLoans =
                branchCounts.values()
                        .stream()
                        .mapToLong(Long::longValue)
                        .average()
                        .orElse(0);

        System.out.println("=== Outlier Branches Report ===");

        branchCounts.entrySet()
                .stream()
                .filter(entry ->
                        entry.getValue() > averageLoans * 2)
                .forEach(entry ->
                        System.out.printf(
                                "%s : %d loans%n",
                                entry.getKey(),
                                entry.getValue()));
    }
}
