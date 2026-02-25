package engine;

import java.util.function.Supplier;

import domain.base.GameApp;
import domain.minesweeper.MinesweeperGetter;
import domain.memorygame.MemoryGameGetter;
import domain.pokergamble.PokerGambleGetter;

/*
 * 게임을 만드는 장소
 * 게임 리스트를 가지고 있다
 * 각각 게임의 생성자와 연결되어 있다.
 */
public enum GameFactory
{
	MINESWEEPER("지뢰찾기","지뢰가 아닌 칸을 모두 여세요!",MinesweeperGetter::getLauncher,MinesweeperGetter::setOption),
	MEMORY_GAME("메모리게임","동일한 카드를 뒤집으세요!",MemoryGameGetter::getLauncher,MemoryGameGetter::setOption),
	POKER_GAMBLE("포커겜블","포커를 승리해 코인을 획득하세요",PokerGambleGetter::getLauncher,PokerGambleGetter::setOption)
	;
	
	GameFactory(String name, String explain, Supplier<GameApp> maker, Runnable setter)
	{
		this.name = name;
		this.explain = explain;
		this.maker = maker;
		this.setter = setter;
	}
	
	private final String name;				// 이름
	private final String explain;			// 설명
	private final Supplier<GameApp> maker;  // Launcher를 반환하는 녀석
	private final Runnable setter;			// option을 수정하는 녀석
	
	public String getName() { return name; }
	public String getExplain() { return explain; }
	public GameApp getLauncher()
	{
		return maker.get();
	}
	
	public void setOption()
	{
		setter.run();
	}
}
