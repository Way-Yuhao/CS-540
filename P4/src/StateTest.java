import java.util.Arrays;

public class StateTest {
	public static void main(String[] args) {
	 String input = "11111111";
	 int[] board = new int[8];
		for (int i = 0; i < 8; i++) {
            board[i] = input.charAt(i) - 48;
            int pos = board[i];
		}
		State st = new State(board);
		System.out.print(st.cost);
	
	}
	
	private static State genSucc(State parent, int colToMod, int targetRow) {
		int[] parentBoard = parent.board;
		int[] succBoard = new int[8];
		for (int j = 0; j < 8; j++) {
			if (j != colToMod) {
				succBoard[j] = parentBoard[j];
			} else { //modify given column
				succBoard[j] = targetRow;
			}
		}
		
		State succ = new State(succBoard);
		return succ;
	}
}
