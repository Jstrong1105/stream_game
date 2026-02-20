package domain.memorygame;

import java.util.function.Consumer;

import domain.base.OptionSetterTemplate;
import util.InputHandler;

enum MemoryGameOptionMenu implements OptionSetterTemplate<MemoryGameOption>
{
	CARD_PAIR("카드 수","카드의 수를 결정합니다.",option->
	{
		int cardPair = InputHandler.readInt(String.format("변경할 카드 수를 입력 (%d~%d) : ",option.getMinCardPair(),option.getMaxCardPair())
				,option.getMinCardPair(),option.getMaxCardPair());
		option.setCardPair(cardPair);
	}),
	PAIR_SIZE("페어 수","페어의 수를 결정합니다.",option->
	{
		int pairSize = InputHandler.readInt(String.format("변경할 페어 수를 입력 (%d~%d) : ",option.getMinPairSize(),option.getMaxPairSize())
				,option.getMinPairSize(),option.getMaxPairSize());
		option.setPairSize(pairSize);
	}),
	TIME_WEIGHT("시간 가중치","보여지는 시간을 결정합니다.",option->
	{
		int timeWeight = InputHandler.readInt(String.format("변경할 시간 가중치를 입력 (%d~%d) : ",option.getMinTimeWeight(),option.getMaxTimeWeight())
				,option.getMinTimeWeight(),option.getMaxTimeWeight());
		option.setTimeWeight(timeWeight);
	})
	;
	
	MemoryGameOptionMenu(String name,String explain ,Consumer<MemoryGameOption> settor)
	{
		this.name = name;
		this.explain = explain;
		this.settor = settor;
	}

	private final String name;
	private final String explain;
	private final Consumer<MemoryGameOption> settor;
	
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
	public void setting(MemoryGameOption option)
	{
		settor.accept(option);
	}
}
