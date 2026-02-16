package domain.minesweeper;

import java.util.Arrays;
import java.util.stream.IntStream;

/*
 * 보드판을 넘겨주면 보드판을 출력하는 메소드
 */
class BoardPrinter
{
	private static final char HIDDEN_SHAPE = '■';
	private static final char FLAG_SHAPE = 'Ρ';
	private static final char MINE_SHAPE = '*';
	private static final char[] OPEN_SHAPE = {'□','①','②','③','④','⑤','⑥','⑦','⑧'};
	
	void printBoard(Cell[][] board)
	{
		int size = board.length;
		
		// 이것도 스트림으로 해봅시다?
		// 상단 숫자 출력은 따로 빼야겠지?
		for(int i = 0; i < size; i++){System.out.print("==");}
		System.out.print(" 지뢰찾기 ");
		for(int i = 0; i < size; i++){System.out.print("==");}
		System.out.println();
		for(int i = 0; i < size; i++){System.out.print("====");}
		System.out.print("========");
		System.out.print("\n  C   ");
		IntStream.range(0, size)
		.forEach(num->System.out.printf("%2d ",num+1));
		System.out.print("\n    ┌");
		IntStream.range(0, size)
		.forEach(num->System.out.print("───"));
		System.out.println("─┐");
		// 행 번호 하나 출력하고 SIZE 씩 출력한 다음에 개행하고
		// 다시 SIZE 씩 출력을 SIZE 만큼 반복한다.
		// 스트림을 이용하자면 
		// 먼저 스트림 배열로 만든다. 
		// 그러면 SIZE 만큼 생길테고 거기서 하나씩 꺼내면 되니까?
		IntStream.range(0, size)
		.forEach(r->
		{
			System.out.printf("%2dR │ ",r+1);
			Arrays.stream(board[r])	
			.map(cell->getShape(cell))
			.forEach(shape -> System.out.printf("%2c ",shape));
			System.out.print("│ \n");
		});
		
		System.out.print("    └");
		IntStream.range(0, size)
		.forEach(num->System.out.print("───"));
		System.out.println("─┘");
	}
	
	// 셀의 상태에 따라 
	// 화면에 표시할 문자를 반환하는 메소드
	private char getShape(Cell cell)
	{
		if(cell.isHidden())
		{
			return HIDDEN_SHAPE;
		}
		else if(cell.isOpen())
		{
			if(cell.isMine())
			{
				return MINE_SHAPE;
			}
			else
			{
				return OPEN_SHAPE[cell.getAdjacentMines()];
			}
		}
		else
		{
			return FLAG_SHAPE;
		}
	}
}
