import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class RemapAccess {

    public static void productPost(String credentials) throws IOException {

        String endpoint = "https://online-api-4.testms.lognex.ru/api/remap/1.1/entity/product";
        String body = "{\"name\": \"мандарины\"}";
        postEntity(endpoint, credentials, body);



    }

    public static void customerOrderPost(String credentials) throws IOException, InterruptedException {

        String organization = organizationGet(credentials);
        String agent = agentGet(credentials);
        String product = productGet(credentials);

        String endpoint = "https://online-api-4.testms.lognex.ru/api/remap/1.1/entity/customerorder";
        String body = "{" +
                "\"name\": \"00003\"," +
                "\"organization\": {\n" +
                "    \"meta\": " + organization+"},"+
                "\"agent\": {\n" +
                "    \"meta\":" + agent+"},"+
                "\"positions\": [" +
                "{\"quantity\": 10, \"price\": 100," +
                "\"assortment\": {"+
                "    \"meta\": " + product+"}"+
                "}"+
                "]"+
                "}";
        postEntity(endpoint, credentials, body);

    }

    public static String organizationGet(String credentials) throws IOException {

        String endpoint = "https://online-api-4.testms.lognex.ru/api/remap/1.1/entity/organization";
        String type = "организация";
        int index = 0;
        return getEntity(endpoint, credentials, type, index);

    }

    public static String agentGet(String credentials) throws IOException {

        String endpoint = "https://online-api-4.testms.lognex.ru/api/remap/1.1/entity/counterparty";
        String type = "контрагент";
        int index = 1;
        return getEntity(endpoint, credentials, type, index);

    }

    public static String productGet(String credentials) throws IOException, InterruptedException {

        String endpoint = "https://online-api-4.testms.lognex.ru/api/remap/1.1/entity/product";
        String type = "товар";
        int index = 0;
        return getEntity(endpoint, credentials, type, index);
    }


    public static void postEntity(String endpoint, String credentials, String body) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", credentials);

        con.setDoOutput(true);

        String jsonInputString = body;

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            //System.out.println(response.toString());
        }

        con.disconnect();
    }

    public static String getEntity(String endpoint, String credentials, String type, int index) throws IOException {

        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Lognex-Pretty-Print-JSON", "true");
        con.setRequestProperty("Authorization", credentials);

        int status = con.getResponseCode();

        System.out.println("Запрашиваемая сущность:  "+type+"... Статус: "+status);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        //System.out.println(content);


        JSONObject object = (JSONObject) new JSONTokener(content.toString()).nextValue();
        JSONObject meta = (JSONObject) object.get("meta");
        int size = meta.getInt("size");
        if (type.equals("товар") && size == 0) {
            System.out.println(type+" еще не создан");
            //productPost(credentials);
            //return null;
        }
        JSONArray rows = object.getJSONArray("rows");
        JSONObject entityFull = (JSONObject) rows.get(index);
        JSONObject entityMeta = (JSONObject) entityFull.get("meta");

        //System.out.println(meta.toString());

        con.disconnect();

        String entity = entityMeta.toString();

        return entity;
    }

}

