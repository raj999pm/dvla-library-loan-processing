import com.dvla.library.model.Close;
import com.dvla.library.model.Loan;
import com.dvla.library.parser.ClosureXmlParser;
import com.dvla.library.parser.LoanXmlParser;
import com.dvla.library.service.LoanReportService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LoanReportServiceTest {


    private final LoanXmlParser parser = new LoanXmlParser();
    private final ClosureXmlParser closeParser = new ClosureXmlParser();
    private final LoanReportService service = new LoanReportService();

    @Test
    void shouldLoadTwentyLoans() {

        List<Loan> loans =
                parser.parse("coding-part1.xml");

        assertEquals(20, loans.size());
    }


    @Test
    void shouldLoadclose() {

        List<Close> close =
                closeParser.parse1("coding-part2.xml");

        assertEquals(0, close.size());
    }

    @Test
    void shouldReturnBooksOnLoanForSpecifiedDate() {

        List<Loan> loans =
                parser.parse("coding-part1.xml");

        List<Loan> result =
                service.getBooksOnLoanForDate(
                        loans,
                        LocalDate.of(2026, 4, 21));

        assertEquals(6, result.size());
    }

    @Test
    void shouldReturnOutstandingLoans() {

        List<Loan> loans =
                parser.parse("coding-part1.xml");

        List<Loan> result =
                service.getOutstandingLoans(loans);

        assertEquals(10, result.size());
    }

    @Test
    void shouldCalculateLoanCountPerBranch() {

        List<Loan> loans =
                parser.parse("coding-part1.xml");

        var result =
                service.getLoanCountPerBranch(loans);

        assertEquals(8L, result.get("Central"));
        assertEquals(7L, result.get("Riverside"));
        assertEquals(5L, result.get("Hillside"));
    }

    @Test
    void shouldNotReturnBookWhenReturnedOnTargetDate() {

        Loan loan = new Loan(
                "L999",
                "M999",
                "Test User",
                "Central",
                "Test Book",
                "Test",
                LocalDate.of(2026,4,1),
                LocalDate.of(2026,4,21),
                LocalDate.of(2026,4,21)
        );

        List<Loan> result =
                service.getBooksOnLoanForDate(
                        List.of(loan),
                        LocalDate.of(2026,4,21));

        assertEquals(0, result.size());
    }
}
