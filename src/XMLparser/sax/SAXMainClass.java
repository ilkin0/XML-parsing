package XMLparser.sax;

import XMLparser.domain.Currencies;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SAXMainClass {
    public static void main(String[] args) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        ContentHandler handler = new ContentHandler();

        System.out.println("Milli mezenne:");

        {
            try {
                parser = factory.newSAXParser();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate date = LocalDate.now();
                String file = "https://www.cbar.az/currencies/" + formatter.format(date) + ".xml";


                parser.parse(file, handler);
                List<Currencies> currenciesList = handler.getCurrencyList();
                JDBConnection jdbConnection = new JDBConnection();

                jdbConnection.insertToSql(currenciesList);
                currenciesList.forEach(System.out::println);

            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        }


    }
}
