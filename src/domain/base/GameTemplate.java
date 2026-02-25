package domain.base;

import util.InputHandler;

/*
 * 게임이 구현해야하는 상세부를 정의하고
 * 공통 로직을 구현한 추상 클래스
 */
public abstract class GameTemplate implements GameApp
{
	private boolean isRunning; // 실행 흐름 컨트롤
	
	@Override
	public void gamestart()
	{
		do
		{
			isRunning = true;
			initialize();
			
			while(isRunning) 
			{
				render();
				handleInput();
				update();
			}
			
		} while (restart());
	}
	
	// 만약 필요하다면 하위에서 오버라이딩해서 다시 시작하는 메소드를 따로 디자인 할 수 있음
	protected boolean restart()
	{
		return InputHandler.readBoolean("다시 시작하시겠습니까 ? (Y/N) : ", "Y", "N");
	}
	
	protected void endGame() { isRunning = false; }	// 게임 종료
	
	protected abstract void initialize();		// 초기화
	protected abstract void render();			// 화면 출력
	protected abstract void handleInput();		// 입력 처리
	protected abstract void update();			// 상태 갱신
}