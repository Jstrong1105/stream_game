package util;

/*
 * 게임을 일정 시간동안 정지시키는 유틸리티
 */
public final class GameSleeper
{
	// 외부에서 객체를 생성하지 못하도록 생성자를 private 으로 
	private GameSleeper() {}
	
	// 남은 시간을 표시하는 메소드
	// 0초가 남아있을때 0.5초 더 기다린다.
	public static void gameSleep(int second)
	{
		try
		{	for(int i = second; i > 0; i--)
			{
				System.out.printf("\r%2d 초 남았습니다.",i);
			 	Thread.sleep(1000);
			}
		
			System.out.printf("\r%2d 초 남았습니다.",0);
			Thread.sleep(500);
		}
		
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	// 남은 시간을 표시하지 않고 멈추는 메소드
	public static void gameStop(int second)
	{
		try
		{	
			 Thread.sleep(1000L*second);
		}
		
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
