module com.example.task_plan_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;


    opens com.example.task_plan_manager to javafx.fxml;
    exports com.example.task_plan_manager.Controllers;
    opens com.example.task_plan_manager.Controllers to javafx.fxml;
    exports com.example.task_plan_manager.other;
    opens com.example.task_plan_manager.other to javafx.fxml;
    exports com.example.task_plan_manager;
}