package com.example.TopScore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class TopScoreApplication {

	public static void main(String[] args) {
		var url = "jdbc:h2:mem:default";

		try (var con = DriverManager.getConnection(url);
			 var stm = con.createStatement();
			 var rs = stm.executeQuery("SELECT 1+1")) {

			if (rs.next()) {

				System.out.println(rs.getInt(1));
			}

		} catch (SQLException ex) {

			var lgr = Logger.getLogger(TopScoreApplication.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		}
		SpringApplication.run(TopScoreApplication.class, args);
	}

}
