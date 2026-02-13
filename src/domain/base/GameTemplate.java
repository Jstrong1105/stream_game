package domain.base;

import util.InputHandler;

/*
 * 게임이 구현해야하는 상세부를 정의하고
 * 공통 로직을 구현한 추상 클래스
 */
public abstract class GameTemplate implements GameApp
{
	@Override
	public void run()
	{
		do
		{
			run = true;
			initialize();
			
			while(run) 
			{
				render();
				handleInput();
				update();
			}
			
		} while (restart());
	}
	
	private boolean run; // 실행 흐름 컨트롤
	
	private boolean restart()
	{
		boolean answer = InputHandler.readBoolean("다시 시작하시겠습니까 ? (Y/N) : ", "Y", "N");
		return answer;
	}
	
	protected void endGame() { run = false; }
	
	protected abstract void initialize();		// 초기화
	protected abstract void render();			// 화면 출력
	protected abstract void handleInput();		// 입력 처리
	protected abstract void update();			// 상태 갱신
}