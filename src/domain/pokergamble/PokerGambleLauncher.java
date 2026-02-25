package domain.pokergamble;

import java.time.Duration;
import java.time.Instant;

import domain.base.GameResult;
import domain.base.GameTemplate;
import domain.trumpcard.Card;
import domain.trumpcard.CardDeck;
import util.InputHandler;
import util.ScreenCleaner;

/*
 * 포커 겜블 구체화
 */
class PokerGambleLauncher extends GameTemplate
{
	private CardDeck cardDeck;		// 52장 카드 묶음
	
	private final int cardCount;	// 5 / 7 포커 
	
	private HandCard playerCard;	// 플레이어 카드
	private HandCard cpuCard;		// cpu 카드
	
	private PokerRankingList playerResult;	// 플레이어 족보
	private PokerRankingList cpuResult;		// cpu 족보
	
	private final int TARGET_COIN;	// 목표 코인
	private int playerCoin;			// 플레이어 코인
	private int totalBetCoin;		// 한 라운드 토탈 베팅 금액
	private int betCoin;			// 이번 턴 베팅 금액
	
	private final int weight;		// 획득 가중치
	private final int level;		// 난이도
	
	private Instant startTime;		// 시작 시간
	
	private final int MIN_BET_COIN = 10;	// 시작 베팅 금액
	
	private int skill;						// 컴퓨터의 카드를 오픈할 수 있는 개수
	private int skillCount;
	private final int[] skillCounts = {1,1,2,3,3,4};
	private boolean useSkill;
	
	PokerGambleLauncher(PokerGambleOption option)
	{
		cardCount = option.getIsFive() ? 5 : 7 ;  
		TARGET_COIN = option.getTargetCoin();
		weight = option.getWeight();
		level = option.getLevel();
	}
	
	// 초기화
	@Override
	protected void initialize()
	{
		ScreenCleaner.cleanScreen();
		InputHandler.readString("포커겜블 게임입니다.");
		
		int[] levels = {4,5,10};
		
		playerCoin = TARGET_COIN / levels[level-1];
		startTime = Instant.now();
		roundInit();
	}
	
	// 라운드 초기화
	private void roundInit()
	{
		cardDeck = new CardDeck();
		playerCard = new HandCard();
		cpuCard = new HandCard();
		
		totalBetCoin = 0;
		betCoin = 0;
		
		skill = 0;
		skillCount = 0;
		useSkill = false;
		
		// 기본 배팅
		if(playerCoin >= MIN_BET_COIN)
		{
			totalBetCoin += MIN_BET_COIN;
			playerCoin -= MIN_BET_COIN;
		}
		
		// 기본 배팅 코인이 부족하면 올인한다.
		else
		{
			totalBetCoin += playerCoin;
			playerCoin = 0;
		}
		
		drawCard();
		drawCard();
	}

	@Override
	protected void render()
	{
		ScreenCleaner.cleanScreen();
		cpuCard.printCard();
		System.out.println("  컴퓨터의 카드");
		playerCard.printCard();
		System.out.println("  당신의 카드");
		System.out.println("목표 코인 : " + TARGET_COIN);
		System.out.println("현재 베팅 금액 : " + totalBetCoin);
		System.out.println("현재 남은 코인 : " + playerCoin);
	}

	@Override
	protected void handleInput()
	{
		betCoin = InputHandler.readInt("베팅할 코인을 입력(-1 스킬) : ",-1,playerCoin);
	}

	@Override
	protected void update()
	{
		if(!useSkill)
		{
			skill = skillCounts[skillCount];
			
			if(betCoin == -1)
			{
				if(InputHandler.readBoolean("현재 오픈 가능한 카드 수 : " + skill + " 오픈하시겠습니까?(Y/N) : ", "y", "n"))
				{
					useSkill = true;
					openCpuCard(skill);
				}
				
				return;
			}
			else
			{
				skillCount += 1;
			}
		}
		else
		{
			if(betCoin == -1)
			{
				InputHandler.readString("이미 스킬을 사용했습니다.");
				return;
			}
		}
		
		// 코인이 남아 있는데 0을 베팅한 경우
		if(betCoin == 0 && playerCoin > 0)
		{
			finish(GameResult.fold());
		}
		
		else
		{	
			playerCoin -= betCoin;
			totalBetCoin += betCoin;
			
			// 카드를 다 받지 않은 경우
			if(playerCard.countCard() < cardCount)
			{
				drawCard();
			}
			
			// 카드를 다 받은 경우
			else
			{
				checkWinner();
			}
		}
	}
	
	// 승패를 판단하는 메소드
	private void checkWinner()
	{
		playerResult = playerCard.getHandRanking();
		cpuResult = cpuCard.getHandRanking();
		
		int eval = playerResult.compareTo(cpuResult);
		
		if(eval > 0)
		{
			// 승리
			finish(GameResult.win((int)Duration.between(startTime, Instant.now()).getSeconds()));
		}
		
		else if(eval < 0)
		{
			// 패배
			finish(GameResult.lose());
		}
		
		else
		{
			// 무승부
			finish(GameResult.draw());
		}
	}
	
	// 게임 종료
	private void finish(GameResult result)
	{
		if(result.isFold())
		{
			System.out.println("기권 했습니다.");
			InputHandler.readString("엔터를 눌러 다음 게임을 시작하세요.");
			roundInit();
			return;
		}
		
		ScreenCleaner.cleanScreen();
		openCpuCard(cpuCard.countCard());
		
		cpuCard.printCard();
		System.out.println(cpuResult.getName());
		
		playerCard.printCard();
		System.out.println(playerResult.getName());
		
		if(result.isWin())
		{
			System.out.println("승리했습니다.");
			playerCoin += totalBetCoin * (1 + weight);
		}
		else if(result.isLose())
		{
			System.out.println("패배했습니다.");
		}
		else if(result.isDraw())
		{
			System.out.println("무승부입니다.");
			playerCoin += totalBetCoin;
		}
		
		// 목표 달성
		if(playerCoin >= TARGET_COIN)
		{
			System.out.println("달성한 코인 : " + playerCoin);
			System.out.println("목표를 달성했습니다.");
			endGame();
			return;
		}
		
		// 코인 탕진
		if(playerCoin <= 0)
		{
			System.out.println("모든 코인을 소진했습니다.");
			endGame();
			return;
		}
		
		InputHandler.readString("엔터를 눌러 다음 게임을 시작하세요.");
		roundInit();
	}
	
	// 컴퓨터의 카드 오픈하기
	private void openCpuCard(int count)
	{
		for(int i = 0; i < count; i++)
		{
			cpuCard.openCard(i);
		}
	}
	
	// 카드 나눠주기
	private void drawCard()
	{
		// 나한테 주는 카드는 전부 공개한다.
		Card card = cardDeck.drawCard();
		card.openCard();
		playerCard.drawCard(card);
		
		// 컴퓨터의 카드는 공개하지 않는다.
		card = cardDeck.drawCard();
		cpuCard.drawCard(card);
	}
}
