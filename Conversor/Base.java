package Conversor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

import static javax.swing.JOptionPane.showMessageDialog;

public class Base {
    public static void main(String[] args) throws IOException, InterruptedException {
        int continuarConversion = JOptionPane.YES_OPTION; // inicia bucle
        while (continuarConversion == JOptionPane.YES_OPTION) {
            //menu
            String[] opciones = {"Conversor de Divisas", "Salir"};
            String opcionSeleccionada = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecciona una opción: ",
                    "Conversor de Divisas",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0] //opcion por defecto
            );

            //salir si cancela o elige salir
            if (opcionSeleccionada == null || opcionSeleccionada.equals("Salir")) {
                break;
            }


            //obtener cantidad a convertir
            String cantidadSt = JOptionPane.showInputDialog("Ingresa la cantidead de dinero que deseas convertir: ");
            double cantidad = Double.parseDouble(cantidadSt);

            //obtener codigos de moneda local
            SupportedCodes supportedCodes = new SupportedCodes();
            List<String> codigosMoneda = supportedCodes.obtenerCodigosMoneda();
            String[] opcionesMoneda = codigosMoneda.toArray(new String[0]);


            String monedaLocal = (String) JOptionPane.showInputDialog(
                    null,
                    "Selecciona tu moneda local:",
                    "Conversor de Moneda",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcionesMoneda,
                    opcionesMoneda[0] // Opción por defecto
            );


            if (monedaLocal != null)
                showMessageDialog(null, "Vas a convertir: " + cantidad + monedaLocal);

            String monedaConvertir;
            do {
                monedaConvertir = (String) JOptionPane.showInputDialog(
                        null,
                        "Selecciona la moneda a la cual deseas convertir: ",
                        "Conversor de Moneda",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opcionesMoneda,
                        opcionesMoneda[0] // Opción por defecto
                );

                if (monedaConvertir != null && monedaConvertir.equals(monedaLocal)) {
                    JOptionPane.showMessageDialog(null, "La divisa a la que deseas convertir es la misma que tu moneda local");
                }
            } while (monedaConvertir != null && monedaConvertir.equals(monedaLocal));

            String enlace = "https://v6.exchangerate-api.com/v6/8ef607f905d843c40fcc2456/pair/" + monedaLocal + "/" + monedaConvertir;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(enlace))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            double conversionRate = json.get("conversion_rate").getAsDouble();
            Convertir conversionValor = new Convertir(conversionRate);
            double cantidadConvertida = (cantidad) * conversionRate;
            String cantidadConvertidaFormat = String.format("%.3f", cantidadConvertida); //redondea cantidadConvertida a solo 3 decimales


            JOptionPane.showMessageDialog(null, cantidad + monedaLocal + "equivalen a " + cantidadConvertidaFormat + monedaConvertir);


            //preguntar si desea continuar
            continuarConversion = JOptionPane.showConfirmDialog(
                    null,
                    "¿Deseas hacer otra conversión?",
                    "Continuar",
                    JOptionPane.YES_NO_OPTION
            );
            JOptionPane.showMessageDialog(null, "¡Gracias por usar el conversor de moneda!");


        }
    }
}









