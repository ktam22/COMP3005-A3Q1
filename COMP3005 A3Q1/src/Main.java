import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Database credentials
        String url = "jdbc:postgresql://<HOST>:<PORT>/<DATABASE_NAME>";
        String user = "<USERNAME>";
        String password = "<PASSWORD>";

        try { // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Connect to the database
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                //Successfully connected
                System.out.println("Connected to PostgreSQL successfully!");

                //User input for which option they want to do
                Scanner scn = new Scanner(System.in);
                System.out.println("Press 1 to get all the students: \nPress 2 to add a student \nPress 3 to update a student email " +
                        "\nPress 4 to delete a student\nPress 5 to exit");
                int chose = scn.nextInt();
                //Call a specific function depending on user input
                while (chose != 5) {
                    switch (chose) {
                        case 1:
                            //Retrieves the records from students table
                            getAllStudents(conn);
                            break;
                        case 2:
                            //Get the user input for adding a new student record
                            scn.nextLine();
                            System.out.println("Enter a first name");
                            String fn = scn.nextLine();
                            System.out.println("Enter a last name");
                            String ln = scn.nextLine();
                            System.out.println("Enter a email");
                            String em = scn.nextLine();
                            System.out.println("Enter a enrollment date");
                            String ed = scn.nextLine();
                            //Add the user
                            addStudent(conn, fn, ln, em, ed);
                            break;
                        case 3:
                            //User input to get the user whose email is being updated and to get the email.
                            System.out.println("Enter a the user id to update");
                            int num = scn.nextInt();
                            scn.nextLine();
                            System.out.println("Enter a the new email");
                            String email = scn.nextLine();
                            //Update the email
                            updateStudentEmail(conn, num, email);
                            break;
                        case 4:
                            //User input to get the user who is being deleted.
                            System.out.println("Give the student id of the one to delete");
                            int id = scn.nextInt();
                            //Delete the user
                            deleteStudent(conn, id);
                            break;
                    }
                    //Continue until the user enters 5
                    System.out.println("Press 1 to get all the students: \nPress 2 to add a student \nPress 3 to update a student email " +
                            "\nPress 4 to delete a student\nPress 5 to exit");
                    chose = scn.nextInt();
                }
            } else {
                //Connection failed
                System.out.println("Failed to establish connection.");
            } // Close the connection
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public static void getAllStudents(Connection conn) {
        try {
            //Create the SQL statement
            Statement stmt = conn.createStatement();
            String SQL = "SELECT * FROM students";
            ResultSet rs = stmt.executeQuery(SQL);
            //Get all the info and print it
            while(rs.next()) {
                int id = rs.getInt("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                String date = rs.getString("enrollment_date");
                System.out.println("ID: " + id + "| Name: " + firstName + " " + lastName + "| Email: " + email +
                        "| Enrollment Date: " + date);
            }
            System.out.println();
            //Close resources
            rs.close();
            stmt.close();
        }
        //Catch anything that went wrong and print the error
        catch (Exception e) {
            System.out.println("Something went wrong.");
            System.out.println(e);
        }
    }
    public static void addStudent(Connection conn, String first_name, String last_name, String email, String enrollment_date) {
        //Create a prepared statement to insert data
        String insertSQL = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);
            pstmt.setDate(4, java.sql.Date.valueOf(enrollment_date));
            //Run the query
            pstmt.executeUpdate();
        }
        //Catch anything that went wrong and print the error
        catch (Exception e) {
            System.out.println("Something went wrong.");
            System.out.println(e);
        }
    }
    public static void updateStudentEmail(Connection conn, int student_id, String new_email) {
        //Create a prepared statement to update data
        String updateSQL = "UPDATE students SET email = ? WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)){
            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);
            //Run the query
            pstmt.executeUpdate();
        }
        //Catch anything that went wrong and print the error
        catch (Exception e) {
            System.out.println("Something went wrong.");
            System.out.println(e);
        }
    }
    public static void deleteStudent(Connection conn, int student_id) {
        //Create a prepared statement to delete data
        String deleteSQL = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)){
            pstmt.setInt(1, student_id);
            //Run the query
            pstmt.executeUpdate();
        }
        //Catch anything that went wrong and print the error
        catch (Exception e) {
            System.out.println("Something went wrong.");
            System.out.println(e);
        }
    }
}