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
	
	private final int SIZE;			// 보드판 가로 / 세로 사이즈
	private final int MINE_COUNT;	// 지뢰의 개수
	
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
		SIZE = size;
		MINE_COUNT = mineCount;
		
		// 보드판 생성 로직
		// Stream 활용
		board = IntStream.range(0, SIZE)
				.mapToObj(r -> IntStream.range(0, SIZE)
				.mapToObj(c-> new Cell())
				.toArray(Cell[]::new))
				.toArray(Cell[][]::new);
				
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
		// 지금 내가 구현하는 지뢰찾기는 최소 10 * 10 에서 10 %를 폭탄으로 
		//                               최대 20 * 20 에서 75 %를 폭탄으로 배치하는 방식이다.
		// 애매한가?
		// 최대로 보면 위의 방식이 더 나아보이는데
		// 일반적으로 실행하면 아래가 나아보인다.
		
		// 만약 지뢰의 비율이 50%를 넘어간다면 전체를 지뢰로 바꾸고 지뢰가 아닌 칸을 설정하게 만드는 방식으로 
		// 만들게 된다면 아래의 방식이 모든 상황에서 압도적으로 효율적이라고 할 수 있다.
		// 위의 방식보다 모든 상황에서 더 빠르다고 할 수 있지만? 정말 운이 없다면 더 걸릴 수도 있긴하지?
		// 위의 방식은 일정한 속도를 보장해 준다고 할 수 있지만 
		// 아래 방식은 걸리는 속도를 일정하게 보장해 주진 않는다. 
		// 다만 거의 무조건 위의 방식보다 빠르다.
		if(SIZE*SIZE/2 >= MINE_COUNT)
		{
			RD
			.ints(0,SIZE*SIZE)
			.distinct()
			.limit(MINE_COUNT)
			.forEach(r->
			{
				board[r/SIZE][r%SIZE].setMine(true);
				adjacentMine(r/SIZE, r%SIZE, true);
			});
		}
		else
		{
			// 여기서는 전체를 지뢰로 바꾸는 로직이 필요하다.
			// board를 2차원 스트림으로 바꾸기
			// 2차원 스트림을 1차원 스트림으로 바꾸기 펼치는 느낌으로
			// 1차원 스트림을 각각의 셀에 setMine(true) 실행하기
			 Arrays.stream(board)
			.flatMap(Arrays::stream)
			.forEach(c->c.setMine(true));
			 
			// 그 다음에 SIZE*SIZE - mineCount 만큼 지뢰가 아닌 셀로 만들어야 한다.
			RD
			.ints(0,SIZE*SIZE)
			.distinct()
			.limit(SIZE*SIZE-MINE_COUNT)
			.forEach(r->{board[r/SIZE][r%SIZE].setMine(false);});
			
			IntStream.range(0, SIZE*SIZE)
			.filter(c->board[c/SIZE][c%SIZE].isMine())
			.forEach(c->
			{
				adjacentMine(c/SIZE, c%SIZE,true);
			});
		}
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
	
	// 처음 입력한 좌표가 지뢰여서 해당 지뢰를 다른 곳으로 옮기는 메소드
	void firstOpen(int row,int col)
	{
		if(board[row][col].isMine())
		{
			// 먼저 새롭게 지뢰를 설정할 좌표를 만들어야한다.
			// 해당 칸은 지뢰가 아닌 칸이여야 겠지?
			// 이 후 사용자가 처음 입력한 칸 주변 8칸의 인접 지뢰 개수를 -1 시키고
			// 새롭게 지뢰를 설정한 좌표 주변 8칸의 인접 지뢰 개수를 +1 해야한다.
			RD.ints(0,SIZE*SIZE)
			.filter(c->!board[c/SIZE][c%SIZE].isMine())
			.limit(1)
			.forEach(c->
			{
				board[row][col].setMine(false);
				adjacentMine(row, col, false);
				
				board[c/SIZE][c%SIZE].setMine(true);
				adjacentMine(c/SIZE, c%SIZE, true);
			});
		}
		
		// 지뢰가 아니라면 아무것도 할 필요가 없다.
	}
	
	// 입력받은 좌표가 유효한 좌표인지 확인하는 메소드
	boolean isOutOfArray(int row,int col)
	{
		return row < 0 || col < 0 || row >= SIZE || col >= SIZE;
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
		.noneMatch(c->!c.isMine()&&!c.isOpen());
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
