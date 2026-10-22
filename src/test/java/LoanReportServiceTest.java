import com.dvla.library.model.Closure;
import com.dvla.library.model.Loan;
import com.dvla.library.parser.ClosureXmlParser;
import com.dvla.library.parser.LoanXmlParser;
import com.dvla.library.service.FineCalculator;
import com.dvla.library.service.LoanReportService;
import com.dvla.library.service.OverdueCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


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
    void shouldLoadClosure() {

        List<Closure> closure =
                closeParser.parseClosure("coding-part2.xml");

        assertEquals(4, closure.size());
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

    @Test
    void shouldCalculateOverdueDays() {

        List<Closure> closures =
                closeParser.parseClosure("coding-part2.xml");

        Loan loan = new Loan(
                "L016",
                "B001",
                "John",
                "Leicester",
                "Book",
                "Fiction",
                LocalDate.of(2026, 5, 14),
                LocalDate.of(2026, 5, 28),
                null
        );

        OverdueCalculator calculator =
                new OverdueCalculator();

        int overdueDays =
                calculator.calculateOverdueDays(
                        loan,
                        closures,
                        LocalDate.of(2026,6,5));

        assertEquals(7, overdueDays);
    }


    @Test
    void shouldCalculateFine() {

        List<Closure> closures =
                closeParser.parseClosure("coding-part2.xml");

        Loan loan = new Loan(
                "L016",
                "B001",
                "John",
                "Leicester",
                "Book",
                "Fiction",
                LocalDate.of(2026, 5, 14),
                LocalDate.of(2026, 5, 28),
                null
        );

        FineCalculator calculator =
                new FineCalculator();

        double fine =
                calculator.calculateFine(
                        loan,
                        closures,
                        LocalDate.of(2026, 6, 5));

        assertEquals(1.40, fine,0.001);
    }


    @Test
    void shouldGroupLoansByGenre() {

        List<Loan> loans =
                parser.parse("coding-part1.xml");

        Map<String, Long> genres =
                loans.stream()
                        .collect(Collectors.groupingBy(
                                Loan::genre,
                                Collectors.counting()));

        assertFalse(genres.isEmpty());
    }


    @Test
    void shouldCalculateAverageBranchLoans() {

        List<Loan> loans =
                parser.parse("coding-part1.xml");

        Map<String, Long> branchCounts =
                loans.stream()
                        .collect(Collectors.groupingBy(
                                Loan::branch,
                                Collectors.counting()));

        double average =
                branchCounts.values()
                        .stream()
                        .mapToLong(Long::longValue)
                        .average()
                        .orElse(0);

        assertTrue(average > 0);
    }
}
