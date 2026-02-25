package domain.pokergamble;

import java.util.ArrayList;
import java.util.List;

/*
 * 포커 랭킹
 */
class PokerRankingList implements Comparable<PokerRankingList>
{
	private final PokerRanking ranking;
	private final List<Integer> kicker;
	
	// 족보 목록
	private enum PokerRanking
	{
		ROYAL_FLUSH("로얄 플러시",12),
		STRAIGHT_FLUSH("스트레이트 플러시",11),
		FOUR_OF_A_KIND("포카드",10),
		FULL_HOUSE("풀하우스",9),
		FLUSH("플러시",8),
		MOUNTAIN("마운틴",7),
		STRAIGHT("스트레이트",6),
		BACK_STRAIGHT("백스트레이트",5),
		THREE_OF_A_KIND("트리플",4),
		TWO_PAIR("투페어",3),
		ONE_PAIR("원페어",2),
		HIGH_CARD("탑",1)
		;
		PokerRanking(String name,int power)
		{
			this.name = name;
			this.power = power;
		}
		
		private final String name;	// 족보 이름
		private final int power;	// 족보 랭킹
	}
	
	// 생성자
	private PokerRankingList(PokerRanking ranking)
	{
		this.ranking = ranking;
		kicker = new ArrayList<Integer>();
	}
	
	static PokerRankingList royalFlush() { return new PokerRankingList(PokerRanking.ROYAL_FLUSH); } 
	static PokerRankingList straightFlush() { return new PokerRankingList(PokerRanking.STRAIGHT_FLUSH); }
	static PokerRankingList fourOfAKind() { return new PokerRankingList(PokerRanking.FOUR_OF_A_KIND); }
	static PokerRankingList fullHouse() { return new PokerRankingList(PokerRanking.FULL_HOUSE); }
	static PokerRankingList flush() { return new PokerRankingList(PokerRanking.FLUSH); }
	static PokerRankingList mountain() { return new PokerRankingList(PokerRanking.MOUNTAIN); }
	static PokerRankingList straight() { return new PokerRankingList(PokerRanking.STRAIGHT); }
	static PokerRankingList backStraight() { return new PokerRankingList(PokerRanking.BACK_STRAIGHT); }
	static PokerRankingList threeOfAKind() { return new PokerRankingList(PokerRanking.THREE_OF_A_KIND); }
	static PokerRankingList twoPair() { return new PokerRankingList(PokerRanking.TWO_PAIR); }
	static PokerRankingList onePair() { return new PokerRankingList(PokerRanking.ONE_PAIR); }
	static PokerRankingList highCard() { return new PokerRankingList(PokerRanking.HIGH_CARD); }
	
	// 키커 추가
	void addKicker(int kicker)
	{
		this.kicker.add(kicker);
	}
	
	String getName()
	{
		return ranking.name;
	}
	
	@Override
	public int compareTo(PokerRankingList o)
	{
		int result = this.ranking.power - o.ranking.power;
		
		if(result != 0)
		{
			return result;
		}
		
		if(this.kicker.size() != o.kicker.size())
		{
			throw new IllegalArgumentException("동일한 족보의 키커의 개수가 서로 다릅니다.");
		}
		
		for(int i = 0; i < this.kicker.size(); i++)
		{
			result = this.kicker.get(i) - o.kicker.get(i);
			
			if(result != 0)
			{
				return result;
			}
		}
		
		return 0;
	}
}
