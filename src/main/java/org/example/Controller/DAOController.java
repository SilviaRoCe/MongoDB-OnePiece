package org.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.example.dao.OnePieceQueries;
import org.example.database.MongoDBConnection;
import java.io.IOException;

public class DAOController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtPuesto;
    @FXML private TextField txtTripulacion;
    @FXML private TextField txtRecompensa;
    @FXML private TextField txtEdad;
    @FXML private TextField txtFruta;
    @FXML private TextField txtEstado;
    @FXML private TextArea txtMensajesCRUD;

    private MongoCollection<Document> col;

    @FXML
    public void initialize() {
        try {
            this.col = MongoDBConnection.getDatabase("silvia").getCollection("OnePiece");
            txtMensajesCRUD.setText(">>> SISTEMA DE FICHEROS IMPEL DOWN v2.0\n" +
                    ">>> CONEXIÓN ESTABLECIDA.\n" +
                    ">>> ESPERANDO INSTRUCCIONES DEL ALMIRANTE...");

        } catch (Exception e) {
            if (txtMensajesCRUD != null) {
                txtMensajesCRUD.setText("⚠ ERROR CRÍTICO: No hay conexión con la base de datos.");
            }
        }
    }

    @FXML
    public void registrar() {
        if (esCampoVacio(txtNombre) || esCampoVacio(txtPuesto) || esCampoVacio(txtRecompensa) || esCampoVacio(txtEdad)) {
            imprimirError("FALTAN DATOS: Para registrar, necesito al menos Nombre, Puesto, Recompensa y Edad.");
            return;
        }

        try {
            Document doc = new Document("nombre", txtNombre.getText())
                    .append("puesto", txtPuesto.getText())
                    .append("tripulacion", txtTripulacion.getText())
                    .append("recompensa", Long.parseLong(txtRecompensa.getText()))
                    .append("edad", Integer.parseInt(txtEdad.getText()))
                    .append("fruta", txtFruta.getText())
                    .append("estado", txtEstado.getText());

            OnePieceQueries.insertarPirata(col, doc);
            imprimirExito("Pirata " + txtNombre.getText() + " registrado correctamente.");
            limpiarCampos();
        } catch (NumberFormatException e) {
            imprimirError("FORMATO INCORRECTO: 'Recompensa' y 'Edad' deben ser números enteros.");
        } catch (Exception e) {
            imprimirError("Error al registrar: " + e.getMessage());
        }
    }


    @FXML
    public void eliminar() {
        if (esCampoVacio(txtNombre)) {
            imprimirError("FALTA EL NOMBRE: Escribe el nombre del pirata que quieres eliminar.");
            return;
        }

        OnePieceQueries.eliminarPirata(col, txtNombre.getText());
        imprimirExito("Solicitud de eliminación enviada para: " + txtNombre.getText());
        limpiarCampos();
    }

    @FXML
    public void actualizar() {
        if (esCampoVacio(txtNombre)) {
            imprimirError("FALTA EL NOMBRE: Necesito el nombre original para saber a quién actualizar.");
            return;
        }

        try {
            Document doc = new Document("nombre", txtNombre.getText())
                    .append("puesto", txtPuesto.getText())
                    .append("tripulacion", txtTripulacion.getText())
                    .append("recompensa", Long.parseLong(txtRecompensa.getText()))
                    .append("edad", Integer.parseInt(txtEdad.getText()))
                    .append("fruta", txtFruta.getText())
                    .append("estado", txtEstado.getText());

            OnePieceQueries.actualizarPirata(col, txtNombre.getText(), doc);
            imprimirExito("Ficha actualizada para: " + txtNombre.getText());
        } catch (NumberFormatException e) {
            imprimirError("FORMATO INCORRECTO: Revisa que Recompensa y Edad sean números.");
        } catch (Exception e) {
            imprimirError("Error al actualizar: " + e.getMessage());
        }
    }

    @FXML
    public void leerTodos() {
        String lista = OnePieceQueries.leerTodos(col);
        txtMensajesCRUD.setText(lista);
        txtMensajesCRUD.positionCaret(0);
    }

    @FXML
    public void irAInicio(javafx.event.ActionEvent event) { irAPantalla("/fxml/onepiece.fxml", event); }

    @FXML
    public void irACRUD(javafx.event.ActionEvent event) { irAPantalla("/fxml/opcionesDAO.fxml", event); }

    private void irAPantalla(String fxml, javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) txtMensajesCRUD.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean esCampoVacio(TextField campo) {
        return campo.getText() == null || campo.getText().trim().isEmpty();
    }

    private void limpiarCampos() {
        txtNombre.clear(); txtPuesto.clear(); txtTripulacion.clear();
        txtRecompensa.clear(); txtEdad.clear(); txtFruta.clear(); txtEstado.clear();
    }

    private void imprimirError(String msg) {
        txtMensajesCRUD.setText("⚠ ERROR: " + msg);
    }

    private void imprimirExito(String msg) {
        txtMensajesCRUD.setText("✔ ÉXITO: " + msg);
    }
}