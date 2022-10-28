package chess15.util;

import chess15.Board;
import chess15.Piece;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonToBoard {

    public static Board jsonToBoard(String json) {
        Board board = new Board();

        // open the json file located in resources with the name of json
        // read the file line by line
        // for each line, get the x and y coordinates and the piece type
        // create a new piece with the given coordinates and type
        // add the piece to the board
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("resources/gamemodes/" + json));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray pieces = (JSONArray) jsonObject.get("pieces");
            for (Object piece : pieces) {
                System.out.println(piece);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return board;
    }
}
