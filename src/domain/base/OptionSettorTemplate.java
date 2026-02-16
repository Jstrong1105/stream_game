package domain.base;

public interface OptionSettorTemplate<T>
{
	String getName();
	String getExplain();
	void setting(T option);
}
