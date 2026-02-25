package domain.pokergamble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import domain.trumpcard.Card;

/*
 * 족보 계산기
 */
class PokerHandEvaluator
{
	private List<Card> handCard;						// 받은 카드
	
	private HashMap<Character, Integer> shapeList;		// ex : ♠ : 2  / ♥ : 2 / ◆ : 1 / ♣ : 2
	private HashMap<Integer, Integer> numberList;		// 2(가) : 1(개) / 3 : 0 / 4 : 2 / 5 : 1 .... 14 : 1
	
	private List<Integer> numberOrder;					// 받은 숫자를 정렬 ex : 14 , 14 , 13 , 7 , 5 , 4 , 3
	
	private List<Integer> flushOrder;					// 플러시에 해당하는 숫자를 정렬 ex : 14 , 14 , 5 , 4 , 2
	
	private HashMap<Integer, Integer> pairPattern;		// 어떤 페어가 몇개 있는지 ex : 1장 : 1개 / 2 : 0 / 3 : 0 / 4 : 1 
	
	private final int MIN_CARD_NUMBER = 2;
	private final int MAX_CARD_NUMBER = 14;
	
	// 카드 뭉치를 받아서 
	// 족보를 계산해서 돌려주는 메소드
	PokerRankingList getRanking(List<Card> handCard)
	{
		if(handCard.size() < 5)
		{
			throw new IllegalArgumentException("카드는 5장이 넘어야 합니다.");
		}
		
		this.handCard = handCard;
		
		prepareData();
		
		PokerRankingList result = null;
		
		for(Supplier<PokerRankingList> eval : evaluator)
		{
			result = eval.get();
			if(result != null)
			{
				return result;
			}
		}
		
		throw new IllegalArgumentException("족보 판단에 에러가 발생했습니다.");
	}
	
	// 숫자와 모양을 이런 저런 데이터로 
	// 분류하는 사전 준비
	private void prepareData()
	{
		flushOrder = null;
		shapeList = new HashMap<>();
		numberList = new HashMap<>();
		numberOrder = new ArrayList<>();
		pairPattern = new HashMap<>();
		
		for(Card card : handCard)
		{
			// 모양 추출
			shapeList.put(card.getShape(), shapeList.getOrDefault(card.getShape(), 0)+1);
			// 숫자 추출
			numberList.put(card.getNumber(),numberList.getOrDefault(card.getNumber(), 0)+1);
			// 정렬할 숫자 저장
			numberOrder.add(card.getNumber());
		}
		
		// 내림차순 정렬
		Collections.sort(numberOrder,Collections.reverseOrder());
		
		// 모양이 5개 이상인 녀석이 있다면
		// 그것만 검사하고 나머지는 검사하지 않는데
		// 그 이유는 5 / 7 포커에서 플러시를 만족하는 모양은 2개 이상일 수 없기 때문이다.
		for(char shape : shapeList.keySet())
		{
			// 5개 이상이 있다면
			if(shapeList.get(shape) >= 5)
			{
				flushOrder = new ArrayList<>();
				
				// 받은 카드 뭉치에서 해당하는 모양을 가진 카드의 숫자를 추출한다.
				for(Card card : handCard)
				{
					if(card.getShape() == shape)
					{
						flushOrder.add(card.getNumber());
					}
				}
				
				// 내림 차순 정렬
				Collections.sort(flushOrder,Collections.reverseOrder());
				break;
			}
		}
		
		for(int i : numberList.values())
		{
			pairPattern.put(i, pairPattern.getOrDefault(i, 0)+1);
		}
	}
	
	private List<Supplier<PokerRankingList>> evaluator = Arrays.asList
			(
					this::straightFlush,
					this::fourOfAKind,
					this::fullHouse,
					this::flush,
					this::straight,
					this::threeOfAKind,
					this::twoPair,
					this::onePair,
					this::highCard
			);
	
	private int evalStraight(List<Integer> list)	
	{
		// 중복 제거
		list = list
			   .stream()
			   .distinct()
			   .collect(Collectors.toList());
		
		// 정렬하기
		Collections.sort(list,Collections.reverseOrder());
		
		if(list.size() < 5)
		{
			return -1;
		}
		
		int n = 0;
		
		// 스트레이트 검사
		for(int i = 0; i < list.size()-1; i++)
		{
			if(list.get(i) - list.get(i+1) == 1)
			{
				n++;
			}
			else
			{
				n = 0;
			}
			
			if(n >= 4)
			{
				return list.get(i-3);
			}
		}
		
		// 백스트레이트 검사
		if(list.contains(14) && list.contains(2) && list.contains(3) && list.contains(4) && list.contains(5))
		{
			return 5;
		}
		
		return -1;
	}
	
	private PokerRankingList straightFlush()
	{
		// 플러시 조건 만족하나?
		if(flushOrder != null)
		{
			int number = evalStraight(flushOrder);
			
			// 그 녀석들이
			// 스트레이트 조건 만족하나?
			if(number != -1)
			{
				PokerRankingList result;
				
				if(number == 14)
				{
					result = PokerRankingList.royalFlush();
				}
				
				else 
				{
					result = PokerRankingList.straightFlush();
				}
				
				result.addKicker(number);
				return result;
			}
		}
		
		return null;
	}
	
	private PokerRankingList fourOfAKind()
	{
		// 포카드 인가?
		if(pairPattern.getOrDefault(4, 0) >= 1)
		{
			PokerRankingList result = PokerRankingList.fourOfAKind();
			
			for(int i = MAX_CARD_NUMBER; i >= MIN_CARD_NUMBER; i--)
			{
				if(numberList.getOrDefault(i, 0) >= 4)
				{
					result.addKicker(i);
					
					for(int j : numberOrder)
					{
						if(j != i)
						{
							result.addKicker(j);
							return result;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private PokerRankingList fullHouse()
	{
		int three = pairPattern.getOrDefault(3, 0);
		int two = pairPattern.getOrDefault(2, 0);
		
		// 풀하우스 인가?
		if(three >= 2 || (three >= 1 && two >= 1))
		{
			PokerRankingList result = PokerRankingList.fullHouse();
			
			for(int i = MAX_CARD_NUMBER; i >= MIN_CARD_NUMBER; i--)
			{
				// 트리플을 이루는 가장 큰 숫자는?
				if(numberList.getOrDefault(i, 0) >= 3)
				{
					result.addKicker(i);
					
					for(int j = MAX_CARD_NUMBER; j >= MIN_CARD_NUMBER; j--)
					{
						// 트리플을 이루는 숫자를 제외하고
						// 페어를 이룬 가장 큰 숫자는?
						if(j != i && numberList.getOrDefault(j, 0) >= 2)
						{
							result.addKicker(j);
							return result;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private PokerRankingList flush()
	{
		// 플러시 인가?
		if(flushOrder != null)
		{
			PokerRankingList result = PokerRankingList.flush();
			
			int n = 0;
			// 플러시를 이루는 숫자 중 가장 큰 숫자 5개는?
			for(int i : flushOrder)
			{
				result.addKicker(i);
				n++;
				
				if(n >= 5)
				{
					return result;
				}
			}
		}
		
		return null;
	}
	
	private PokerRankingList straight()
	{
		int straight = evalStraight(numberOrder);

		if(straight != -1)
		{
			PokerRankingList result;
			
			if(straight == 14)
			{
				result = PokerRankingList.mountain();
			}
			
			else if(straight == 5)
			{
				result = PokerRankingList.backStraight();
			}
			
			else
			{
				result = PokerRankingList.straight();
			}
			
			result.addKicker(straight);
			return result;
		}
				
		return null;
	}
	
	private PokerRankingList threeOfAKind()
	{
		// 트리플인가?
		if(pairPattern.getOrDefault(3, 0) >= 1)
		{
			PokerRankingList result = PokerRankingList.threeOfAKind();
			
			for(int i = MAX_CARD_NUMBER; i >= MIN_CARD_NUMBER; i--)
			{
				// 트리플을 이루는 카드 중 가장 큰 숫자는?
				if(numberList.getOrDefault(i, 0) >= 3)
				{
					result.addKicker(i);
					
					int n = 0;
					
					for(int j : numberOrder)
					{
						// 트리플을 이룬 숫자를 제외하고
						// 가장 큰 숫자 2개는?
						if(j != i)
						{
							result.addKicker(j);
							n++;
						}
						
						if(n >= 2)
						{
							return result;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private PokerRankingList twoPair()
	{
		// 투페어 인가?
		if(pairPattern.getOrDefault(2, 0) >= 2)
		{
			PokerRankingList result = PokerRankingList.twoPair();
			
			for(int i = MAX_CARD_NUMBER; i >= MIN_CARD_NUMBER; i--)
			{
				// 하이 페어는 ?
				if(numberList.getOrDefault(i, 0) >= 2)
				{
					result.addKicker(i);
					
					for(int j = MAX_CARD_NUMBER; j >= MIN_CARD_NUMBER; j--)
					{
						// 로우 페어는?
						if(j != i && numberList.getOrDefault(j, 0) >= 2)
						{
							result.addKicker(j);
							
							for(int k : numberOrder)
							{
								// 하이 페어와 로우 페어를 이루는 숫자를 제외하고 가장 큰 숫자는?
								if(k != i && k != j)
								{
									result.addKicker(k);
									return result;
								}
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private PokerRankingList onePair()
	{
		// 원페어 인가?
		if(pairPattern.getOrDefault(2, 0) >= 1)
		{
			PokerRankingList result = PokerRankingList.onePair();
			
			for(int i = MAX_CARD_NUMBER; i >= MIN_CARD_NUMBER; i--)
			{
				// 페어를 이루는 가장 큰 수는?
				if(numberList.getOrDefault(i, 0) >= 2)
				{
					result.addKicker(i);
					
					int n = 0;
					
					for(int j : numberOrder)
					{
						// 페어를 이루는 수를 제외하고 가장 큰 수 3개는?
						if(j != i)
						{
							result.addKicker(j);
							n++;
						}
						
						if(n >= 3)
						{
							return result;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	private PokerRankingList highCard()
	{
		PokerRankingList result = PokerRankingList.highCard();
		
		int n = 0;
		
		for(int i : numberOrder)
		{
			result.addKicker(i);
			n++;
			
			if(n >= 5)
			{
				return result;
			}
		}
		
		return null;
	}
}