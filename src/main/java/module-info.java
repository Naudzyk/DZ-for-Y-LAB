module org.example.habit_trackingzhenya {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens org.example.habit_trackingzhenya to javafx.fxml;
    exports org.example.habit_trackingzhenya;
}