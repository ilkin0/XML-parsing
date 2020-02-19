package XMLparser.sax;

import XMLparser.domain.Currencies;

import java.sql.*;
import java.util.List;

public class JDBConnection {
    public static void main(String[] args) {
        String username = "postgres";
        String password = "postgres";
        String jdbcURL = "jdbc:postgresql://127.0.0.1:5432/postgres";

        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        {
            try {
                connection = DriverManager.getConnection(jdbcURL, username, password);
                String sql = "insert into currency (id, code, nominal, name, value, action_date) " +
                        "values ()";
                statement = connection.prepareStatement(sql);
                resultSet = statement.executeQuery();

//
//                while (resultSet.next()){
//                    long id = resultSet.getLong("id");
//                    String code = resultSet.getString("code");
//                    String nominal = resultSet.getString("nominal");
//                    String name = resultSet.getString("name");
//                    BigDecimal value = resultSet.getBigDecimal("value");
//                    Date action_date = resultSet.getDate("action_date");
//                    System.out.printf("%d %s %s %s %.4f, %s\n", id, code, nominal, name, value, action_date);
//                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (statement != null) {
                        statement.close();
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

    public void insertToSql(List<Currencies> list) {
        String username = "postgres";
        String password = "postgres";
        String jdbcURL = "jdbc:postgresql://127.0.0.1:5432/postgres";

        Connection connection = null;
        PreparedStatement statement = null;

        {
            try {
                connection = DriverManager.getConnection(jdbcURL, username, password);

                String sql = "insert into currency_sax (code, nominal, name, value) " +
                        "values (?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);

                for (int i = 1; i < list.size(); i++) {
                    preparedStatement.setString(1, list.get(i).getCode());
                    preparedStatement.setString(2, list.get(i).getNominal());
                    preparedStatement.setString(3, list.get(i).getName());
                    preparedStatement.setBigDecimal(4, list.get(i).getValue());

                    preparedStatement.executeUpdate();


                }


            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
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
}
