package com.dvla.library.service;

import com.dvla.library.model.Closure;
import com.dvla.library.model.Loan;

import java.time.LocalDate;
import java.util.List;

public class FineCalculator {

    private static final double DAILY_FINE = 0.20;

    public double calculateFine(
            Loan loan,
            List<Closure> closures,
            LocalDate reportDate) {

        OverdueCalculator calculator =
                new OverdueCalculator();

        int overdueDays =
                calculator.calculateOverdueDays(
                        loan,
                        closures,
                        reportDate);

        return overdueDays * DAILY_FINE;
    }
}