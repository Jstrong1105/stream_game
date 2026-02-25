package domain.memorygame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import domain.trumpcard.Card;
import domain.trumpcard.CardDeck;
import domain.trumpcard.CardPrinter;
import util.GameSleeper;
import util.InputHandler;
import util.ScreenCleaner;

/*
 * 메모리 게임에서 보드판을 담당하는 클래스
 */
class MemoryBoard
{
	private final CardDeck cardDeck;	// 52장의 카드를 가지고 있는 카드 덱
	
	private List<Card> boardCard;		// count * pair 만큼 카드를 들고 있는 바닥에 깔린 카드들
	
	private List<Card> playerCard;		// 플레이어가 고른 카드를 임시로 저장해둘 리스트
	
	private final int cardPair;			// 카드의 개수
	private final int pairSize;			// 카드의 페어 수
	
	// 생성자
	MemoryBoard(int cardPair,int pairSize)
	{
		this.cardPair = cardPair;
		this.pairSize = pairSize;
		
		boardCard = new ArrayList<>();
		playerCard = new ArrayList<>();
		
		cardDeck = new CardDeck();
		
		for(int i = 0; i < cardPair; i++)
		{
			// 카드를 한장 받고
			Card card = cardDeck.drawCard();
			
			for(int j = 0; j < pairSize; j++)
			{
				// 그 카드의 복사본을 보드에 추가한다.
				boardCard.add(card.copyCard());
			}
		}
		
		Collections.shuffle(boardCard);
	}
	
	// 모든 카드를 오픈하는 메소드
	void openAll()
	{
		boardCard
		.stream()
		.filter(card->!card.isOpen())
		.forEach(card->card.openCard());
	}
	
	// 모든 카드를 숨기는 메소드
	void hiddenAll()
	{
		boardCard
		.stream()
		.filter(card->card.isOpen())
		.forEach(card->card.hiddenCard());
	}
	
	// 한장 오픈 하는 메소드
	// 이미 열린 카드를 선택하면 false 를 반환한다.
	boolean openCard(int index)
	{
		if(index < 0 || index >= boardCard.size()) 
		{
			throw new IllegalArgumentException("존재하지않는 카드입니다.");
		}
		
		Card card = boardCard.get(index);
		
		if(card.isOpen())
		{
			return true;
		}
		else
		{
			card.openCard();
			playerCard.add(card);
			return false;
		}
	}
	
	// 페어 수 만큼 골랐는지 확인하는 메소드
	boolean completeSelection()
	{
		return playerCard.size() == pairSize;
	}
	
	// 고른 카드가 전부 일치하는지 확인하는 메소드
	boolean isSameCard() 
	{
		for(int i = 0; i < playerCard.size()-1; i++)
		{
			if(!playerCard.get(i).equals(playerCard.get(i+1)))
			{
				notSameCard();
				return false;
			}
		}
		
		return true;
	}
	
	// 일치 하지 않는 카드가 포함되어 있어서 다시 뒤집는 메소드
	private void notSameCard()
	{
		for(Card card : playerCard)
		{
			card.hiddenCard();
		}
	}
	
	// 플레이어가 고른 카드를 리셋하는 메소드
	void resetChoice()
	{
		playerCard.clear();
	}
	
	// 클리어 여부를 반환하는 메소드
	boolean clear()
	{
		return boardCard
			   .stream()
			   .noneMatch(card->!card.isOpen());
	}
	
	// 카드를 출력하는 메소드
	void printCard()
	{
		List<Card> print = new ArrayList<Card>();
		
		for(int i = 0; i < pairSize; i++)
		{
			print.clear();
			
			for(int j = 0; j < cardPair; j++)
			{
				print.add(boardCard.get(j+i*cardPair));
			}
			
			CardPrinter.print(print);
			System.out.println();
		}
	}
}
