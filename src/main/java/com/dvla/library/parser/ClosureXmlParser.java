package com.dvla.library.parser;

import com.dvla.library.model.Close;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ClosureXmlParser {

    public List<Close> parse1(String fileName) {

        List<Close> close = new ArrayList<>();

        try {

            Document document =
                    DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .parse(new File(fileName));

            document.getDocumentElement().normalize();

            NodeList closureNodes =
                    document.getElementsByTagName("closure");

            for (int i = 0; i < closureNodes.getLength(); i++) {

                try {

                    Element element =
                            (Element) closureNodes.item(i);

                    Close closure = new Close(

                            parseDate(
                                    getValue(element, "date")),
                            getValue(element, "reason")
                    );

                    if (isValidLoan(closure)) {

                        close.add(closure);

                    } else {

                        System.err.println(
                                "Skipping invalid loan record ");
                    }

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

        return close;
    }


    private boolean isValidLoan(Close close) {

        if (close.date() == null) {
            return false;
        }

        if (close.reason() == null || close.reason().isBlank()) {
            return false;
        }



        return true;
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
