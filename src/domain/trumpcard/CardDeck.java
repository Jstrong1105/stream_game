package domain.trumpcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.trumpcard.Card.CardNumber;
import domain.trumpcard.Card.CardShape;

/*
 * 카드 뭉치를 들고 있는 카드 덱 객체
 */
public class CardDeck
{
	private List<Card> cardDeck = new ArrayList<>();
	
	// 생성자
	public CardDeck()
	{
		resetDeck();
	}
	
	// 카드 나눠주기
	public Card drawCard()
	{
		if(cardDeck.isEmpty())
		{
			throw new IllegalStateException("카드 덱이 비어있습니다.");
		}
		
		return cardDeck.remove(cardDeck.size()-1);
	}
	
	// 카드 남은 장수 확인하기
	public int getCountCard()
	{
		return cardDeck.size();
	}
	
	// 카드 덱 초기화 하기
	public void resetDeck()
	{
		cardDeck.clear();
		
		for(CardShape shape : CardShape.values())
		{
			for(CardNumber number : CardNumber.values())
			{
				cardDeck.add(new Card(shape,number));
			}
		}
		
		Collections.shuffle(cardDeck);
	}
}
