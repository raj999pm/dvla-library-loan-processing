package com.dvla.library.parser;

import com.dvla.library.model.Closure;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ClosureXmlParser {

    public List<Closure> parseClosure(String fileName) {

        List<Closure> closures = new ArrayList<>();

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

                    Closure closure = new Closure(

                            parseDate(element.getAttribute("date")),
                            getValue(element, "reason")

                    );


                    if (isValidClosure(closure)) {

                        closures.add(closure);

                    } else {

                        System.err.println(
                                "Skipping invalid closure record");
                    }

                } catch (Exception ex) {

                    System.err.println(
                            "Skipping malformed closure record");
                }
            }

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Failed to parse XML",
                    ex);
        }

        return closures;
    }


    private boolean isValidClosure(Closure closure) {

        if (closure.date() == null) {
            return false;
        }

        if (closure.reason() == null || closure.reason().isBlank()) {
            return false;
        }



        return true;
    }



    private String getValue(
            Element element,
            String tagName) {

        NodeList nodes =
                element.getElementsByTagName(tagName);

        if (nodes.getLength() == 0) {
            return null;
        }

        return nodes.item(0)
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
