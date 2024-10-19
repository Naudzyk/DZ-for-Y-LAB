module org.example.habit_trackingzhenya {
    requires java.sql;
    requires static lombok;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.postgresql.jdbc;
    requires jdk.compiler;
    requires liquibase.core;

    exports org.example.habit_trackingzhenya;
    exports org.example.habit_trackingzhenya.models;
    exports org.example.habit_trackingzhenya.repositories;
    exports org.example.habit_trackingzhenya.services;
    exports org.example.habit_trackingzhenya.controller;
    exports org.example.habit_trackingzhenya.utils;
    exports org.example.habit_trackingzhenya.exception;

}