package domain.minesweeper;

import java.time.Duration;
import java.time.Instant;

import domain.base.GameResult;
import domain.base.GameTemplate;
import util.InputHandler;
import util.ScreenCleaner;

/*
 * 지뢰찾기 구체화 클래스
 */
class MinesweeperLauncher extends GameTemplate
{
	private final int SIZE;			// 사이즈
	private final int MINES_COUNT;  // 폭탄의 개수
	
	private CellBoard board;	// 게임판
	
	private int playerRow;		// 플레이어가 선택한 행
	private int playerCol;		// 플레이어가 선택한 열
	private boolean open;		// 오픈할건지 깃발 설치할 건지 여부
	private boolean first;		// 첫 입력 여부
	
	private Instant startTime;	// 시작 시간
	
	// 생성자
	MinesweeperLauncher(MinesweeperOption option)
	{
		SIZE = option.getSize();
		MINES_COUNT = SIZE*SIZE*option.getWeight()/10;
	}
	
	// 초기화
	@Override
	protected void initialize()
	{
		// 안내메시지 출력
		ScreenCleaner.cleanScreen();
		InputHandler.readString("지뢰찾기 게임입니다. 시작하려면 엔터를 눌러주세요.");
		
		// 속성 초기화
		first = true;
		playerRow = -1;
		playerCol = -1;
		
		// 새로운 보드판 생성
		board = new CellBoard(SIZE, MINES_COUNT);
		
		// 현재 시간 기록
		startTime = Instant.now();
	}

	// 화면 출력
	@Override
	protected void render()
	{
		ScreenCleaner.cleanScreen();
		board.boardPrint();
	}

	// 사용자 입력
	@Override
	protected void handleInput()
	{
		System.out.println("폭탄의 개수 : " + MINES_COUNT);
		playerRow = InputHandler.readInt("행 번호(R) : ",1,SIZE) -1;
		playerCol = InputHandler.readInt("열 번호(C) : ",1,SIZE) -1;
		open = InputHandler.readBoolean("1. 오픈 / 2. 깃발 : ", "1", "2");
	}

	// 게임 상태 업데이트
	@Override
	protected void update()
	{
		// 깃발 설치
		if(!open)
		{
			board.toggleFlag(playerRow, playerCol);
		}
		// 오픈 하기
		else
		{
			openCell();
		}
	}
	
	// 입력 처리
	private void openCell()
	{
		// 첫 입력이라면
		if(first)
		{
			first = false;
			board.firstOpen(playerRow, playerCol);
		}
		 
		// 고른 칸이 폭탄이라면
		if(board.isMine(playerRow, playerCol))
		{
			finish(GameResult.lose());
		}
		
		// 고른 칸이 폭탄이 아니라면
		else
		{
			board.openCell(playerRow, playerCol);
			
			clearCheck();
		}
	}
	
	// 클리어 체크
	private void clearCheck()
	{
		if(board.isClear())
		{
			finish(GameResult.win((int)Duration.between(startTime, Instant.now()).getSeconds()));
		}
	}
	
	// 게임 종료
	private void finish(GameResult result)
	{
		System.out.println();
		
		board.openMine();
		render();
		
		if(result.isLose())
		{
			System.out.println((playerRow+1) + "행 " + (playerCol+1) + "열은 지뢰입니다.");
		}
		else if(result.isWin())
		{
			System.out.println("모든 지뢰를 찾아냈습니다.");
			System.out.println("클리어 시간 : " + result.getClearTime() + "초");
		}
		
		endGame();
	}
}
