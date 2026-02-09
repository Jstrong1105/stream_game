package util;

/*
 * 화면을 지우는 유틸리티
 */
public final class ScreenCleaner
{
	private ScreenCleaner() {}
	
	public static void cleanScreen()
	{
		System.out.print("\033[H\033[2J\033[3J");
	}
}








