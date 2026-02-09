package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * 입력을 처리하는 유틸리티
 */
public final class InputHandler
{
	// 외부에서 객체를 생성하지 못하도록 생성자를 private으로 설정
	private InputHandler(){}
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	// 사용자에게 문자를 입력받는 메소드
	public static String readString(String prompt)
	{
		try
		{
			System.out.print(prompt);
			return br.readLine();
		}
		catch (IOException e)
		{
			throw new RuntimeException("입력 에러");
		}
	}
	
	// 사용자에게 숫자를 입력받는 메소드
	public static int readInt(String prompt)
	{
		while(true)
		{
			try
			{
				return Integer.parseInt(readString(prompt));
			}
			catch (NumberFormatException e)
			{
				System.out.println("숫자만 입력하세요.");
			}
		}
	}
	
	// 사용자에게 범위 안의 숫자를 입력받는 메소드
	public static int readInt(String prompt,int min,int max)
	{
		while(true)
		{
			int number = readInt(prompt);
			
			if(number < min || number > max)
			{
				System.out.println(min + " ~ " + max + " 사이로 입력해주세요.");
			}
			else
			{
				return number;
			}
		}
	}
	
	// 사용자에게 boolean 을 입력받는 메소드
	public static boolean readBoolean(String prompt,String y,String n)
	{
		while(true)
		{
			String answer = readString(prompt);
			
			if(answer.equals(y))
			{
				return true;
			}
			else if(answer.equals(n))
			{
				return false;
			}
			else
			{
				System.out.println("다시 입력해주세요.");
			}
		}
	}
}

