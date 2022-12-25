package chess15.util;

import chess15.*;
import chess15.gamemodes.JSONGrabber;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class is a utility class that converts a JSON file to a Board object.
 */
public class JsonToBoard {
    /**
     * Converts a JSON file to a Board object.
     * @param json The path to the JSON file.
     * @return The Board object.
     * @throws Exception If the JSON file is invalid.
     */
    public static Board jsonToBoard(String json) {
        Board board = new Board();
        JSONParser parser = new JSONParser();

        try {
//            URL url = JSONGrabber.getInstance().getClass().getResource(json);
//            Object obj = parser.parse(new FileReader(url.getFile()));
//            JSONObject jsonObject = (JSONObject) obj;
//            JSONArray pieces = (JSONArray) jsonObject.get("pieces");

            InputStream inputStream = JSONGrabber.getInstance().getClass().getResource(json).openStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(inputStream);
            JsonNode piecesNode = root.get("pieces");

            if (piecesNode.isArray()) {
                ArrayNode piecesArray = (ArrayNode) piecesNode;
                Iterator<JsonNode> pieces = piecesArray.elements();
                while (pieces.hasNext()) {
                    JsonNode piece = pieces.next();
                    Piece p = new Piece(Objects.equals(piece.get("color").asText(), "white") ? Piece.Color.WHITE : Piece.Color.BLACK,
                            getPieceType(piece.get("look").asText()),
                            getMoveSet(piece.get("moves").asText()),
                            piece.get("isKing").asBoolean());

                    Vector2 pos = new Vector2(piece.get("column").intValue(), piece.get("row").intValue());
                    board.elements[pos.x][pos.y] = p;
                }
            }

//            for (Object piece : pieces) {
//                JSONObject pieceObject = (JSONObject) piece;
//                Piece piece1 = new Piece(Objects.equals((String) pieceObject.get("color"), "white") ? Piece.Color.WHITE : Piece.Color.BLACK,
//                        getPieceType((String) pieceObject.get("look")),
//                        getMoveSet((String) pieceObject.get("moves")),
//                        (Boolean) pieceObject.get("isKing"));
//
//                Vector2 position = new Vector2(((Long) pieceObject.get("column")).intValue(),((Long) pieceObject.get("row")).intValue());
//                board.elements[position.x][position.y] = piece1;
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return board;
    }

    /**
     * Gets the Piece type from a String.
     * @param type A string with the lowercase name of the piece type.
     * @return The Piece type.
     */
    protected static Piece.Type getPieceType(String type) {
        return switch (type) {
            case "pawn" -> Piece.Type.PAWN;
            case "rook" -> Piece.Type.ROOK;
            case "knight" -> Piece.Type.KNIGHT;
            case "bishop" -> Piece.Type.BISHOP;
            case "queen" -> Piece.Type.QUEEN;
            case "king" -> Piece.Type.KING;
            default -> null;
        };
    }

    /**
     * Gets the MoveSet from a String.
     * @param type A string with the lowercase name of the move set.
     * @return The MoveSet.
     */
    protected static MoveSet getMoveSet(String type) {
        return switch (type) {
            case "pawn" -> Pawn.getInstance();
            case "rook" -> Rook.getInstance();
            case "knight" -> Knight.getInstance();
            case "bishop" -> Bishop.getInstance();
            case "queen" -> Queen.getInstance();
            case "king" -> King.getInstance();
            default -> null;
        };
    }
}
