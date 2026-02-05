package org.example.extras;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NoLanzar extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Carga el archivo FXML
        // Asegúrate de que onepiece.fxml esté en la carpeta 'resources' de tu proyecto
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Inicio.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("One Piece - Base de Datos");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);

    }



}