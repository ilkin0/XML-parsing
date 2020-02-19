package XMLparser.jaxb;

import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JAXBMain {
    public static void main(String[] args) {

        JAXBContext jaxbContext;

        {
            try {
                jaxbContext = JAXBContext.newInstance(ValCurs.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

                LocalDate date = LocalDate.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String file = "https://www.cbar.az/currencies/" + dateTimeFormatter.format(date) + ".xml";

                ValCurs currencies = (ValCurs) unmarshaller.unmarshal(new InputSource(file));
                System.out.println(currencies);
                insertTOSql(currencies);

            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }

    private static void insertTOSql(ValCurs valCurs) {

        String username = "postgres";
        String password = "postgres";

        String jdbcUrl = "jdbc:postgresql://127.0.0.1:5432/postgres";

        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            connection.setAutoCommit(false);
            String sql = "insert into currency_jaxb(code, nominal, name, value) values(?, ?, ?,?)";
            preparedStatement = connection.prepareStatement(sql);

            int count = 0;

            for (ValType valType : valCurs.getValTypeList()) {
                for (Valute valute : valType.getValuteList()) {
                    System.out.println(valute.getName());
                    preparedStatement.setString(1, valute.getCode());
                    preparedStatement.setString(2, valute.getNominal());
                    preparedStatement.setString(3, valute.getName());
                    preparedStatement.setBigDecimal(4, valute.getValue());
                    count += preparedStatement.executeUpdate();
                }
            }

            System.out.println("count = " + count);
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
