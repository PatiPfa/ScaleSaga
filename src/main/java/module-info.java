module com.example.memoryprototyp1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.memoryprototyp1 to javafx.fxml;
    exports com.example.memoryprototyp1;
    exports com.example.memoryprototyp1.GameModi;
    opens com.example.memoryprototyp1.GameModi to javafx.fxml;
}