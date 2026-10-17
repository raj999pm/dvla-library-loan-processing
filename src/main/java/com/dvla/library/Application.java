package com.dvla.library;

import com.dvla.library.model.Loan;
import com.dvla.library.parser.LoanXmlParser;
import com.dvla.library.service.LoanReportService;

import java.time.LocalDate;
import java.util.List;

public class Application {

    public static void main(String[] args) {

        LoanXmlParser parser =
                new LoanXmlParser();

        List<Loan> loans =
                parser.parse("coding-part1.xml");

        LoanReportService service =
                new LoanReportService();

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


        System.out.println("\nOutstanding Loans");

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


        System.out.println("\nLoans Per Branch");

        service.getLoanCountPerBranch(loans)
                .forEach((branch, count) ->
                        System.out.println(
                                branch + " : " + count));
    }
}
