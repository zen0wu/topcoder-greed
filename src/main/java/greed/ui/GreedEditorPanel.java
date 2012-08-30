package greed.ui;

import greed.Greed;
import greed.util.Configuration;
import greed.util.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Greed is good! Cheers!
 */
public class GreedEditorPanel extends JPanel implements TalkingWindow, ActionListener {
    private JTextArea logTextArea;
    private JButton reloadConfigButton;
    private JButton regenerateButton;

    private Greed greed;

    public GreedEditorPanel(Greed greed) {
        this.greed = greed;

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setMargin(new Insets(10, 10, 10, 10));
        logTextArea.setBackground(Color.BLACK);
        logTextArea.setForeground(Color.WHITE);
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logTextArea.setMinimumSize(new Dimension(150, 60));

	    JScrollPane scrollPane = new JScrollPane(logTextArea);

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

        TitledBorder border = new TitledBorder(new EmptyBorder(5, 3, 3, 1), Greed.APP_NAME + " " + Greed.APP_VERSION);
        border.setTitleColor(Color.decode("0xccff99"));
        this.setBorder(border);
    }

    @Override
    public void say(String message) {
        logTextArea.append(" - " + message + "\n");
    }

    @Override
    public void clear() {
        logTextArea.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object src = actionEvent.getSource();
        if (src == reloadConfigButton) {
            this.say("Reloading your configuration from \"" + Configuration.getWorkspace() + "/greed.conf\"");
            Log.i("Reload configuration");
            Configuration.reload();
        }
        else if (src == regenerateButton) {
            this.say("Regeneration!");
            greed.generateCode();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        reloadConfigButton.setEnabled(enabled);
        regenerateButton.setEnabled(enabled);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new GreedEditorPanel(null));
        frame.pack();
        frame.setVisible(true);
    }

}
