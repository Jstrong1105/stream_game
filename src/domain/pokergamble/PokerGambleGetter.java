package domain.pokergamble;

import domain.base.GameApp;
import util.MenuRender;

/*
 * 외부와 소통이 가능한 녀석
 */
public final class PokerGambleGetter
{
	private PokerGambleGetter() {}
	
	private static PokerGambleOption option;
	
	private static PokerGambleOption getOption()
	{
		if(option == null)
		{
			option = new PokerGambleOption();
		}
		
		return option;
	}
	
	public static GameApp getLauncher()
	{
		return new PokerGambleLauncher(getOption());
	}
	
	public static void setOption()
	{
		MenuRender.menuRender(getOption(), PokerGambleOptionMenu.values(), "포커겜블 옵션");
	}
}
