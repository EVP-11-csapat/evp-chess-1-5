package chess15.algorithm;

import chess15.Vector2;
import chess15.gamemodes.JSONGrabber;
import chess15.util.Move;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;

public class SearchTree {
    class Node {
        public Move white;
        public Move black;
        public ArrayList<Node> children;

        public Node(Move white, Move black, ArrayList<Node> children){
            this.white = white;
            this.black = black;
            this.children = children;
        }
    }

    public Node root;

    public SearchTree() {
        JSONParser parser = new JSONParser();
        try {
            URL url = JSONGrabber.getInstance().getClass().getResource("openings.json");
            Object obj = parser.parse(new FileReader(url.getFile()));
            JSONObject jsonObject = (JSONObject) obj;

            root = parseNode(jsonObject);
            root.black = null;
            root.white = null;

            System.out.println(root);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Node parseNode(JSONObject node) {
        Move black = stringToMove(String.valueOf(node.get("black")));
        Move white = stringToMove(String.valueOf(node.get("white")));
        ArrayList<Node> children = new ArrayList<>();
        JSONArray childNodes = (JSONArray) node.get("children");
        if (childNodes != null) {
            for (Object childNode : childNodes) {
                children.add(parseNode((JSONObject) childNode));
            }
        }
        return new Node(white, black, children);
    }

    private Move stringToMove(String string) {
        return new Move(new Vector2(string.charAt(0), string.charAt(1)), new Vector2(string.charAt(2),string.charAt(3)));
    }
}
