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
		
		board = new MemoryBoard(cardPair,pairSize);	// 보드판 생성
		playerNumber = -1;
		
		InputHandler.readString("메모리게임입니다. 엔터를 누르면 카드가 보여집니다.");
		
		board.openAll();						// 전부 열고
		board.printCard();						// 보여준 다음
		GameSleeper.gameSleep(10 * timeWeight);	// 기다리고
		board.hiddenAll();						// 다시 숨기고
		startTime = Instant.now();				// 시작시간 기록
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
			// 이미 오픈한 카드라면
			if(board.openCard(playerNumber))
			{
				InputHandler.readString("이미 오픈한 카드입니다.");
				return;
			}
		}
		
		// 카드를 전부 골랐다면
		if(board.completeSelection())
		{
			checkCard();
		}
		
		// 전부 찾았다면
		if(board.clear())
		{
			finish(GameResult.win((int)Duration.between(startTime, Instant.now()).getSeconds()));
		}
	}
	
	// 카드를 전부 골라서 판정하기
	private void checkCard()
	{
		render();
		
		// 전부 일치한다면
		if(board.isSameCard())
		{
			System.out.println("같은 카드입니다.");
			GameSleeper.gameSleep(1);
		}
		// 한장이라도 일치하지 않는다면
		else
		{
			System.out.println("다른 카드입니다.");
			GameSleeper.gameSleep(3*timeWeight);
		}
		
		board.resetChoice();
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
}
