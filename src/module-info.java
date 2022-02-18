module CAF {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires java.desktop;
	requires org.apache.poi.ooxml;
	requires org.apache.poi.poi;
	
	opens application.controller;
	opens application.dao;
	opens util;
	opens application.model;
	opens application to javafx.graphics, javafx.fxml;
}
