package org.example.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson; // Importante según apuntes
import java.util.Arrays;

public class OnePieceQueries {

    public static String getMediaEdad(MongoCollection<Document> col) {
        StringBuilder sb = new StringBuilder();
        Bson etapaGroup = Aggregates.group("$tripulacion", Accumulators.avg("mediaEdad", "$edad"));
        Bson etapaSort = Aggregates.sort(Sorts.descending("mediaEdad"));

        MongoIterable<Document> res = col.aggregate(Arrays.asList(etapaGroup, etapaSort));

        for (Document doc : res) {
            sb.append("Tripulación: ").append(doc.getString("_id"))
                    .append(" | Media: ").append(String.format("%.2f", doc.getDouble("mediaEdad"))).append("\n");
        }
        return sb.toString();
    }

    public static String subirRecompensaLuffy(MongoCollection<Document> col) {
        Bson filtro = Filters.eq("nombre", "Monkey D. Luffy");
        Bson actualizacion = Updates.inc("recompensa", 500000000L);
        col.updateOne(filtro, actualizacion);
        return "¡La recompensa de Luffy ha subido 500 Millones!";
    }

    public static String getCapitanesVivos(MongoCollection<Document> col) {
        StringBuilder sb = new StringBuilder();
        Bson filtro = Filters.and(Filters.eq("puesto", "Capitán"), Filters.eq("estado", "Vivo"));

        MongoIterable<Document> res = col.find(filtro);
        for (Document doc : res) {
            sb.append("Capitán: ").append(doc.getString("nombre")).append("\n");
        }
        return sb.toString();
    }

    public static void insertarPirata(MongoCollection<Document> col, Document pirata) {
        col.insertOne(pirata);
    }

    public static void eliminarPirata(MongoCollection<Document> col, String nombre) {
        col.deleteOne(Filters.eq("nombre", nombre));
    }

    public static void actualizarPirata(MongoCollection<Document> col, String nombre, Document datosNuevos) {
        col.updateOne(Filters.eq("nombre", nombre), new Document("$set", datosNuevos));
    }

    public static String leerTodos(MongoCollection<Document> col) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== BASE DE DATOS CRIMINAL - VISTA OFICIAL ===\n\n");

        for (Document doc : col.find()) {
            String nombre = doc.getString("nombre");
            String puesto = doc.getString("puesto");
            String banda = doc.getString("tripulacion");

            Object recompensaObj = doc.get("recompensa");
            String recompensa = (recompensaObj != null) ? recompensaObj.toString() : "0";

            Object edadObj = doc.get("edad");
            String edad = (edadObj != null) ? edadObj.toString() : "?";

            Object frutaObj = doc.get("fruta");
            String fruta = (frutaObj != null) ? frutaObj.toString() : "Ninguna";

            String estado = doc.getString("estado");
            String estadoStr = (estado != null) ? estado.toUpperCase() : "DESC.";

            sb.append("─────────────────────────────────────────────────────────────────────────────────────\n");
            sb.append(String.format("☠ %-35s │ 💰 %25s ฿ │ %s\n",
                    nombre.toUpperCase(), recompensa, estadoStr));

            sb.append(String.format("   Cargo: %-25s │ Banda: %-25s\n", puesto, banda));
            sb.append(String.format("   Edad:  %-3s                         │ Fruta: %s\n", edad, fruta));
        }
        sb.append("─────────────────────────────────────────────────────────────────────────────────────\n");
        return sb.toString();
    }

    public static String getTop5MasBuscados(MongoCollection<Document> col) {
        StringBuilder sb = new StringBuilder();

        Bson etapaMatch = Aggregates.match(Filters.eq("estado", "Vivo"));
        Bson etapaSort = Aggregates.sort(Sorts.descending("recompensa"));
        Bson etapaLimit = Aggregates.limit(5);

        MongoIterable<Document> res = col.aggregate(Arrays.asList(etapaMatch, etapaSort, etapaLimit));

        for (Document doc : res) {
            sb.append("☠ ").append(doc.getString("nombre"))
                    .append(" : ").append(doc.get("recompensa")).append(" Berries\n");
        }
        return sb.toString();
    }

    public static String getRecompensasAltas(MongoCollection<Document> col) {
        StringBuilder sb = new StringBuilder();
        Bson filtro = Filters.gt("recompensa", 1000000L);

        MongoIterable<Document> res = col.find(filtro).sort(Sorts.descending("recompensa"));

        int contador = 0;
        for (Document doc : res) {
            sb.append(doc.getString("nombre"))
                    .append(" - ").append(doc.get("recompensa")).append("\n");
            contador++;
        }
        if (contador == 0) sb.append("No hay piratas con tanta recompensa.");
        return sb.toString();
    }
}