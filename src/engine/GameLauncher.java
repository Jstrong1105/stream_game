package engine;

import domain.base.GameApp;
import util.GameSleeper;
import util.InputHandler;
import util.ScreenCleaner;

/*
 * 진입 포인트
 * entry point
 */
public class GameLauncher
{
	public static void main(String[] args)
	{
		// 프로그램 시작시 한번만 실행할 부분
		
		ScreenCleaner.cleanScreen();
		
		System.out.print("로딩 중입니다");
		
		for(int i = 0; i < 3; i++)
		{
			System.out.print("...");
			GameSleeper.gameStop(1);
		}
		
		System.out.println();
		
		GameFactory[] games = GameFactory.values();
		
		boolean run = true;
		
		// 반복적으로 실행할 메뉴 출력 부분
		while(run)
		{
			ScreenCleaner.cleanScreen();
			System.out.println("===== 게임 런처 =====");
			
			System.out.println("0. 종료");
			
			for(GameFactory game : games)
			{
				System.out.println(game.ordinal()+1 + ". " + game.getName() + " : " + game.getExplain());
			}
			
			int answer = InputHandler.readInt("번호를 선택 : ",0,games.length);
			
			if(answer == 0)
			{
				run = false;
			}
			else
			{
				int choice = InputHandler.readInt("1. 실행 / 2. 옵션 변경 : ",1,2);
				
				if(choice == 1)
				{
					GameApp launcher = games[answer-1].getLauncher();
					launcher.gamestart();
				}
				else if(choice == 2)
				{
					games[answer-1].setOption();
				}
			}
		}
		
		// 프로그램이 종료될 때 실행될 부분
		InputHandler.close();
		System.out.println("연결이 종료되었습니다.");
		System.out.println("프로그램이 종료됩니다.");
	}
}
