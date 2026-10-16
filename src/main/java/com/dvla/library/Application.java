package com.dvla.library;

import com.dvla.library.model.Loan;
import com.dvla.library.parser.LoanXmlParser;

import java.util.List;

public class Application {

    public static void main(String[] args) {

        LoanXmlParser parser =
                new LoanXmlParser();

        List<Loan> loans =
                parser.parse("coding-part1.xml");

        System.out.println(
                "Loans loaded: " + loans.size());
    }
}
