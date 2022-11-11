package chess15.util;

import chess15.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Objects;

public class JsonToBoard {

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

                board.elements[((Long) pieceObject.get("row")).intValue()][((Long) pieceObject.get("column")).intValue()] = piece1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return board;
    }

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
