module CAF {
	requires javafx.controls;
	requires java.sql;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires java.desktop;
	requires java.net.http;
	requires org.json;
	opens application.controller;
	opens application.dao;
	opens util;
	opens application.model;
	opens application to javafx.graphics, javafx.fxml;
}
