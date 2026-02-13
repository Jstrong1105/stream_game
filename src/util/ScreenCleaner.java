package util;

/*
 * 화면을 지우는 유틸리티
 */
public final class ScreenCleaner
{
	private ScreenCleaner() {}
	
	public static void cleanScreen()
	{
		// \033[H 홈으로 커서 이동
		// \033[2J 화면 지우기
		// \033[3J 버퍼 비우기
		System.out.print("\033[H\033[2J\033[3J");
		System.out.flush();
	}
}
