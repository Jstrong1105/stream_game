package domain.minesweeper;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/*
 * 지뢰찾기 보드판
 * stream 학습 용
 */
class CellBoard
{
	// 8방향 좌표 값
	private static final int[] DIRECTIONS_ROW = {-1,-1,-1,0,0,1,1,1};
	private static final int[] DIRECTIONS_COL = {-1,0,1,-1,1,-1,0,1};
	
	private final int size;			// 보드판 가로 / 세로 사이즈
	private final int mineCount;	// 지뢰의 개수
	
	private final Cell[][] board;	// 보드판
	
	private static final Random RD; // 랜덤 인스턴스
	private static final BoardPrinter PRINTER;// 보드를 주면 화면에 출력해주는 인스턴스
	
	static
	{
		RD = new Random();
		PRINTER = new BoardPrinter();
	}
	
	// 생성자
	CellBoard(int size,int mineCount)
	{
		this.size = size;
		this.mineCount = mineCount;
		
		// 보드판 생성 로직
		// Stream 활용
		// board = IntStream.range(0, size)
		//		.mapToObj(r -> IntStream.range(0, size)
		//		.mapToObj(c-> new Cell())
		//		.toArray(Cell[]::new))
		//		.toArray(Cell[][]::new);
				
		// 지뢰 배치 로직
		// Stream 활용
		// 리스트에 0부터 SIZE * SIZE 까지 담고 셔플한 다음에 지뢰 개수만큼 가져와서 
		// Stream에서 해당하는 숫자를 지뢰로 바꾸기
		
		// 이 방식은 일단 전체 사이즈 만큼 리스트를 만들고 섞은 다음 순서대로 지뢰로 바꾸는 방식
		// 전체 사이즈 + 전체 사이즈의 수를 셔플 + 폭탄 수 만큼 일을 한다.
		// 지뢰 밀도가 90%를 넘어간다면 이 방식이 더 나을 수도 있다.
		// 하지만 지뢰 밀도가 50%를 넘어간다면 전체를 지뢰로 바꾸고 지뢰가 아닌 칸을 만드는 방식으로 한다면
		// 바꾸어야 하는 칸의 비율은 50%를 넘어가지 않게 된다. 
		// 이러한 상황에서는 List를 생성해서 숫자를 전부 담고 그걸 셔플하는 과정이 메모리 소모가 휠씬 심하다고 할 수 있다.
		/*
		List<Integer> list = IntStream.range(0, SIZE*SIZE)
							.boxed()
							.collect(Collectors.toList());
		
		Collections.shuffle(list);
		
		list.stream()
		.limit(MINE_COUNT)
		.forEach(r->
				{
					board[r/SIZE][r%SIZE].setMine(true);
				});
		*/	
		
		// Stream 자체에서 랜덤하게 숫자를 뽑아서 해당 숫자를 지뢰로 바꾸기
		// 지뢰가 매우 많으면 비효율적임
		
		// 이 방식은 랜덤하게 뽑아서 지뢰로 만드는 방식 
		// 이 방식은 위의 방식과 비교해서 일단 전체를 생성하고 셔플하는 과정이 없기 때문에 
		// 효율적이지만 폭탄의 개수가 매우 많아서 
		// 중복이 아닌 숫자를 뽑는데 더 많은 자원을 소모한다면 전체를 한번 뽑는게 더 효율적일 수 있다.
		// 최소 폭탄 수 만큼 일을 한다.
		// 최대 매우 크게 일을 한다. 만약 100개 중에 99개를 폭탄으로 만들어야 한다면?
		// 마지막 한개를 찾기 위해서 2% 확률을 뚫어야 하니까 50번 그 다음은 33번 그 다음은 25번... 
		// 정확히 몇번 정도 일지 예상은 하기 어렵지만 99개라고 하면 500번 정도는? 하지 않을까 싶네
		// 위의 방법으로 하면 99개를 적용하는데 100번 + 100개 셔플 + 배치
		
		// 지뢰 개수가 과반수 인지 체크해서 전부 지뢰로 바꾼 다음에 일반 셀로 바꾸기 
		// 지뢰 개수가 과반수를 넘지 않는다면 지뢰 설치하기
		
		// 절반 이상이 지뢰라면
		if(mineCount > size*size / 2)
		{
			// 전체를 지뢰로 채운다.
			board = IntStream.range(0, size)
					.mapToObj(r -> IntStream.range(0, size)
					.mapToObj(c -> new Cell(true))
					.toArray(Cell[]::new))
					.toArray(Cell[][]::new);
			
			// 랜덤하게 size * size - mineCount 만큼 지뢰가 아닌 셀로 만든다.
			RD
			.ints(0,size*size)
			.distinct()
			.limit(size*size-mineCount)
			.forEach(num->board[num/size][num%size].setMine(false));
		}
		
		// 지뢰가 절반이하라면
		else
		{
			// 전체를 일반 셀로 채운다.
			board = IntStream.range(0, size)
					.mapToObj(r -> IntStream.range(0, size)
					.mapToObj(c -> new Cell(false))
					.toArray(Cell[]::new))
					.toArray(Cell[][]::new);
			
			// mineCount 만큼 지뢰로 만든다.
			RD
			.ints(0,size*size)
			.distinct()
			.limit(mineCount)
			.forEach(num->board[num/size][num%size].setMine(true));
		}
		
		// 전체를 순회하면서 지뢰인 셀 주변 8칸 처리
		IntStream.range(0, size*size)
		.filter(num->board[num/size][num%size].isMine())
		.forEach(num->adjacentMine(num/size, num%size,true));
		
	}	
	
	// 지뢰 주변 셀의 adjacentMine의 개수를 증감시키는 메소드
	private void adjacentMine(int row,int col,boolean add)
	{
		// 보드판의 범위 안에 있다면
		if(!isOutOfArray(row, col))
		{
			for(int i = 0; i < 8; i++)
			{
				int nRow = row + DIRECTIONS_ROW[i];
				int nCol = col + DIRECTIONS_COL[i];
				
				// 새롭게 계산한 주변 칸이 보드판의 범위 안에 있다면
				if(!isOutOfArray(nRow,nCol))
				{
					if(add)
					{
						board[nRow][nCol].addAdjacentMine();
					}
					else
					{
						board[nRow][nCol].minusAdjacentMine();
					}
				}
			}
		}
	}
	
	// 출력하는 메소드
	void boardPrint()
	{
		PRINTER.printBoard(board);
	}
	
	// 처음 입력한 좌표가 지뢰인지 확인하고
	// 지뢰라면 다른 곳으로 옮기는 메소드
	void firstOpen(int row,int col)
	{
		if(board[row][col].isMine())
		{
			// 먼저 새롭게 지뢰를 설정할 좌표를 만들어야한다.
			// 해당 칸은 지뢰가 아닌 칸이여야 겠지?
			// 이 후 사용자가 처음 입력한 칸 주변 8칸의 인접 지뢰 개수를 -1 시키고
			// 새롭게 지뢰를 설정한 좌표 주변 8칸의 인접 지뢰 개수를 +1 해야한다.
			RD.ints(0,size*size)
			.filter(c->!board[c/size][c%size].isMine())
			.limit(1)
			.forEach(c->
			{
				board[row][col].setMine(false);
				adjacentMine(row, col, false);
				
				board[c/size][c%size].setMine(true);
				adjacentMine(c/size, c%size, true);
			});
		}
		
		// 지뢰가 아니라면 아무것도 할 필요가 없다.
	}
	
	// 입력받은 좌표가 유효한 좌표인지 확인하는 메소드
	private boolean isOutOfArray(int row,int col)
	{
		return row < 0 || col < 0 || row >= size || col >= size;
	}
	
	// 범위를 벗어나면 종료하는 래퍼 메소드
	private void ifOutRange(int row,int col)
	{
		if(isOutOfArray(row,col))
		{
			throw new IllegalArgumentException("보드판의 범위를 벗어났습니다.");
		}
	}
	
	// 좌표를 입력 받아 깃발을 설치하거나 회수하는 메소드
	void toggleFlag(int row, int col)
	{
		ifOutRange(row,col);
		
		board[row][col].toggleFlag();
	}
	
	// 좌표를 입력 받아 한칸이 지뢰인지 반환하는 메소드
	boolean isMine(int row, int col)
	{
		ifOutRange(row,col);
		
		return board[row][col].isMine();
	}
	
	// 좌표를 입력 받아 한칸을 오픈하는 메소드
	// 추가로 주변에 지뢰가 한개도 없다면 주변 8칸도 열어야 한다.
	void openCell(int row, int col)
	{
		// 이미 열었거나 깃발인 칸이라면
		// 진행하지 않는다.
		if(!board[row][col].isHidden())
		{
			return;
		}
		
		// 해당 칸 열기
		board[row][col].openCell();
		
		// 만약 해당 칸 주변 지뢰가 0개라면
		if(board[row][col].getAdjacentMines() == 0)
		{
			// 주변 8칸을 순차적으로 연다.
			for(int i = 0; i < 8; i++)
			{
				int newRow = row + DIRECTIONS_ROW[i];
				int newCol = col + DIRECTIONS_COL[i];
				
				// 만약 해당 칸이 보드판을 벗어나지 않는다면
				if(!isOutOfArray(newRow, newCol))
				{
					// 그 칸도 연다.
					openCell(newRow,newCol);
				}
			}
		}
	}
	
	// 지뢰가 아닌 칸을 모두 열었는 지 확인하는 메소드
	boolean isClear()
	{
		return
		Arrays.stream(board)
		.flatMap(Arrays::stream)
		.allMatch(c->c.isMine()||c.isOpen());
		// .noneMatch(c->!c.isMine()&&!c.isOpen());
		// 지뢰가 아니고 열리지 않은 칸이 하나라도 있다면? false 인거지?
	}
	
	// 지뢰가 아닌 칸을 모두 열었거나 지뢰를 오픈했다면 모든 지뢰를 오픈하는 메소드
	void openMine()
	{
		Arrays.stream(board)
		.flatMap(Arrays::stream)
		.filter(c->c.isMine())
		.forEach(c->c.openMine());
	}
}

