package domain.pokergamble;

import java.util.function.Consumer;

import domain.base.OptionSetter;
import util.InputHandler;

/*
 * 포커 겜블 옵션 메뉴
 */
enum PokerGambleOptionMenu implements OptionSetter<PokerGambleOption>
{
	FIVE_SEVEN("5/7 포커","받는 카드의 장수를 결정합니다.",option->
	{
		boolean five = InputHandler.readBoolean("1. 5포커 / 2. 7포커 : ", "1", "2");
		option.setIsFive(five);
	}),
	
	TARGET_COIN("목표 코인","목표로 하는 코인의 개수를 결정합니다.",option->
	{
		int targetCoin = InputHandler.readInt(String.format("변경할 목표 코인을 입력 (%d~%d) : ",option.getMinTargetCoin(),option.getMaxTargetCoin())
				,option.getMinTargetCoin(),option.getMaxTargetCoin());
		option.setTargetCoin(targetCoin);
	}),
	LEVEL("난이도","시작 시 보유한 코인의 비율을 결정합니다.",option->
	{
		int Level = InputHandler.readInt(String.format("변경할 레벨을 입력 (%d~%d) : ",option.getMinLevel(),option.getMaxLevel())
				,option.getMinLevel(),option.getMaxLevel());
		option.setLevel(Level);
	}),
	WEIGHT("가중치","승리시 획득하는 배율을 결정합니다.",option->
	{
		int weight = InputHandler.readInt(String.format("변경할 가중치를 입력 (%d~%d) : ", option.getMinWeight(),option.getMaxWeight())
				,option.getMinWeight(),option.getMaxWeight());
		option.setWeight(weight);
				
	})
	;
	
	PokerGambleOptionMenu(String name, String explain, Consumer<PokerGambleOption> setter)
	{
		this.name = name;
		this.explain = explain;
		this.setter = setter;
	}
	
	private final String name;
	private final String explain;
	private final Consumer<PokerGambleOption> setter;
	
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getExplain()
	{
		return explain;
	}

	@Override
	public void setting(PokerGambleOption option)
	{
		setter.accept(option);
	}
}
