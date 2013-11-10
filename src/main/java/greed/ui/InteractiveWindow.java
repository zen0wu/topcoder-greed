package greed.ui;

/**
 * Greed is good! Cheers!
 */
public interface InteractiveWindow {
    public void showLine(String message);
    public void show(String message);

    public void indent();
    public void unindent();

    public void clear();
    public void error(String message);
}
