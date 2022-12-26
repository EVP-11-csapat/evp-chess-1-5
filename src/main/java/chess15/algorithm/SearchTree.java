package chess15.algorithm;

import chess15.Vector2;
import chess15.gamemodes.JSONGrabber;
import chess15.util.Move;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class SearchTree {
    static class Node {
        public Move white;
        public Move black;
        public ArrayList<Node> children;

        public Node(Move white, Move black, ArrayList<Node> children) {
            this.white = white;
            this.black = black;
            this.children = children;
        }
    }

    public Node root;

    public SearchTree() {
        JSONParser parser = new JSONParser();
        try {

            InputStream inputStream = JSONGrabber.getInstance().getClass().getResource("openings.json").openStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(inputStream);

            root = parseNode(rootNode);
            root.black = null;
            root.white = null;

            System.out.println(root);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private Move stringToMove(String string) {
        return new Move(new Vector2((string.charAt(0) - 48), string.charAt(1) - 48), new Vector2(string.charAt(2) - 48, string.charAt(3) - 48));
    }
}
