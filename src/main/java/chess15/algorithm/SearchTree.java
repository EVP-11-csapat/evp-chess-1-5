package chess15.algorithm;

import chess15.board.Vector2;
import chess15.gamemodes.JSONGrabber;
import chess15.gui.util.Constants;
import chess15.board.Move;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * SearchTree class for opening book.
 */
public class SearchTree {
    /**
     * Node class for SearchTree.
     */
    static class Node {
        public Move white;
        public Move black;
        public ArrayList<Node> children;

        /**
         * Constructor for Node.
         * @param white {@link Move} white's move
         * @param black {@link Move} black's move
         * @param children {@link ArrayList} of {@link Node} children
         */
        public Node(Move white, Move black, ArrayList<Node> children) {
            this.white = white;
            this.black = black;
            this.children = children;
        }
    }

    public Node root;

    /**
     * Constructor for SearchTree.
     */
    public SearchTree() {
        try {
            // Load the JSON file
            InputStream inputStream = Objects.requireNonNull(JSONGrabber.getInstance().getClass()
                    .getResource("openings.json")).openStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputStream);

            root = parseNode(rootNode);
            root.black = null;
            root.white = null;

            if (Constants.DEVMODE)
                System.out.println(root);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse a node from a JSON file.
     * @param node {@link JsonNode} node to parse
     * @return {@link Node} parsed node
     */
    private Node parseNode(JsonNode node) {
        Move black = stringToMove(node.get("black").asText());
        Move white = stringToMove(node.get("white").asText());
        ArrayList<Node> children = new ArrayList<>();
        ArrayNode childNodes = (ArrayNode) node.get("children");
        Iterator<JsonNode> childrenIter = childNodes.elements();
        while (childrenIter.hasNext()) {
            JsonNode child = childrenIter.next();
            children.add(parseNode(child));
        }
        return new Node(white, black, children);
    }

    /**
     * Convert a string to a move.
     * @param string {@link String} string to convert
     * @return {@link Move} converted move
     */
    private Move stringToMove(String string) {
        return new Move(new Vector2((string.charAt(0) - 48), string.charAt(1) - 48), new Vector2(string.charAt(2) - 48, string.charAt(3) - 48));
    }
}
