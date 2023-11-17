import java.sql.*;


public class Database {

    void insertData(String setUsername, String setPassword){


        try (Connection connectionForDataBase = DriverManager.getConnection("jdbc:mysql://localhost:3306/driver_info", "root", "35Characters+")) {
            // Create a PreparedStatement for inserting data
            String insertSQL = "INSERT INTO driver_login (username, password) VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connectionForDataBase.prepareStatement(insertSQL)) {
                // Set the username and password values
                preparedStatement.setString(1, setUsername); // Replace with the actual username
                preparedStatement.setString(2, setPassword); // Replace with the actual password

                // Execute the insert statement
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Driver information added successfully.");
                } else {
                    System.out.println("Failed to insert driver information.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to load the database");
        }

    }

    Boolean retrieveData(String checkUsername, String checkPassword){


        try (Connection connectionForDataBase = DriverManager.getConnection("jdbc:mysql://localhost:3306/driver_info", "root", "35Characters+")) {
            // Create a PreparedStatement for searching data
            String searchSQL = "SELECT username, password FROM driver_login WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connectionForDataBase.prepareStatement(searchSQL)) {
                // Set the username and password values to search for
                preparedStatement.setString(1, checkUsername); // Replace with the username to search for
                preparedStatement.setString(2, checkPassword); // Replace with the password to search for

                // Execute the SELECT statement
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Data found, username and password combination exists
                    System.out.println("Username and password combination exists in the database.");
                    return true;

                } else {
                    // Data not found, username and password combination does not exist
                    System.out.println("Username and password combination does not exist in the database.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to load the database");
        }

        return false;
    }


}
