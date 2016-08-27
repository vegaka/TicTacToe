package tictactoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vegard on 07.04.2016.
 */
public class Node {

    private List<Node> children = new ArrayList<>();
    private double score;
    private Board currentBoard;
    private Move move;

    public Node(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    public Node(Move move, Board board) {
        this.move = move;
        this.currentBoard = board;
    }

    public void generateChildren(String currentPlayer, int turn) {
        if (turn == 10) {
            return;
        }

        for (int i = 0; i < Board.NUMBER_OF_CELLS; i++ ) {
            if (currentBoard.getCell(Utils.getBoardX(i), Utils.getBoardY(i)).equals(" ")) {
                Board newBoard = Board.fromBoard(currentBoard);
                Move newMove = new Move(Utils.getBoardX(i), Utils.getBoardY(i), currentPlayer);
                newBoard.applyMove(newMove);
                Node child = new Node(newMove, newBoard);
                children.add(child);

                if (!newBoard.getVictory(newMove)) {
                    child.generateChildren(currentBoard.getOtherPlayer(currentPlayer), turn + 1);
                    if (turn == 9) {
                        child.score = GameTree.DRAW_WEIGHT;
                    }
                } else {
                    if (currentPlayer.equals(currentBoard.getStartPlayer())) {
                        child.score = GameTree.WIN_WEIGHT - turn;
                    } else {
                        child.score = GameTree.LOSS_WEIGHT + turn;
                    }
                }

            }
        }

        if (currentPlayer.equals(currentBoard.getStartPlayer())) {
            score = getMaxChildScore();
        } else {
            score = getMinChildScore();
        }
    }

    private double getMaxChildScore() {
        return children.stream().mapToDouble(node -> node.score).max().getAsDouble();
    }

    private double getMinChildScore() {
        return children.stream().mapToDouble(node -> node.score).min().getAsDouble();
    }

    public Node getBestChild(boolean startingPlayer) {
        /*
        double bestScore = children.get(0).getScore();
        Node bestNode = children.get(0);
        for (Node child: children) {
            if (child.getScore() > bestScore) {
                bestScore = child.getScore();
                bestNode = child;
            }
        }

        return bestNode;*/
        return children.stream().filter(node -> node.score == score).findFirst().get();
    }

    public Node getChild(Move move) {
        for (Node child: children) {
            if (child.getMove().equals(move)) {
                return child;
            }
        }

        return null;
    }

    public double getScore() {
        return score;
    }

    public Move getMove() {
        return move;
    }
}
