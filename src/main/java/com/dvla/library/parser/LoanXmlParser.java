package com.dvla.library.parser;

import com.dvla.library.model.Loan;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanXmlParser {

    public List<Loan> parse(String fileName) {

        List<Loan> loans = new ArrayList<>();

        try {

            Document document =
                    DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .parse(new File(fileName));

            document.getDocumentElement().normalize();

            NodeList loanNodes =
                    document.getElementsByTagName("loan");

            for (int i = 0; i < loanNodes.getLength(); i++) {

                try {

                    Element element =
                            (Element) loanNodes.item(i);

                    Loan loan = new Loan(
                            element.getAttribute("id"),
                            getValue(element, "member_id"),
                            getValue(element, "member_name"),
                            getValue(element, "branch"),
                            getValue(element, "title"),
                            getValue(element, "genre"),
                            parseDate(
                                    getValue(element, "loan_date")),
                            parseDate(
                                    getValue(element, "due_date")),
                            parseDate(
                                    getValue(element,
                                            "returned_date"))
                    );

                    loans.add(loan);

                } catch (Exception ex) {

                    System.err.println(
                            "Skipping malformed loan record");
                }
            }

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to parse XML",
                    ex);
        }

        return loans;
    }

    private String getValue(
            Element element,
            String tagName) {

        return element
                .getElementsByTagName(tagName)
                .item(0)
                .getTextContent()
                .trim();
    }

    private LocalDate parseDate(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return LocalDate.parse(value);
    }
}
