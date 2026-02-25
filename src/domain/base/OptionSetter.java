package domain.base;

public interface OptionSetter<T>
{
	String getName();
	String getExplain();
	void setting(T option);
}
