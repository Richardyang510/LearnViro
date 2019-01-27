package com.words.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import com.google.cloud.translate.*;

import org.apache.commons.codec.binary.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Path("/words")
public class WordsEndpoint {

    ObjectMapper mapper = new ObjectMapper();

    public WordsEndpoint() {
    }

    @POST
    @Path("recog")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public JsonNode recognize(JsonNode payload) {
        System.out.println("post request called");
        ObjectNode out = JsonNodeFactory.instance.objectNode();
        //System.out.println(payload.get("message"));

        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            // The path to the image file to annotate

            // String fileName = "resource/s-l300.jpg";
            // java.nio.file.Path path = Paths.get(fileName);
            // byte[] data = Files.readAllBytes(path);
            // ByteString imgBytes = ByteString.copyFrom(data);

            String base64String = payload.get("data").toString();
            System.out.println(base64String);

            byte[] data = Base64.decodeBase64(base64String.getBytes());
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            String word = "", tword = "";

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    out.put("status", "not OK");
                    out.put("error", res.getError().getMessage());
                }

                word = res.getLabelAnnotationsList().get(0).getDescription();

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) ->
                            System.out.printf("%s : %s\n", k, v.toString()));

                }
            }

            System.out.println(word);

            Translate translate = TranslateOptions.getDefaultInstance().getService();
            Translation translation = translate.translate(word,
                    Translate.TranslateOption.sourceLanguage("en"),
                    Translate.TranslateOption.targetLanguage("es"),
                    Translate.TranslateOption.model("base"));

            tword = translation.getTranslatedText();
            System.out.println("Translated Text: " + tword);

            HttpURLConnection connection = null;

            try {

                String urls[] = {"https://api.datamuse.com/words?ml=" + tword + "&v=es",
                        "https://api.datamuse.com/words?sl=" + tword + "&v=es",
                        "https://api.datamuse.com/words?sp=" + tword + "&v=es"};
                ArrayNode choices = JsonNodeFactory.instance.arrayNode();
                choices.add(tword);

                for(int i = 0;i < 4;++i){
                    //Create connection
                    URL url = new URL(urls[i]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    connection.setUseCaches(false);
                    connection.setDoOutput(true);

                    //Send request
                    DataOutputStream wr = new DataOutputStream(
                            connection.getOutputStream());
                    wr.close();

                    //Get Response
                    InputStream is = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                    StringBuilder getResponse = new StringBuilder(); // or StringBuffer if Java version 5+
                    String line;
                    while ((line = rd.readLine()) != null) {
                        getResponse.append(line);
                        getResponse.append('\r');
                    }
                    rd.close();

                    String dictionaryApiResult = getResponse.toString();

                    ArrayNode arrayNode = mapper.readValue(dictionaryApiResult, ArrayNode.class);

                    int index = Math.min(5, arrayNode.size());
                    choices.add(arrayNode.get(index).get("word").toString());
                }

                out.set("data", choices);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }


        } catch (IOException e) {
            //e.printStackTrace();
            out.put("status", "not OK");
            out.put("error", e.getMessage());
        }

        out.put("status", "OK");
        return out;
    }

}
