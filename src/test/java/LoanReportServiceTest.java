import com.dvla.library.model.Loan;
import com.dvla.library.parser.LoanXmlParser;
import com.dvla.library.service.LoanReportService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LoanReportServiceTest {


    private final LoanXmlParser parser = new LoanXmlParser();
    private final LoanReportService service = new LoanReportService();

    @Test
    void shouldLoadTwentyLoans() {

        List<Loan> loans =
                parser.parse("coding-part1.xml");

        assertEquals(20, loans.size());
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
}
