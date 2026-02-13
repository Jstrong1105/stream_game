package domain.minesweeper;

/*
 * 보드판을 넘겨주면 보드판을 출력하는 메소드
 */
class BoardPrinter
{
	private static final char HIDDEN_SHAPE = '■';
	private static final char FLAG_SHAPE = 'Ρ';
	private static final char MINE_SHAPE = '※';
	private static final char[] OPEN_SHAPE = {'□','①','②','③','④','⑤','⑥','⑦','⑧'};
	
	void printBoard(Cell[][] board)
	{
		// 이것도 스트림으로 해봅시다?
	}
}
