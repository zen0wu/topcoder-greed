package greed.ui;

import greed.AppInfo;
import greed.Greed;
import greed.util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Greed is good! Cheers!
 */
public class GreedEditorPanel extends JPanel implements InteractiveWindow, ActionListener {

    private static final String NORMAL_PREFIX = "";
    private static final String ERROR_PREFIX = "";
    private static final String NEW_LINE = "\n";

    private JTextPane interactiveWindow;
    private JButton reloadConfigButton;
    private JButton regenerateButton;

    private Style normalStyle, errorStyle;

    private Greed greed;

    public GreedEditorPanel(Greed greed) {
        this.greed = greed;

        interactiveWindow = new JTextPane();
        interactiveWindow.setEditable(false);
        interactiveWindow.setMargin(new Insets(10, 10, 10, 10));
        interactiveWindow.setBackground(Color.BLACK);
        interactiveWindow.setForeground(Color.WHITE);
        interactiveWindow.setFont(new Font("Monospaced", Font.PLAIN, 12));
        interactiveWindow.setMinimumSize(new Dimension(150, 60));

        normalStyle = interactiveWindow.addStyle("Normal Style", null);
        StyleConstants.setForeground(normalStyle, Color.WHITE);
        StyleConstants.setBold(normalStyle, false);
        errorStyle = interactiveWindow.addStyle("Error Style", null);
        StyleConstants.setForeground(errorStyle, Color.RED);
        StyleConstants.setBold(errorStyle, true);

        JScrollPane scrollPane = new JScrollPane(interactiveWindow);

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridwidth = 5;
        constraints.gridheight = 4;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.8;
        constraints.weighty = 0.5;
        constraints.fill = GridBagConstraints.BOTH;
        this.add(scrollPane, constraints);

        reloadConfigButton = new JButton("Reload configuration");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridx = 5;
        constraints.gridy = 1;
        constraints.weightx = 0.01;
        constraints.weighty = 0.25;
        constraints.ipady = 5;
        constraints.insets = new Insets(10, 10, 3, 0);
        reloadConfigButton.setPreferredSize(new Dimension(135, 20));
        reloadConfigButton.setMaximumSize(new Dimension(150, 20));
        reloadConfigButton.setMargin(new Insets(0, 0, 0, 0));
        reloadConfigButton.addActionListener(this);
        this.add(reloadConfigButton, constraints);

        regenerateButton = new JButton("Regenerate code");
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 2;
        constraints.insets = new Insets(3, 10, 10, 0);
        regenerateButton.setPreferredSize(new Dimension(135, 20));
        regenerateButton.setMaximumSize(new Dimension(150, 20));
        regenerateButton.setMargin(new Insets(0, 0, 0, 0));
        regenerateButton.addActionListener(this);
        this.add(regenerateButton, constraints);

        TitledBorder border = new TitledBorder(new EmptyBorder(5, 3, 3, 1), AppInfo.getAppName() + " " + AppInfo.getVersion());
        border.setTitleColor(Color.decode("0xccff99"));
        this.setBorder(border);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object src = actionEvent.getSource();
        if (src == reloadConfigButton) {
            this.showLine("Reload configuration");
            greed.initialize();
        } else if (src == regenerateButton) {
            greed.generateCode(true);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        reloadConfigButton.setEnabled(enabled);
        regenerateButton.setEnabled(enabled);
    }

    private boolean atLineStart = true;
    private int indentLevel = 0;

    @Override
    public void clear() {
        interactiveWindow.setText("");
        indentLevel = 0;
        atLineStart = true;
    }

    @Override
    public void showLine(String message) {
        if (atLineStart) {
            showIndentation();
            appendText(NORMAL_PREFIX, normalStyle);
        }
        appendText(message + NEW_LINE, normalStyle);
        atLineStart = true;
    }

    @Override
    public void show(String message) {
        if (atLineStart) {
            atLineStart = false;
            showIndentation();
            appendText(NORMAL_PREFIX, normalStyle);
        }
        appendText(message, normalStyle);
    }

    private void showIndentation() {
        for (int i = 0; i < indentLevel; ++i)
            appendText("  ", normalStyle);
    }

    private void appendText(String text, Style style) {
        StyledDocument doc = interactiveWindow.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            Log.e("Cannot write to editor panel", e);
        }
    }

    @Override
    public void indent() {
        indentLevel++;
    }

    @Override
    public void unindent() {
        if (indentLevel > 0)
            indentLevel--;
    }

    @Override
    public void error(String message) {
        if (!atLineStart) {
            appendText(NEW_LINE, normalStyle);
            atLineStart = true;
        }
        showIndentation();
        appendText(ERROR_PREFIX + message + NEW_LINE, errorStyle);
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame();
        GreedEditorPanel greedEditorPanel = new GreedEditorPanel(null);
        frame.add(greedEditorPanel);
        frame.setSize(500, 500);

        greedEditorPanel.showLine("Line 1");
        greedEditorPanel.indent();
        greedEditorPanel.showLine("Line 2");
        greedEditorPanel.showLine("Line 3");
        greedEditorPanel.indent();
        greedEditorPanel.showLine("Line 4");
        greedEditorPanel.show("Line 5...");
        greedEditorPanel.unindent();
        greedEditorPanel.unindent();
        greedEditorPanel.show("done");
        greedEditorPanel.showLine("Line 6");
        greedEditorPanel.error("Error 1");
        greedEditorPanel.indent();
        greedEditorPanel.error("Error 2");
        greedEditorPanel.showLine("Line 7");

        frame.pack();
        frame.setVisible(true);

        Thread.sleep(5000);
        greedEditorPanel.clear();
    }

}
