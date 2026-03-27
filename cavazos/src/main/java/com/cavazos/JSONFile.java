package com.cavazos;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public final class JSONFile {

    private JSONFile() {
        // Utility class
    }

    // read a json array file from classpath resources
    public static JSONArray readArray(String resourceName) {
        JSONParser jsonParser = new JSONParser();

        try (InputStream input = JSONFile.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                return null;
            }
            try (Reader reader = new InputStreamReader(input)) {
                Object obj = jsonParser.parse(reader);
                return (JSONArray) obj;
            }
        } catch (IOException | ParseException e) {
            System.err.println("Failed to parse resource: " + resourceName);
            return null;
        }
    }
}
