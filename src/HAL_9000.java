import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

public class HAL_9000 extends Player
{


    public HAL_9000(int color,String name)
    {
        super(color,name);
    }
    public Move getMove(BlokusBoard board)
    {
        ArrayList<Move> validMoves = new ArrayList();
        ArrayList<Move> sortedValidMoves = new ArrayList();
        //System.out.println("my color is "+getColor() + " the turn is "+board.getTurn());
        ArrayList<IntPoint> avaiableMoves = board.moveLocations(getColor());
        Collections.shuffle(avaiableMoves);
        //System.out.println("available move locations "+avaiableMoves);
        ArrayList<Integer> usableShapePositions = new ArrayList<>();
        boolean[] used = (getColor()==BlokusBoard.ORANGE)?board.getOrangeUsedShapes():board.getPurpleUsedShapes();
        for(int x=0; x<used.length; x++)
            if(!used[x])
            {
                usableShapePositions.add(x);
            }
        //System.out.println("usable pieces "+ Arrays.toString(used));
        Collections.shuffle(usableShapePositions);
        if(usableShapePositions.isEmpty() || avaiableMoves.isEmpty())
            return null;
        else
        {
            //System.out.println("hi");
            Move move = null;
            for(IntPoint movLoc: avaiableMoves)
                for(Integer position: usableShapePositions)
                {
                    for(int i=0;i<8;i++) {
                        boolean flip = i > 3;
                        int rotation = i % 4;
                        boolean[][] shape = board.getShapes().get(position).manipulatedShape(flip, rotation);
                        for (int r = -shape.length+1; r <shape.length;  r++)
                            for (int c = -shape[0].length+1; c < shape[0].length; c++)
                            {
                                IntPoint topLeft = new IntPoint(movLoc.getX()+c,movLoc.getY()+r);
                                Move test = new Move(position,flip,rotation,topLeft);
                                if(board.isValidMove(test,getColor()))
                                    validMoves.add(test);
                            }
                    }
                }

        }

        for(int x = 0;x < validMoves.size();x++)
        {
            if(validMoves.get(x).getPieceNumber() >= 10 ) {
                sortedValidMoves.add(validMoves.get(x));
            }
        }
        for(int x = 0;x < validMoves.size();x++)
        {
            if(validMoves.get(x).getPieceNumber() >= 5 && validMoves.get(x).getPieceNumber() <= 9)
                sortedValidMoves.add(validMoves.get(x));
        }
        for(int x = 0;x < validMoves.size();x++)
        {
            if(validMoves.get(x).getPieceNumber() >= 3 && validMoves.get(x).getPieceNumber() <= 4)
                sortedValidMoves.add(validMoves.get(x));
        }
        for(int x = 0;x < validMoves.size();x++)
        {
            if(validMoves.get(x).getPieceNumber() == 2)
                sortedValidMoves.add(validMoves.get(x));
        }
        for(int x = 0;x < validMoves.size();x++)
        {
            if(validMoves.get(x).getPieceNumber() == 1)
                sortedValidMoves.add(validMoves.get(x));
        }
        int[] movesLeft = new int[sortedValidMoves.size()];
        int[] mostLeft = new int[sortedValidMoves.size()];
        for(int x = 0;x < sortedValidMoves.size();x++)
        {
            ArrayList<Move> fakeValidMoves = sortedValidMoves;
            BlokusBoard fakeBoard = new BlokusBoard(board);
            fakeBoard.makeMove(fakeValidMoves.get(x),getColor());
            if(getOtherColor() == 5)
                movesLeft[x] = fakeBoard.getPurpleMoveLocations().size();
            else
                movesLeft[x] = fakeBoard.getOrangeMoveLocations().size();
        }
        boolean allSame = true;
        for(int x = 1;x < movesLeft.length;x++)
        {
            if(movesLeft[x] != movesLeft[x-1])
                allSame = false;
        }
        if(allSame)
        {
            for(int x = 0;x < sortedValidMoves.size();x++)
            {
                ArrayList<Move> fakeValidMoves = sortedValidMoves;
                BlokusBoard fakeBoard = new BlokusBoard(board);
                fakeBoard.makeMove(fakeValidMoves.get(x),getColor());
                if(getColor() == 5)
                    mostLeft[x] = fakeBoard.getPurpleMoveLocations().size();
                else
                    mostLeft[x] = fakeBoard.getOrangeMoveLocations().size();
            }
            int largetMoves = 0;
            int largestLoc = 0;
            for (int x = 0; x < movesLeft.length; x++) {
                if (movesLeft[x] > largetMoves) {
                    largetMoves = movesLeft[x];
                    largestLoc = x;
                }
            }
            if(sortedValidMoves.isEmpty() == false)
                return sortedValidMoves.get(largestLoc);
            else
                return null;

        }
        else {
            int smallestMoves = 100000;
            int smallestLoc = 0;
            for (int x = 0; x < movesLeft.length; x++) {
                if (movesLeft[x] < smallestMoves) {
                    smallestMoves = movesLeft[x];
                    smallestLoc = x;
                }
            }
            if(sortedValidMoves.isEmpty() == false)
                return sortedValidMoves.get(smallestLoc);
            else
                return null;
        }

    }
    public Player freshCopy()
    {
        return new HAL_9000(getColor(),getName());
    }

}
