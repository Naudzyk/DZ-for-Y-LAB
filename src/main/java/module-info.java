module org.example.habit_trackingzhenya {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;


    opens org.example.habit_trackingzhenya to javafx.fxml;
    exports org.example.habit_trackingzhenya;
}