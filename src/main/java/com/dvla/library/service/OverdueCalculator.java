package com.dvla.library.service;

import com.dvla.library.model.Closure;
import com.dvla.library.model.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OverdueCalculator {

    public int calculateOverdueDays(
            Loan loan,
            List<Closure> closures,
            LocalDate reportDate) {

        if (loan.returnedDate() != null) {
            return 0;
        }

        if (!loan.dueDate().isBefore(reportDate)) {
            return 0;
        }

        Set<LocalDate> closureDates = closures.stream()
                .map(Closure::date)
                .collect(Collectors.toSet());

        int overdueDays = 0;

        LocalDate currentDate =
                loan.dueDate().plusDays(1);

        while (!currentDate.isAfter(reportDate)) {

            if (!closureDates.contains(currentDate)) {
                overdueDays++;
            }

            currentDate = currentDate.plusDays(1);
        }

        return overdueDays;
    }
}
