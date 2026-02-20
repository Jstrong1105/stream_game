package domain.memorygame;

import domain.base.GameApp;
import util.MenuRender;

public final class MemoryGameGetter
{
	private MemoryGameGetter() {}
	
	private static MemoryGameOption option;
	
	private static MemoryGameOption getOption()
	{
		if(option == null)
		{
			option = new MemoryGameOption();
		}
		
		return option;
	}
	
	public static GameApp getLauncher()
	{
		return new MemoryGameLauncher(getOption());
	}
	
	public static void setOption()
	{
		MenuRender.menuRender(getOption(), MemoryGameOptionMenu.values(), "메모리게임 옵션");
	}
}
