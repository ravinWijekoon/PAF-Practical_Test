package com.HealthcareSystem.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Doctor {

	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/paf?useTimeZone=true&serverTimezone=UTC",
					"root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String insertDoctor(String name, String spec, String reg, String addr, String phn) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for inserting.";
			}
			// create a prepared statement
			String query = " insert into doctor(`doctor_name`,`specialization`,`regNo`,`address`,`phone`)"
					+ " values (?, ?, ?, ?, ?)";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			//preparedStmt.setInt(1, doctor_id);
			preparedStmt.setString(1, name);
			preparedStmt.setString(2, spec);
			preparedStmt.setString(3, reg);
			preparedStmt.setString(4, addr);
			preparedStmt.setString(5, phn);

			// execute the statement
			preparedStmt.execute();
			con.close();
			
			String newDoctors = readDoctors();
			output = "{\"status\":\"success\", \"data\": \"" + newDoctors + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error while insering the doctor information.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String readDoctors() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<table border=\"1\"><tr><th>Doctor Name</th><th>Specialization</th><th>Registration No.</th><th>Address</th><th>Phone</th><th>Update</th><th>Delete</th></tr>";
			String query = "select * from doctor";
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next()) {
				String DoctorID = Integer.toString(rs.getInt("doctor_id"));
				String Name = rs.getString("doctor_name");
				String specialization = rs.getString("specialization");
				String regNo = rs.getString("regNo");
				String address = rs.getString("address");
				String phone = rs.getString("phone");
				// Add into the html table
				
				output += "<tr><td><input id='hidDoctorIDUpdate' name='hidDoctorIDUpdate' type='hidden' value='" + DoctorID + "'></td>";
				output += "<tr><td>" + Name + "</td>";
				output += "<td>" + specialization + "</td>";
				output += "<td>" + regNo + "</td>";
				output += "<td>" + address + "</td>";
				output += "<td>" + phone + "</td>";
				// buttons
				
				 output +=
				 "<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td><td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-doctorid='"+ DoctorID + "'>" + "</td></tr>";
				
				 
			}
			con.close();
			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the doctor information.";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String updateDoctorDetails(String ID, String name, String spec, String reg, String addr, String phn) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for updating.";
			}
			// create a prepared statement
			String query = "UPDATE doctor SET doctor_name=?,specialization=?,regNo=?,address=?,phone=?WHERE doctor_id =?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setString(1, name);
			preparedStmt.setString(2, spec);
			preparedStmt.setString(3, reg);
			preparedStmt.setString(4, addr);
			preparedStmt.setString(5, phn);
			preparedStmt.setInt(6, Integer.parseInt(ID));
			// execute the statement
			preparedStmt.execute();
			con.close();
			
			String newDoctors = readDoctors();
			output = "{\"status\":\"success\", \"data\": \"" + newDoctors + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\":\"Error While updating the doctor information.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String deleteDoctorDetails(String doctor_id) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}
			// create a prepared statement
			String query = "delete from doctor where doctor_id=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(doctor_id));
			// execute the statement
			preparedStmt.execute();
			con.close();
			
			String newDoctors = readDoctors();
			output = "{\"status\":\"success\", \"data\": \"" + newDoctors + "\"}";
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while deleteing doctor info.\"}";
			System.err.println(e.getMessage());
		}
		return output;
	}
	

	
	public String readSpecifiedDoctors(String id) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading specified doctor.";
			}
			// Prepare the html table to be displayed
			output = "<table border=\"1\"><tr><th>Doctor Name</th><th>Specialization</th><th>Registration No.</th><th>Address</th><th>Phone</th></tr>";
			String query = "select * from doctor where doctor_id="+id;
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
			// iterate through the rows in the result set
			if(rs.next()) {
				// String DoctorID = Integer.toString(rs.getInt("doctor_id "));
				String Name = rs.getString("doctor_name");
				String specialization = rs.getString("specialization");
				String regNo = rs.getString("regNo");
				String address = rs.getString("address");
				String phone = rs.getString("phone");
				// Add into the html table
				output += "<tr><td>" + Name + "</td>";
				output += "<td>" + specialization + "</td>";
				output += "<td>" + regNo + "</td>";
				output += "<td>" + address + "</td>";
				output += "<td>" + phone + "</td>";

				// buttons
				/*
				 * output +=
				 * "<td><input name=\"btnUpdate\" type=\"button\"value=\"Update\" class=\"btn btn-secondary\"></td>"
				 * + "<td><form method=\"post\" action=\"items.jsp\">" +
				 * "<input name=\"btnRemove\" type=\"submit\" value=\"Remove\"class=\"btn btn-danger\">"
				 * + "<input name=\"userID\" type=\"hidden\" value=\"" + userID + "\">" +
				 * "</form></td></tr>";
				 */
			}
			con.close();
			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the doctor information.";
			System.err.println(e.getMessage());
		}
		return output;
	}
	
	
	public String readAppointmentsForSpecifiedDoctors(String id) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading appointments for specified doctor.";
			}
			// Prepare the html table to be displayed
			output = "<table border=\"1\"><tr><th>Appointment ID</th><th>Patient ID</th><th>Patient Name</th><th>Doctor ID</th><th>Hospital Name</th><th>Appointment Time</th><th>Appointment Date</th><th>Ward No</th></tr>";
			String query = "select * from appointments where doctor_id="+id;
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
			// iterate through the rows in the result set
			while (rs.next())
			{
				String appointment_id = Integer.toString(rs.getInt("appointment_id"));
				String user_id = Integer.toString(rs.getInt("userid"));
				String username = rs.getString("username");
				String doctor_id = Integer.toString(rs.getInt("doctor_id"));
				//String doctor_name = rs.getString("doctor_name");
				//String hospital_id = Integer.toString(rs.getInt("hospital_id"));
				String hospital_name = rs.getString("hospital_name");
				String appointment_time = rs.getString("appointment_time");
				String appointment_date = rs.getString("appointment_date");
				String WardNo = rs.getString("WardNo");
	
				// Add into the html table
				output += "<tr><td>" + appointment_id + "</td>";
				output += "<td>" + user_id + "</td>";
				output += "<td>" + username + "</td>";
				output += "<td>" + doctor_id + "</td>";
				//output += "<td>" + doctor_name + "</td>";
				//output += "<td>" + hospital_id + "</td>";
				output += "<td>" + hospital_name + "</td>";
				output += "<td>" + appointment_time + "</td>";
				output += "<td>" + appointment_date + "</td>";
				output += "<td>" + WardNo + "</td>";
	
			}
			con.close();
			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the appointment information for specified doctor.";
			System.err.println(e.getMessage());
		}
		return output;
	}

}
