package domain.trumpcard;

/*
 * 카드 한장을 나타내는 객체
 * 카드 클래스는 public 으로 생성자는 패키지 프라이빗으로
 * 외부에서 사용할 수 있지만 생성할 때는 카드덱 클래스를 통해서만 생성할 수 있다.
 */
public class Card
{
	private final CardShape shape;
	private final CardNumber number;
	private boolean open;
	
	// 카드 모양
	enum CardShape
	{
		SPADE('♤'),
		HEART('♥'),
		DIA('◆'),
		CLUB('♧');
		
		CardShape(char shape)
		{
			this.shape = shape;
		}
		
		private final char shape;
	}
	
	// 카드 숫자
	enum CardNumber
	{
		TWO(2,'2'),
		THREE(3,'3'),
		FOUR(4,'4'),
		FIVE(5,'5'),
		SIX(6,'6'),
		SEVEN(7,'7'),
		EIGHT(8,'8'),
		NINE(9,'9'),
		TEN(10,'T'),
		JACK(11,'J'),
		QUEEN(12,'Q'),
		KING(13,'K'),
		ACE(14,'A');
		
		CardNumber(int number,char print)
		{
			this.number = number;
			this.print = print;
		}
		
		private final int number;
		private final char print;
	}
	
	// 생성자
	Card(CardShape shape, CardNumber number)
	{
		this.shape = shape;
		this.number = number;
		open = false;
	}
	
	// 열기
	public void openCard()
	{
		open = true;
	}
	
	// 닫기
	public void hiddenCard()
	{
		open = false;
	}
	
	// 오픈/히든 반환
	public boolean isOpen() { return open; }
	
	// 모양 반환
	public char getShape() { return shape.shape; }
	
	// 숫자 반환
	public int getNumber() { return number.number; }
	
	// 카드 복사하기
	public Card copyCard()
	{
		return new Card(shape,number);
	}
	
	char getPrintNumber() { return number.print; } 
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) { return true; }
		if(o == null) { return false; }
		if(this.getClass() != o.getClass()) { return false; }
		
		Card card = (Card) o;
		return shape == card.shape && number == card.number;
	}
	
	@Override
	public int hashCode()
	{
		return shape.ordinal()*31 + number.ordinal();
	}
}


