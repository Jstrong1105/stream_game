package domain.minesweeper;

import domain.base.GameApp;
import util.MenuRender;

/*
 * 지뢰찾기 패키지에서 유일하게 외부와 소통이 가능한 클래스
 */
public final class MinesweeperGetter
{
	private MinesweeperGetter() {}
	
	private static MinesweeperOption option;
	
	private static MinesweeperOption getOption()
	{
		if(option == null)
		{
			option = new MinesweeperOption();
		}
		
		return option;
	}
	
	public static GameApp getLauncher()
	{
		return new MinesweeperLauncher(getOption());
	}
	
	public static void setOption()
	{
		MenuRender.menuRender(getOption(), MinesweeperOptionMenu.values(), "지뢰찾기 옵션");
	}
}
