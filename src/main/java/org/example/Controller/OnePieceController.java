package org.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.dao.OnePieceQueries;
import org.example.database.MongoDBConnection;
import javafx.stage.Stage;

import java.io.IOException;

public class OnePieceController {


    @FXML private TextArea txtPantalla;
    private MongoCollection<Document> col;

    @FXML
    public void initialize() {
        try {
            this.col = MongoDBConnection.getDatabase("silvia").getCollection("OnePiece");
            txtPantalla.setText(">>> Sistema Marine OS v1.0 Conectado.");
        } catch (Exception e) {
            txtPantalla.setText("Error de conexión con Atlas: " + e.getMessage());
        }
    }


    @FXML
    public void onMediaEdadClick() {
        txtPantalla.setText("--- MEDIA DE EDAD ---\n");
        txtPantalla.appendText(OnePieceQueries.getMediaEdad(col));
        txtPantalla.positionCaret(0);
    }

    @FXML
    public void onSubirLuffyClick() {
        String msg = OnePieceQueries.subirRecompensaLuffy(col);
        txtPantalla.setText(msg);
        txtPantalla.positionCaret(0);
    }

    @FXML
    public void onTop5Click() {
        txtPantalla.setText("--- ☠ LOS 5 MÁS BUSCADOS ☠ ---\n");
        txtPantalla.appendText(OnePieceQueries.getTop5MasBuscados(col));
        txtPantalla.positionCaret(0);
    }

    @FXML
    public void onRecompensasAltasClick() {
        txtPantalla.setText("--- 💰 RECOMPENSAS > 1 MILLÓN 💰 ---\n");
        txtPantalla.appendText(OnePieceQueries.getRecompensasAltas(col));
        txtPantalla.positionCaret(0);
    }

    @FXML
    public void onCapitanesClick() {
        txtPantalla.setText("--- ⚓ CAPITANES VIVOS ⚓ ---\n");
        txtPantalla.appendText(OnePieceQueries.getCapitanesVivos(col));
        txtPantalla.positionCaret(0);
    }


    @FXML
    public void irAInicio(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/onepiece.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtPantalla.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void irACRUD(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/opcionesDAO.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtPantalla.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}