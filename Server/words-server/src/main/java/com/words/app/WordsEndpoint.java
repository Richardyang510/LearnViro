package com.words.app;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import org.apache.commons.codec.binary.Base64;

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
    public void recognize() {
        System.out.println("post request called");
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            // The path to the image file to annotate
            /*
            String fileName = "resource/s-l300.jpg";
            java.nio.file.Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);
            */

            String string = "iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo1M0FFRDEyOTM0MTcxMUUzODk4NUFFRjVCRDAxRTdFNCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo1M0FFRDEyQTM0MTcxMUUzODk4NUFFRjVCRDAxRTdFNCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjUzQUVEMTI3MzQxNzExRTM4OTg1QUVGNUJEMDFFN0U0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjUzQUVEMTI4MzQxNzExRTM4OTg1QUVGNUJEMDFFN0U0Ii8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+mJNOYAAAAlpJREFUeNrsV00oRFEUnmFWFhPlJylFKTRJSBa2wkopbLAhZSF/qbGwUrJAbEyUUmJWZEGxsJeFhYWNIgqF8tOUmvB8p87Tcd37PMaYBbe+znnnnr977pl773gty/IkciR5Ejz+E0h4Al43Sgf5xWUgIaASuARGSo4PFxSdDpBRIBvYA7qhsx9zAnCcCnIEpCtTdQiwzTq1IFvK/A1QAJ27WLegXhOcRpuBt0c628bcAyadZAP/Jf9uEqAyRzTyVQNvjwjbOvcA9i8FdBxoBB6AaezbvNIHtMcky2XRFHQGFZ1JkAH+PAO67B4ROl0gfYAfWAOCVIEZoAfIAYqAOSi2SEN21C9EusaSsn5NcPI5xzFyOOYMJdCqcdaukZULvlkz32zQdfLZauoB65MEAlhRoVgd8YFPErBMTbiokS9pZKXKd5OB1+mafC5SAr3ABHAhJhqxKq9YIe1ZlkPJ1S3JYhvb3stNbo8LjtnrQ7NEwQxBaRb0WDgkftihpHIbAoYtO2d+TEmyGnFP3p0DLNgVSkEE6GS+wtArTZrye6QN+wgK+a4dnIZPMVoBqsR3CA5ODRUw/RreKgDbGr7E1Bj6ywgGmXzbyV8HHU7PQNoXb9pbPqL9QvZCtyUqcKU9inliR3Hk/0ZwD9v4FdmODG66C8JxfH+E3VxGdEZH4xA8yr6dE0CJ7kE245DAJvt2dR2Hf6P8TglsGN4A3x0R9ukuAZTqEWT9BxNYZ58fhs/BaBloAA6BE35kXPO9T3ji88F+kpGvVEYGP17ygGL2pX8R/f83/PMJvAowABJ6rfzLuIqkAAAAAElFTkSuQmCC"

            byte[] data = Base64.decodeBase64(string.getBytes());
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
                    return;
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) ->
                            System.out.printf("%s : %s\n", k, v.toString()));
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
