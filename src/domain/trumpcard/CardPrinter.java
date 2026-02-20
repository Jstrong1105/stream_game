package domain.trumpcard;

import java.util.List;
import java.util.stream.IntStream;

public final class CardPrinter
{
	private CardPrinter() {}
	
	private static final char QUESTION = '?';
	
	private static final String    TOP = "┌─────┐ ";
	private static final String MIDDLE = "│  %c  │ ";		// 모양
  //private static final String MIDDLE = "│  %c  │ ";		// 숫자
	private static final String BOTTOM = "└─────┘ ";
	
	public static void print(List<Card> cards)
	{
		// 출력할 문자들을 담아둘 StringBuilder들
		StringBuilder[] lines = 
		{
			new StringBuilder(),	// 상단
			new StringBuilder(),	// 모양
			new StringBuilder(),	// 숫자
			new StringBuilder()		// 하단
		};
		
		// for 문을 구성하는게 일반적인 상황이지만
		// stream 연습을 하는 중이니 stream으로 해결함
		cards
		.stream()
		.forEach(card ->
		{
			lines[0].append(TOP);
			
			char shape = card.isOpen() ? card.getShape() : QUESTION;
			lines[1].append(String.format(MIDDLE,shape));
			
			char number = card.isOpen() ? card.getPrintNumber() : QUESTION;
			lines[2].append(String.format(MIDDLE,number));
			
			lines[3].append(BOTTOM);
		});
		
		// 마찬가지로 for문이 일반적이지만 
		// stream 연습 
		// 사실 이건 진짜 억지로 쓴거 같은데
		IntStream.range(0, 3)
		.forEach(number -> System.out.println(lines[number].toString()));
		
		// 마지막에 개행을 하지 않도록 만듬
		System.out.print(lines[3].toString());
	}
}