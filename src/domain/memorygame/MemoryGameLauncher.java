package domain.memorygame;

import java.time.Duration;
import java.time.Instant;

import domain.base.GameResult;
import domain.base.GameTemplate;
import util.GameSleeper;
import util.InputHandler;
import util.ScreenCleaner;

/*
 * 메모리게임 구체화
 */
class MemoryGameLauncher extends GameTemplate
{
	private MemoryBoard board;	  // 보드판
	
	private final int cardPair;	  // 카드 수
	private final int pairSize;	  // 페어 수
	private final int timeWeight; // 시간 가중치
	
	private int playerNumber;	  // 사용자 입력 값
	
	private Instant startTime;	  // 시작시간
	
	// 생성자
	MemoryGameLauncher(MemoryGameOption option)
	{
		cardPair = option.getCardPair();
		pairSize = option.getPairSize();
		timeWeight = option.getTimeWeight();
	}
	
	// 초기화
	@Override
	protected void initialize()
	{
		ScreenCleaner.cleanScreen();
		InputHandler.readString("메모리게임입니다.");
		
		board = new MemoryBoard(cardPair,pairSize);	// 보드판 생성
		playerNumber = -1;
		
		InputHandler.readString("엔터를 누르면 카드가 보여집니다.");
		board.openAll();
		board.printCard();
		GameSleeper.gameSleep(10 * timeWeight);
		board.hiddenAll();
		startTime = Instant.now();
	}

	// 화면 출력
	@Override
	protected void render()
	{
		ScreenCleaner.cleanScreen();
		board.printCard();
	}

	// 사용자 입력
	@Override
	protected void handleInput()
	{
		playerNumber = InputHandler.readInt("카드를 선택 (0 포기) : ",0,cardPair*pairSize)-1;
	}

	// 게임 상태 업데이트
	@Override
	protected void update()
	{
		// 기권 했다면
		if(playerNumber == -1)
		{
			finish(GameResult.fold());
			return;
		}
		
		// 기권하지 않았다면
		else
		{
			board.openCard(playerNumber);
		}
		
		// 카드를 전부 골랐다면
		if(board.selectAll())
		{
			board.matchCard(3*timeWeight);
		}
		
		// 전부 찾았다면
		if(board.clear())
		{
			finish(GameResult.win((int)Duration.between(startTime, Instant.now()).getSeconds()));
		}
	}
	
	// 게임 종료
	private void finish(GameResult result)
	{
		ScreenCleaner.cleanScreen();
		board.openAll();
		board.printCard();
		
		if(result.isWin())
		{
			System.out.println("모든 카드를 맞췄습니다.");
			System.out.println("소요시간 : " + result.getClearTime() +"초");
		}
		else
		{
			System.out.println("기권했습니다.");
		}
		
		endGame();
	}
	
	public static void main(String[] args)
	{
		MemoryGameLauncher game = new MemoryGameLauncher(new MemoryGameOption());
		game.gamestart();
	}
}
