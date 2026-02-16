package domain.minesweeper;

/*
 * 지뢰찾기 한칸
 */
class Cell
{
	/*
	 * Cell 한칸이 가지는 상태 값
	 */
	private enum CellStatus
	{
		OPEN,HIDDEN,FLAGGED
	}
	
	private CellStatus status;	// 지뢰의 상태
	private boolean mine;		// 지뢰 여부
	private int adjacentMines;	// 인접한 지뢰의 개수
	
	private static final int MIN_ADJACENTMINES = 0;
	private static final int MAX_ADJACENTMINES = 8;
	
	// 생성자
	Cell()
	{
		status = CellStatus.HIDDEN;
		mine = false;
		adjacentMines = 0;
	}
	
	// 게터
	boolean isMine() { return mine; }
	int getAdjacentMines() { return adjacentMines; }
	boolean isHidden() { return status == CellStatus.HIDDEN; }
	boolean isOpen() { return status == CellStatus.OPEN; }
	
	// 세터
	void setMine(boolean mine){this.mine = mine;}
	
	void addAdjacentMine()
	{
		if(adjacentMines < MAX_ADJACENTMINES)
		{
			adjacentMines++;
		}
	}
	
	void minusAdjacentMine()
	{
		if(adjacentMines > MIN_ADJACENTMINES)
		{
			adjacentMines--;
		}
	}
	
	// 깃발 와리가리
	void toggleFlag()
	{
		if(status == CellStatus.FLAGGED)
		{
			status = CellStatus.HIDDEN;
		}
		else if(status == CellStatus.HIDDEN)
		{
			status = CellStatus.FLAGGED;
		}
	}
	
	// 셀 열기
	void openCell()
	{
		if(status == CellStatus.HIDDEN)
		{
			status = CellStatus.OPEN;
		}
	}
	
	// 지뢰 열기
	void openMine()
	{
		if(mine)
		{
			status = CellStatus.OPEN;
		}
	}
}
