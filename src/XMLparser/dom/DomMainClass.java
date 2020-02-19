package XMLparser.dom;

import XMLparser.domain.Currencies;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DomMainClass {
    public static void main(String[] args) {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        {
            try {
                builder = factory.newDocumentBuilder();
                LocalDate date = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String file = "https://www.cbar.az/currencies/" + formatter.format(date) + ".xml";
                Document document = builder.parse(new InputSource(file));

                document.normalizeDocument();

                NodeList currencyNodeList = document.getElementsByTagName("Valute");
                List<Currencies> currenciesList = new ArrayList<>();

                for (int i = 0; i < currencyNodeList.getLength(); i++) {
                    Node currencyNode = currencyNodeList.item(i);
                    Element currencyElement = (Element) currencyNode;

                    Currencies currencies = new Currencies();

                    if (currencyNode.getNodeType() == Node.ELEMENT_NODE) {

                        String code = currencyElement.getAttribute("Code");
                        currencies.setCode(code);

                        String nominal = currencyElement.getElementsByTagName("Nominal").item(0).getTextContent();
                        currencies.setNominal(nominal);

                        String name = currencyElement.getElementsByTagName("Name").item(0).getTextContent();
                        currencies.setName(name);

                        String value = currencyElement.getElementsByTagName("Value").item(0).getTextContent();
                        currencies.setValue(new BigDecimal(value));

                        currenciesList.add(currencies);
                    }
                }

                currenciesList.forEach(System.out::println);
                insertToSQL(currenciesList);


            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void insertToSQL(List<Currencies> list) {
        String username = "postgres";
        String password = "postgres";
        String jdbcURL = "jdbc:postgresql://127.0.0.1:5432/postgres";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);

            String sql = "insert into currency_dom(code, nominal, name, value) values(?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            int count = 0;

            for (int i = 0; i < list.size(); i++) {

                preparedStatement.setString(1, list.get(i).getCode());
                preparedStatement.setString(2, list.get(i).getNominal());
                preparedStatement.setString(3, list.get(i).getName());
                preparedStatement.setBigDecimal(4, list.get(i).getValue());
                ;


                count = preparedStatement.executeUpdate();


            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
