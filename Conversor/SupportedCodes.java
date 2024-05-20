package Conversor;



import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

    public class   SupportedCodes {
        public List<String> obtenerCodigosMoneda() throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v6.exchangerate-api.com/v6/8ef607f905d843c40fcc2456/codes?=&="))
                    .header("User-Agent", "insomnia/9.1.1")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonArray supportedCodes = json.getAsJsonArray("supported_codes");

            // extraer y mostrar los supported codes
            List<String> codigosMoneda = new ArrayList<String>();
            for (JsonElement codeElement : supportedCodes) {
                JsonArray codeArray = codeElement.getAsJsonArray();
                String code = codeArray.get(0).getAsString(); //el codigo esta en pos 1
                codigosMoneda.add(code);
            }


            return codigosMoneda;
        }
}
