import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Scanner;

//javac ChatGPTAPIExample2.java ; java ChatGPTAPIExample2 

public class ChatGPTAPIExample2 {

   public static String chatGPT(String prompt) {
       String url = "https://api.openai.com/v1/chat/completions";
       String apiKey = "sk-4ABWxxxxxxxxxxxxxxxxxxxxxxxxxx7u9";
       String model = "gpt-3.5-turbo";

       try {
           URL obj = new URL(url);
           HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
           connection.setRequestMethod("POST");
           connection.setRequestProperty("Authorization", "Bearer " + apiKey);
           connection.setRequestProperty("Content-Type", "application/json");

           // The request body
           String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
           connection.setDoOutput(true);
           OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
           writer.write(body);
           writer.flush();
           writer.close();

           // Response from ChatGPT
           BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
           String line;

           StringBuffer response = new StringBuffer();

           while ((line = br.readLine()) != null) {
               response.append(line);
           }
           br.close();

           System.out.println("\n\n"+response+"\n\n");


        JSONObject jobj = new JSONObject(""+response+"");

        System.out.println(jobj.toString());
        

        // Einzelne Felder extrahieren
        String id = jobj.getString("id");
        String object = jobj.getString("object");
        long created = jobj.getLong("created");
        String gptmodel = jobj.getString("model");

        // Das "choices" Array und das darin enthaltene "message" Objekt extrahieren
        JSONArray choicesArray = jobj.getJSONArray("choices");
        JSONObject firstChoice = choicesArray.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        String role = message.getString("role");
        String content = message.getString("content");

        // Die "usage" Daten extrahieren
        JSONObject usage = jobj.getJSONObject("usage");
        int promptTokens = usage.getInt("prompt_tokens");
        int completionTokens = usage.getInt("completion_tokens");
        int totalTokens = usage.getInt("total_tokens");

        // Ausgabe
        System.out.println("ID: " + id);
        System.out.println("Object: " + object);
        System.out.println("Created: " + created);
        System.out.println("Model: " + gptmodel);
        System.out.println("Role: " + role);
        System.out.println("Content: " + content);
        System.out.println("Prompt Tokens: " + promptTokens);
        System.out.println("Completion Tokens: " + completionTokens);
        System.out.println("Total Tokens: " + totalTokens);
    
    
        sendPostRequest(content,"https://www.deinblog.de/..."); //beispiel

           // calls the method to extract the message.
           return extractMessageFromJSONResponse(response.toString());

       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }

   public static String extractMessageFromJSONResponse(String response) {
       int start = response.indexOf("content")+ 11;

       int end = response.indexOf("\"", start);

       return response.substring(start, end);

   }

   public static void main(String[] args) {

 Scanner scanner = new Scanner(System.in);  // Erstellen eines Scanner-Objekts

        System.out.println("Bitte geben Sie etwas ein:");
        String userInput = scanner.nextLine();  // Warten auf Benutzereingabe

        System.out.println("Sie haben eingegeben: " + userInput);

        scanner.close();  // Schließen des Scanner-Objekts

        String frage = userInput; //"welche probleme haben typische mittelständische firmen mit IT, internet, marketing usw ? Erstelle eine Ausführliche Liste und beschreibe diese ausführlich ";

       System.out.println(chatGPT(frage+" Aus der Antwort erstelle ein json mit 2 Elementen. Einem title das eine reisserische Ueberschrift zum Thema enthält mit den wichtigsten Keywords und ein Element namens content welches die Antwort enthält."));

   }
   
   
    public static void sendPostRequest(String content, String urlString) {
        try {
            // URL des WordPress-Plugins
            URL url = new URL(urlString);
            
            // Erstellen des JSON-Objekts
            String jsonPayload = content; //"{\"content\":\"" + content + "\"}";

            // Öffnen der Verbindung
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            //connection.setRequestProperty("Host", "www.meinblog.de"); //ggf wichtig je nach server

            // Senden des JSON-Objekts
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Überprüfen der Antwort
/*            int responseCode = connection.getResponseCode();
            System.out.println("HTTP Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Beitrag erfolgreich gesendet!");
            } else {
                System.out.println("Fehler beim Senden des Beitrags.");
            }
  */
  
            // Antwort des Servers lesen
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // Ausgeben der Antwort
            System.out.println("Serverantwort: " + response.toString());
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
}
