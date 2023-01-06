package chess15.gui.newui;

public class MoveRepr {
    private String whiteMove;
    private String blackMove;

    public MoveRepr(String whiteMove, String blackMove) {
        this.whiteMove = whiteMove;
        this.blackMove = blackMove;
    }

    public String getWhiteMove() {
        return whiteMove;
    }

    public void setWhiteMove(String whiteMove) {
        this.whiteMove = whiteMove;
    }

    public String getBlackMove() {
        return blackMove;
    }

    public void setBlackMove(String blackMove) {
        this.blackMove = blackMove;
    }
}
