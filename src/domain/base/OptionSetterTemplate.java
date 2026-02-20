package domain.base;

public interface OptionSetterTemplate<T>
{
	String getName();
	String getExplain();
	void setting(T option);
}
