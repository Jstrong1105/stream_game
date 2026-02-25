package domain.pokergamble;

import java.util.ArrayList;
import java.util.List;

import domain.trumpcard.Card;
import domain.trumpcard.CardPrinter;

/*
 * 각각의 플레이어의 카드 뭉치
 */
class HandCard
{
	private List<Card> handCard;	// 카드 뭉치
	
	private static final PokerHandEvaluator evaluator;	// 족보 계산기
	
	static
	{
		evaluator = new PokerHandEvaluator();
	}
	
	HandCard()
	{
		handCard = new ArrayList<Card>();
	}
	
	// 카드 받기
	void drawCard(Card card)
	{
		handCard.add(card);
	}
	
	// 카드 오픈하기
	void openCard(int index)
	{
		if(index < 0 || index >= handCard.size())
		{
			throw new IllegalArgumentException("존재하지 않는 인덱스입니다.");
		}
		
		handCard.get(index).openCard();
	}
	
	// 카드 개수 반환하기
	int countCard()
	{
		return handCard.size();
	}
	
	// 카드 출력하기
	void printCard()
	{
		CardPrinter.print(handCard);
	}
	
	// 카드 족보 계산하기
	PokerRankingList getHandRanking()
	{
		return evaluator.getRanking(handCard);
	}
}