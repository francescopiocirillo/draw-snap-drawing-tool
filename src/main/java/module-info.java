module it.unisa.software_architecture_design.drawsnapdrawingtool {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    opens it.unisa.software_architecture_design.drawsnapdrawingtool to javafx.fxml;
    exports it.unisa.software_architecture_design.drawsnapdrawingtool;
}