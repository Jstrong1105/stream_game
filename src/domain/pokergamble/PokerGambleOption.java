package domain.pokergamble;

/*
 * 포커 겜블 옵션
 */
class PokerGambleOption
{
	// 5 / 7 모드
	private boolean isFive = true;
	
	boolean getIsFive() { return isFive; }
	
	// 목표 코인
	private final int MIN_TARGET_COIN = 1000;
	private final int MAX_TARGET_COIN = 10000;
	private int targetCoin = MIN_TARGET_COIN;
	
	int getMinTargetCoin() { return MIN_TARGET_COIN; }
	int getMaxTargetCoin() { return MAX_TARGET_COIN; }
	int getTargetCoin() { return targetCoin; }
	
	// 시작 코인
	private final int MIN_LEVEL = 1;
	private final int MAX_LEVEL = 3;
	private int level = MIN_LEVEL;
	
	int getMinLevel() { return MIN_LEVEL; }
	int getMaxLevel() { return MAX_LEVEL; }
	int getLevel() { return level; }
	
	// 승리 시 획득하는 코인 가중치
	private final int MIN_WEIGHT = 1;
	private final int MAX_WEIGHT = 3;
	private int weight = MIN_WEIGHT;
	
	int getMinWeight() { return MIN_WEIGHT; }
	int getMaxWeight() { return MAX_WEIGHT; }
	int getWeight() { return weight; }
	
	// 세터
	void setIsFive(boolean isFive)
	{
		this.isFive = isFive;
	}
	
	void setTargetCoin(int targetCoin)
	{
		if(targetCoin < MIN_TARGET_COIN || targetCoin > MAX_TARGET_COIN)
		{
			throw new IllegalArgumentException("허용하지 않는 목표 코인입니다.");
		}
		this.targetCoin = targetCoin;
	}
	
	void setLevel(int level)
	{
		if(level < MIN_LEVEL || level > MAX_LEVEL)
		{
			throw new IllegalArgumentException("허용하지 않는 레벨입니다.");
		}
		this.level = level;
	}
	
	void setWeight(int weight)
	{
		if(weight < MIN_WEIGHT || weight > MAX_WEIGHT)
		{
			throw new IllegalArgumentException("허용하지 않는 가중치입니다.");
		}
		this.weight = weight;
	}
}
