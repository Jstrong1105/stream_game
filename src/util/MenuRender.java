package util;

import domain.base.OptionSetterTemplate;

/*
 * 옵션 변경 메뉴 출력기
 */
public final class MenuRender
{
	private MenuRender() {}
	
	public static <T , E extends Enum<E> & OptionSetterTemplate<T>> void menuRender(T option,E[] menu,String title)
	{
		while(true)
		{
			System.out.println("===== " + title + " =====");
			
			System.out.println("0. 뒤로가기");
			
			for(E e : menu)
			{
				System.out.println( (e.ordinal()+1) + ". " + e.getName() + " : " + e.getExplain());
			}
			
			int answer = InputHandler.readInt("번호를 선택 : ",0,menu.length);
			
			if(answer == 0)
			{
				break;
			}
			else
			{
				menu[answer-1].setting(option);
				System.out.println("옵션이 변경되었습니다.");
			}
		}
	}
}
