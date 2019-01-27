package com.words.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import org.apache.commons.codec.binary.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Path("/words")
public class WordsEndpoint {

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

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) ->
                            out.put(k.getName(),v.toString()));
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
