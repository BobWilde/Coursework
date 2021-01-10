package controllers;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import server.Main;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Path("scores/")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)

public class Scores {

    // Get a list of all scores in the Scores table
    @GET
    @Path("list")
    public String ScoresList() {
        System.out.println("Invoked Scores.ScoresList()");
        JSONArray response = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT Users.Username, Scores.Score FROM Scores JOIN Users ON Scores.UserID=Users.UserID");
            ResultSet results = ps.executeQuery();
            while (results.next() == true) {
                JSONObject row = new JSONObject();
                row.put("Username", results.getString(1));
                row.put("Score", results.getInt(2));
                response.add(row);
            }
            return response.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to list items.  Error code xx.\"}";
        }
    }

    @POST
    @Path("add")
    public String ScoresAdd(
            @FormDataParam("Score") Integer Score) {
        System.out.println("Invoked Users.UsersAdd()");
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Scores (GameID, Score) VALUES (?, ?)");
            ps.setInt(1, 1);
            ps.setInt(2, Score);
            ps.execute();
            return "{\"OK\": \"Added user.\"}";
        } catch (Exception exception) {
            System.out.println("Database error: " + exception.getMessage());
            return "{\"Error\": \"Unable to create new item, please see server console for more info.\"}";
        }

    }
}

