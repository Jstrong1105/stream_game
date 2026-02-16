package domain.minesweeper;

import domain.base.GameApp;
import util.MenuRender;

public class MinesweeperGetter
{
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
