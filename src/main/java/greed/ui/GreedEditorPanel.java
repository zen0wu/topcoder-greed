package greed.ui;

import com.topcoder.client.contestApplet.widgets.RoundBorder;
import greed.util.Log;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GreedEditorPanel extends JPanel implements TalkingWindow {
    private JTextArea logTextArea;

    public GreedEditorPanel() {
        Log.i("Create Greed Editor Panel");
        this.setLayout(new BorderLayout());

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setMargin(new Insets(10, 10, 10, 10));
        logTextArea.setBackground(Color.BLACK);
        logTextArea.setForeground(Color.WHITE);
        logTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(logTextArea);

        TitledBorder border = new TitledBorder(new RoundBorder(Color.decode("0x333333"), 8, true), "Greed says");
        border.setTitleColor(Color.decode("0xCCFF99"));
        scrollPane.setBorder(border);
        this.add(scrollPane);
    }

    @Override
    public void say(String message) {
        logTextArea.append(" - " + message + "\n");
    }

    @Override
    public void clear() {
        logTextArea.setText("");
    }
}
