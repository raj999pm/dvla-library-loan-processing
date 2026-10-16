package com.dvla.library.model;

import java.time.LocalDate;

public record Loan (String loanId,
                    String memberId,
                    String memberName,
                    String branch,
                    String title,
                    String genre,
                    LocalDate loanDate,
                    LocalDate dueDate,
                    LocalDate returnedDate){

}
