package chess15.util;

import chess15.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Objects;

/**
 * This class is a utility class that converts a JSON file to a Board object.
 */
public class JsonToBoard {
    /**
     * Converts a JSON file to a Board object.
     * @param path The path to the JSON file.
     * @return The Board object.
     * @throws Exception If the JSON file is invalid.
     */
    public static Board jsonToBoard(String json) {
        Board board = new Board();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader( "src/main/resources/gamemodes/" + json));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray pieces = (JSONArray) jsonObject.get("pieces");

            for (Object piece : pieces) {
                JSONObject pieceObject = (JSONObject) piece;
                Piece piece1 = new Piece(Objects.equals((String) pieceObject.get("color"), "white") ? Piece.Color.WHITE : Piece.Color.BLACK,
                        getPieceType((String) pieceObject.get("look")),
                        getMoveSet((String) pieceObject.get("moves")),
                        (Boolean) pieceObject.get("isKing"));

                Vector2 position = new Vector2(((Long) pieceObject.get("column")).intValue(),((Long) pieceObject.get("row")).intValue());
                board.elements[position.x][position.y] = piece1;

                if(piece1.isKing){
                    if(piece1.color == Piece.Color.WHITE) board.whiteKing = position;
                    else board.blackKing = position;
                }
            }
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
    private static Piece.Type getPieceType(String type) {
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
     * @param moves A string with the lowercase name of the move set.
     * @return The MoveSet.
     */
    private static MoveSet getMoveSet(String type) {
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
