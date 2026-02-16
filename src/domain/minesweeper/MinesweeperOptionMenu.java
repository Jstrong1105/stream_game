package domain.minesweeper;

import java.util.function.Consumer;

import domain.base.OptionSettorTemplate;
import util.InputHandler;

enum MinesweeperOptionMenu implements OptionSettorTemplate<MinesweeperOption>
{
	SIZE("사이즈", "보드판의 가로, 세로 길이를 결정합니다.", (option) -> {
        int size = InputHandler.readInt("변경할 사이즈(" + option.getMinSize() + "~" + option.getMaxSize() + "): ", 
                                        option.getMinSize(), option.getMaxSize());
        option.setSize(size);
    }),
    
    WEIGHT("난이도", "지뢰의 밀도를 결정합니다. (1: 쉬움, 2: 보통, 3: 어려움)", (option) -> {
        int weight = InputHandler.readInt("변경할 난이도(" + option.getMinWeight() + "~" + option.getMaxWeight() + "): ", 
                                          option.getMinWeight(), option.getMaxWeight());
        option.setWeight(weight);
    });
	
	MinesweeperOptionMenu(String name,String explain ,Consumer<MinesweeperOption> settor)
	{
		this.name = name;
		this.explain = explain;
		this.settor = settor;
	}

	private final String name;
	private final String explain;
	private final Consumer<MinesweeperOption> settor;
	
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
	public void setting(MinesweeperOption option)
	{
		settor.accept(option);
	}
}
