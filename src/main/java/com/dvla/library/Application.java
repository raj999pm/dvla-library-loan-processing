package com.dvla.library;

import com.dvla.library.model.Closure;
import com.dvla.library.model.Loan;
import com.dvla.library.parser.ClosureXmlParser;
import com.dvla.library.parser.LoanXmlParser;
import com.dvla.library.service.FineCalculator;
import com.dvla.library.service.LoanReportService;
import com.dvla.library.service.OverdueCalculator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) {

        LoanXmlParser loanParser =
                new LoanXmlParser();

        ClosureXmlParser closureParser =
                new ClosureXmlParser();

        LoanReportService service =
                new LoanReportService();

        List<Loan> loans =
                loanParser.parse("coding-part1.xml");

        List<Closure> closures =
                closureParser.parseClosure("coding-part2.xml");

        System.out.println("=================================");
        System.out.println("FEATURE 1 - PARSE LOAN XML");
        System.out.println("=================================");
        System.out.println("Loans Loaded: " + loans.size());

        System.out.println("\n=================================");
        System.out.println("FEATURE 5 - PARSE CLOSURE XML");
        System.out.println("=================================");

        closures.forEach(System.out::println);

        System.out.println("\n=================================");
        System.out.println("FEATURE 2 - BOOKS ON LOAN");
        System.out.println("=================================");

        LocalDate targetDate =
                LocalDate.of(2026, 4, 21);

        List<Loan> activeLoans =
                service.getBooksOnLoanForDate(
                        loans,
                        targetDate);

        activeLoans.forEach(loan ->
                System.out.println(
                        loan.loanId()
                                + " | "
                                + loan.title()
                                + " | "
                                + loan.memberName()
                                + " | "
                                + loan.branch()));

        System.out.println("\n=================================");
        System.out.println("FEATURE 3 - OUTSTANDING LOANS");
        System.out.println("=================================");

        service.getOutstandingLoans(loans)
                .forEach(loan ->
                        System.out.println(
                                loan.loanId()
                                        + " | "
                                        + loan.title()
                                        + " | "
                                        + loan.memberName()
                                        + " | "
                                        + loan.branch()
                                        + " | "
                                        + loan.dueDate()));

        System.out.println("\n=================================");
        System.out.println("FEATURE 4 - BRANCH LOAN SUMMARY");
        System.out.println("=================================");

        service.getLoanCountPerBranch(loans)
                .forEach((branch, count) ->
                        System.out.println(
                                branch + " : " + count));

        System.out.println("\n=================================");
        System.out.println("FEATURE 6 - OVERDUE DAYS");
        System.out.println("=================================");

        OverdueCalculator overdueCalculator =
                new OverdueCalculator();

        LocalDate reportDate =
                LocalDate.of(2026, 6, 5);

        service.getOutstandingLoans(loans)
                .forEach(loan -> {

                    int overdueDays =
                            overdueCalculator.calculateOverdueDays(
                                    loan,
                                    closures,
                                    reportDate);

                    if (overdueDays > 0) {

                        System.out.println(
                                loan.loanId()
                                        + " | "
                                        + loan.title()
                                        + " | "
                                        + overdueDays
                                        + " overdue days");
                    }
                });

        System.out.println("\n=================================");
        System.out.println("FEATURE 7 - FINES REPORT");
        System.out.println("=================================");

        FineCalculator fineCalculator =
                new FineCalculator();

        service.getOutstandingLoans(loans)
                .forEach(loan -> {

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
                                fine);
                    }
                });

        System.out.println("\n=================================");
        System.out.println("FEATURE 8 - GENRE POPULARITY");
        System.out.println("=================================");

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
                        System.out.println(
                                entry.getKey()
                                        + " : "
                                        + entry.getValue()));

        System.out.println("\n=================================");
        System.out.println("FEATURE 9 - OUTLIER BRANCHES");
        System.out.println("=================================");

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

        branchCounts.entrySet()
                .stream()
                .filter(entry ->
                        entry.getValue() > averageLoans * 2)
                .forEach(entry ->
                        System.out.println(
                                entry.getKey()
                                        + " : "
                                        + entry.getValue()
                                        + " loans"));
    }
}
