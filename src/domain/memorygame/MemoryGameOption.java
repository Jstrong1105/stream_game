package domain.memorygame;

/*
 * 메모리 게임 옵션
 */
class MemoryGameOption
{
	// 카드 수
	private final int MIN_CARD_PAIR = 4;
	private final int MAX_CARD_PAIR = 8;
	private int cardPair = MIN_CARD_PAIR;
	
	int getMinCardPair() { return MIN_CARD_PAIR; }
	int getMaxCardPair() { return MAX_CARD_PAIR; }
	int getCardPair() { return cardPair; }
	
	// 페어
	private final int MIN_PAIR_SIZE = 2;
	private final int MAX_PAIR_SIZE = 4;
	private int pairSize = MIN_PAIR_SIZE;
	
	int getMinPairSize() { return MIN_PAIR_SIZE; }
	int getMaxPairSize() { return MAX_PAIR_SIZE; }
	int getPairSize() { return pairSize; }		
	
	// 시간 가중치
	private final int MIN_TIME_WEIGHT = 1;
	private final int MAX_TIME_WEIGHT = 3;
	private int timeWeight = MIN_TIME_WEIGHT;
	
	int getMinTimeWeight() { return MIN_TIME_WEIGHT; }
	int getMaxTimeWeight() { return MAX_TIME_WEIGHT; }
	int getTimeWeight() { return timeWeight; }
	
	// 세터
	void setCardPair(int cardPair)
	{
		if(cardPair < MIN_CARD_PAIR || cardPair > MAX_CARD_PAIR)
		{
			throw new IllegalArgumentException("허용하지않는 카드 수 입니다.");
		}
		this.cardPair = cardPair;
	}
	
	void setPairSize(int pairSize)
	{
		if(pairSize < MIN_PAIR_SIZE || pairSize > MAX_PAIR_SIZE)
		{
			throw new IllegalArgumentException("허용하지않는 페어 수 입니다.");
		}
		this.pairSize = pairSize;
	}
	
	void setTimeWeight(int timeWeight)
	{
		if(timeWeight < MIN_TIME_WEIGHT || timeWeight > MAX_TIME_WEIGHT)
		{
			throw new IllegalArgumentException("허용하지않는 시간 가중치 입니다.");
		}
		this.timeWeight = timeWeight;
	}
}
