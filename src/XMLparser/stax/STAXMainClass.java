package XMLparser.stax;

import XMLparser.domain.Currencies;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.stream.StreamSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class STAXMainClass {
    public static void main(String[] args) {
        try {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate date = LocalDate.now();
            String file = "https://www.cbar.az/currencies/" + formatter.format(date) + ".xml";

            List<Currencies> currencyList = new ArrayList<>();
            Currencies tempCurrency = new Currencies();
            boolean isCurrencies = false;
            boolean isCode = false;
            boolean isNominal = false;
            boolean isName = false;
            boolean isValue = false;

//            int counter = 0;
//            int limit = new Scanner(System.in).nextInt();

            XMLStreamReader reader = factory.createXMLStreamReader(new StreamSource(file));
            while (reader.hasNext()) {
                int elementType = reader.next();

                if (elementType == XMLEvent.START_ELEMENT) {
                    if (reader.getName().toString().equals("Valute")) {
                        tempCurrency = new Currencies();
                        isCurrencies = true;
                        String code = reader.getAttributeValue(null, "Code");
                        tempCurrency.setCode(code);
                    } else if (reader.getName().toString().equals("Nominal")) {
                        isNominal = true;
                    } else if (reader.getName().toString().equals("Name")) {
                        isName = true;
                    } else if (reader.getName().toString().equals("Value")) {
                        isValue = true;
                    }

                } else if (elementType == XMLEvent.CHARACTERS) {
                    String data = reader.getText();
                    if (isCode) {
                        tempCurrency.setCode(data);
                    } else if (isNominal) {
                        tempCurrency.setNominal(data);
                    } else if (isName) {
                        tempCurrency.setName(data);
                    } else if (isValue) {
                        tempCurrency.setValue(new BigDecimal(data));
                    }
                } else if (elementType == XMLEvent.END_ELEMENT) {
                    if (reader.getName().toString().equals("Valute")) {
                        isCurrencies = false;

//                        For limitation parsing process
//                        if ( counter < limit) {
//                            currencyList.add(tempCurrency);
//                        }else {
//                            break;
//                        }

                        currencyList.add(tempCurrency);
                        tempCurrency = null;
                    } else if (reader.getName().toString().equals("Code")) {
                        isCode = false;
                    } else if (reader.getName().toString().equals("Nominal")) {
                        isNominal = false;
                    } else if (reader.getName().toString().equals("Name")) {
                        isName = false;
                    } else if (reader.getName().toString().equals("Value")) {
                        isValue = false;
                    }
                }
            }

            insertToSQL(currencyList);
            currencyList.forEach(System.out::println);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static void insertToSQL(List<Currencies> currencyList) {
        String username = "postgres";
        String password = "postgres";
        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.setAutoCommit(false);
            String sql = "insert into currency_stax (code, nominal, name, value) " +
                    "values (?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(sql);

            for (Currencies currencies : currencyList) {
                preparedStatement.setString(1, currencies.getCode());
                preparedStatement.setString(2, currencies.getNominal());
                preparedStatement.setString(3, currencies.getName());
                preparedStatement.setBigDecimal(4, currencies.getValue());
                preparedStatement.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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
