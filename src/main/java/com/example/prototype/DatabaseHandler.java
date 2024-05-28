package com.example.prototype;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {
    private final String url = "jdbc:mysql://localhost:3306/clinic";
    private final String userName = "root";
    private final String password = "";

    public Doctor getDoctorByFullName(String fullName) {
        Doctor doctor = null;
        String query = "SELECT * FROM Doctors WHERE fullName = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, fullName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                doctor = new Doctor();
                doctor.setId(resultSet.getString("id"));
                doctor.setFullName(resultSet.getString("fullName"));
                doctor.setPhoneNumber(resultSet.getString("phoneNumber"));
                doctor.setPassword(resultSet.getString("password"));
            } else {
                System.out.println("Doctor with fullName " + fullName + " not found.");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't retrieve Doctor by fullName: ");
        }

        return doctor;
    }


    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM Doctors";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from doctors");

            while (resultSet.next()) {
                Doctor doctor = new Doctor();
                doctor.setId(resultSet.getString("id"));
                doctor.setFullName(resultSet.getString("fullName"));
                doctor.setPhoneNumber(resultSet.getString("phoneNumber"));
                doctor.setPassword(resultSet.getString("password"));
                doctors.add(doctor);
            }
            connection.close();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Couldn't retrieve all Doctors.");
        }

        return doctors;
    }

    public boolean verifyCredentials(String username, String pw) {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, userName, password);
            String sql = "SELECT * FROM doctors WHERE fullName = ? AND password = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, pw);
            rs = stmt.executeQuery();


            return rs.next();
//            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could Not verify user");
            return false;
        }

    }

    public void addNewDoctor(Doctor doctor) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO doctors (fullName, phoneNumber, password) VALUES (?, ?, ?)");
            preparedStatement.setString(1, doctor.getFullName());
            preparedStatement.setString(2, doctor.getPhoneNumber());
            preparedStatement.setString(3, doctor.getPassword());
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't insert a new Doctor.");
        }

    }

    public String addNewPatient(Patient patient) {
        int patientId = -1;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO patients (fullName, age, gender, address, phoneNumber, dateOfBirth, dateJoined, doctorId) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, patient.getFullName());
            preparedStatement.setInt(2, patient.getAge());
            preparedStatement.setString(3, patient.getGender());
            preparedStatement.setString(4, patient.getAddress());
            preparedStatement.setString(5, patient.getPhoneNumber());

            // Convert LocalDate to java.sql.Date
            Date sqlDateOfBirth = Date.valueOf(patient.getDateOfBirth());
            preparedStatement.setDate(6, sqlDateOfBirth);

            Date sqlDateJoined = Date.valueOf(patient.getDateJoined());
            preparedStatement.setDate(7, sqlDateJoined);

            preparedStatement.setString(8, patient.getDocId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 1) {

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    patientId = generatedKeys.getInt(1); // Retrieve the generated ID
                } else {
                    throw new SQLException("Failed to retrieve the generated appointment ID.");
                }
            } else {
                throw new SQLException("Failed to insert appointment, no rows affected.");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't insert a new Patient");
        }
        return Integer.toString(patientId);
    }

    public void updatePatient(Patient patient) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE patients " +
                            "SET fullName = ?, age = ?, gender = ?, address = ?, phoneNumber = ?, dateOfBirth = ? " +
                            "WHERE id = ?"
            );
            preparedStatement.setString(1, patient.getFullName());
            preparedStatement.setInt(2, patient.getAge());
            preparedStatement.setString(3, patient.getGender());
            preparedStatement.setString(4, patient.getAddress());
            preparedStatement.setString(5, patient.getPhoneNumber());
            Date sqlDate = Date.valueOf(patient.getDateOfBirth());
            preparedStatement.setDate(6, sqlDate);
            preparedStatement.setInt(7, patient.getId());

            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't update the patient");
        }
    }

    public void deletePatient(String patientId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM patients WHERE id = ?");
            preparedStatement.setString(1, patientId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't delete the patient");
        }
    }

    public String addNewAppointment(Appointment appointment, String patientId, String doctorId) {
        int appointmentId = -1; // Default value indicating failure

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO appointments (patient_id, short_description, notes, appointment_date, selected_hour, selected_minute, status, payment_status, prescription, doctor_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, patientId);
            preparedStatement.setString(2, appointment.getShortDescription());
            preparedStatement.setString(3, appointment.getNotes());
            Date sqlDate = Date.valueOf(appointment.getDate());
            preparedStatement.setDate(4, sqlDate);
            preparedStatement.setInt(5, appointment.getSelectedHour());
            preparedStatement.setInt(6, appointment.getSelectedMinute());
            preparedStatement.setString(7, appointment.getStatus());
            preparedStatement.setBoolean(8, appointment.isPaymentStatus());
            preparedStatement.setString(9, appointment.getPrescription());
            System.out.println("DOCTOR ID ON INSERTION" + doctorId);
            preparedStatement.setString(10, doctorId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 1) {
                // Retrieve the generated keys
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    appointmentId = generatedKeys.getInt(1); // Retrieve the generated ID
                } else {
                    throw new SQLException("Failed to retrieve the generated appointment ID.");
                }
            } else {
                throw new SQLException("Failed to insert appointment, no rows affected.");
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not add new Appointment");
        }

        return Integer.toString(appointmentId);
    }

    public void deleteAppointment(String appointmentId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM appointments WHERE appointment_id = ?");
            preparedStatement.setString(1, appointmentId);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't delete the appointment");
        }
    }

    public void updateAppointment(Appointment appointment) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE appointments " +
                            "SET notes = ?, prescription = ?, diagnosis = ?, status = ?, payment_status = ? " +
                            "WHERE appointment_id = ?"
            );
            preparedStatement.setString(1, appointment.getNotes());
            preparedStatement.setString(2, appointment.getPrescription());
            preparedStatement.setString(3, appointment.getDiagnosis());
            preparedStatement.setString(4, appointment.getStatus());
            preparedStatement.setBoolean(5, appointment.isPaymentStatus());
            preparedStatement.setString(6, appointment.getId());

            preparedStatement.executeUpdate();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't update the appointment");
        }
    }


    public ObservableList<Patient> getDoctorsPatients(String doctorId) {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM patients WHERE doctorId = ?");
            statement.setString(1, doctorId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Patient patient = new Patient();
//
                patient.setDocId(doctorId);
//
                patient.setId(resultSet.getInt("id"));
                patient.setFullName(resultSet.getString("fullName"));
                patient.setAge(resultSet.getInt("age"));
                patient.setGender(resultSet.getString("gender"));
                patient.setAddress(resultSet.getString("address"));
                patient.setPhoneNumber(resultSet.getString("phoneNumber"));

                // Converting the Date field from the db to LocalDate
                java.sql.Date sqlDateOfBirth = resultSet.getDate("dateOfBirth");
                LocalDate dateOfBirth = sqlDateOfBirth.toLocalDate();
                patient.setDateOfBirth(dateOfBirth);

                // same thing ^^
                java.sql.Date sqlDateJoined = resultSet.getDate("dateJoined");
                LocalDate dateJoined = sqlDateJoined.toLocalDate();
                patient.setDateJoined(dateJoined);

                patient.setAppointmentsList(FXCollections.observableArrayList());
                patients.add(patient);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't retrieve patients for the doctor.");
        }

        return patients;
    }


    public ObservableList<Appointment> getPatientAppointments(int patientId) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointments WHERE patient_id = ?");
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getString("appointment_id"));
                appointment.setShortDescription(resultSet.getString("short_description"));
                appointment.setPatientId(resultSet.getString("patient_id"));
                appointment.setNotes(resultSet.getString("notes"));
                appointment.setDate(resultSet.getDate("appointment_date").toLocalDate());
                appointment.setSelectedHour(resultSet.getInt("selected_hour"));
                appointment.setSelectedMinute(resultSet.getInt("selected_minute"));
                appointment.setStatus(resultSet.getString("status"));
                appointment.setPaymentStatus(resultSet.getBoolean("payment_status"));
                appointment.setPrescription(resultSet.getString("prescription"));
                appointment.setDiagnosis(resultSet.getString("diagnosis"));
                appointments.add(appointment);
            }
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't load appointments");
        }
        return appointments;
    }

    public ObservableList<Appointment> getFilteredList(String chosenStatus, String patientId, String doctorId) {
        ObservableList<Appointment> filteredAppointmentsList = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointments WHERE patient_id = ? AND doctor_id = ? AND status = ?");
            statement.setString(1, patientId);
            statement.setString(2, doctorId);
            statement.setString(3, chosenStatus);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getString("appointment_id"));
                appointment.setShortDescription(resultSet.getString("short_description"));
                appointment.setPatientId(resultSet.getString("patient_id"));
                appointment.setNotes(resultSet.getString("notes"));
                appointment.setDate(resultSet.getDate("appointment_date").toLocalDate());
                appointment.setSelectedHour(resultSet.getInt("selected_hour"));
                appointment.setSelectedMinute(resultSet.getInt("selected_minute"));
                appointment.setStatus(resultSet.getString("status"));
                appointment.setPaymentStatus(resultSet.getBoolean("payment_status"));
                appointment.setPrescription(resultSet.getString("prescription"));
                appointment.setDiagnosis(resultSet.getString("diagnosis"));
                filteredAppointmentsList.add(appointment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't retrieve filtered appointments list");

        }
        return filteredAppointmentsList;
    }

    public List<Integer> getAppointmentHours(LocalDate date) {
        List<Integer> hours = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement statement = connection.prepareStatement("SELECT selected_hour FROM appointments WHERE appointment_date = ? ORDER BY selected_hour");
            java.sql.Date wantedDate = java.sql.Date.valueOf(date);
            statement.setDate(1, wantedDate);
            ResultSet resultSet = statement.executeQuery(); // Execute the query and get the result set

            // Iterate through the result set and add selected_hour values to the list
            while (resultSet.next()) {
                int hour = resultSet.getInt("selected_hour");
                hours.add(hour);
            }

            // Close resources
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not retrieve Hours for the date " + date);
        }

        return hours;
    }

    public boolean checkIfAppointmentTimeExists(LocalDate appointmentDate, int hour, int minute) {
        boolean exists = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM appointments WHERE appointment_date = ? AND selected_hour = ? AND selected_minute = ?");
            java.sql.Date date = java.sql.Date.valueOf(appointmentDate);
            statement.setDate(1, date);
            statement.setInt(2, hour);
            statement.setInt(3, minute);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not check if appointment exists or not.");
        }
        return exists;
    }

    public ObservableList<Appointment> getAllAppointments(String doctorId) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_date DESC, selected_hour, selected_minute");
            statement.setString(1, doctorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getString("appointment_id"));
                appointment.setShortDescription(resultSet.getString("short_description"));
                appointment.setPatientId(resultSet.getString("patient_id"));
                appointment.setNotes(resultSet.getString("notes"));
                appointment.setDate(resultSet.getDate("appointment_date").toLocalDate());
                appointment.setSelectedHour(resultSet.getInt("selected_hour"));
                appointment.setSelectedMinute(resultSet.getInt("selected_minute"));
                appointment.setStatus(resultSet.getString("status"));
                appointment.setPaymentStatus(resultSet.getBoolean("payment_status"));
                appointment.setPrescription(resultSet.getString("prescription"));
                appointments.add(appointment);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could NOT return appointments");
        }

        return appointments;
    }

    public String getPatientName(String patientId) {
        String patientName = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement statement = connection.prepareStatement("SELECT fullName FROM patients WHERE id = ?");
            statement.setString(1, patientId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                patientName = resultSet.getString("fullName");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could NOT return patient name");
        }
        return patientName;
    }

    public int getPatientAge(String patientId) {
        int patientAge = -1; // Use -1 to indicate an error or not found
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement statement = connection.prepareStatement("SELECT age FROM patients WHERE id = ?");
            statement.setString(1, patientId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                patientAge = resultSet.getInt("age");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could NOT return patient age");
        }
        return patientAge;
    }

    public Patient getPatient(String patientId) {
        Patient patient = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, userName, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM patients WHERE id = ?");
            statement.setString(1, patientId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                patient = new Patient();
                patient.setId(resultSet.getInt("id"));
                patient.setFullName(resultSet.getString("fullName"));
                patient.setAge(resultSet.getInt("age"));
                patient.setGender(resultSet.getString("gender"));
                patient.setAddress(resultSet.getString("address"));
                patient.setPhoneNumber(resultSet.getString("phoneNumber"));
                patient.setDateOfBirth(resultSet.getDate("dateOfBirth").toLocalDate());
                patient.setDateJoined(resultSet.getDate("dateJoined").toLocalDate());
                patient.setDocId(resultSet.getString("doctorId"));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could NOT retrieve patient information");
        }
        return patient;
    }


    public static void main(String[] args) {
        DatabaseHandler db = new DatabaseHandler();
        ObservableList<Appointment> list = db.getAllAppointments("1");
        for (Appointment app : list) {
            System.out.println(app.getDate());

        }
    }
}




