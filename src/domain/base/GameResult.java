package domain.base;

/*
 * 게임 결과를 담을 객체
 */
public class GameResult
{
	// enum 객체
	private enum ResultType
	{
		WIN,LOSE,DRAW,FOLD; 
	}
	
	private final ResultType result;	// 결과를 담을 객체
	private final int clearTime;		// 클리어 시간을 담을 객체
	
	// 생성자
	private GameResult(ResultType result, int clearTime)
	{
		this.result = result;
		this.clearTime = clearTime;
	}
	
	// 결과만 받는 생성자
	private GameResult(ResultType result)
	{
		this(result,Integer.MAX_VALUE);
	}
	
	// 외부에서 GameResult를 생성하기 위해서 만든 정적 메소드
	// 기존 방식 -> GameResult result = new GameResult(GameResult.ResultType.WIN,100);
	// 아래 방식 -> GameResult result = GameResult.win(100);
	public static GameResult win(int clearTime) { return new GameResult(ResultType.WIN,clearTime); }
	public static GameResult lose() {return new GameResult(ResultType.LOSE);};
	public static GameResult draw() {return new GameResult(ResultType.DRAW);};
	public static GameResult fold() {return new GameResult(ResultType.FOLD);};
	
	// getter
	public int getClearTime() { return clearTime; }
	
	public boolean isWin()  { return result == ResultType.WIN;  }
	public boolean isLose() { return result == ResultType.LOSE; }
	public boolean isDraw() { return result == ResultType.DRAW; }
	public boolean isFold() { return result == ResultType.FOLD; }
}





