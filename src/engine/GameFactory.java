package engine;

import java.util.function.Supplier;

import domain.base.GameApp;

/*
 * 게임을 만드는 장소
 * 게임 리스트를 가지고 있다
 * 각각 게임의 생성자와 연결되어 있다.
 */
public enum GameFactory
{
	
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
	public GameApp getLancher()
	{
		return maker.get();
	}
	
	public void setOption()
	{
		setter.run();
	}
}
