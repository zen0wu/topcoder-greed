package greed.ui;

import greed.util.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Greed is good! Cheers!
 */
public class ConfigurationDialog extends JDialog implements ActionListener {
    private JButton saveButton;
    private JButton cancelButton;

    private JLabel label;
    private JTextField workspaceFolder;

    public ConfigurationDialog() {
        super((JFrame) null, "Greed configuration", true);
        setSize(new Dimension(200, 100));
        super.setLocationRelativeTo(getParent());

        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        label = new JLabel("Workspace: ");
        label.setForeground(Color.WHITE);
        contentPane.add(label);

        workspaceFolder = new JTextField("", 35);
        workspaceFolder.setBackground(Color.BLACK);
        workspaceFolder.setForeground(Color.WHITE);
        workspaceFolder.setMinimumSize(new Dimension(100, 30));
        workspaceFolder.setText(Configuration.getWorkspace());
        contentPane.add(workspaceFolder);

        saveButton = new JButton("Verify & Save");
        contentPane.add(saveButton);

        cancelButton = new JButton("Cancel");
        contentPane.add(cancelButton);

        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);

        pack();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object src = actionEvent.getSource();
        if (src == saveButton) {
            String workspace = workspaceFolder.getText();
            if (checkSave(workspace))
                this.dispose();
        } else if (src == cancelButton) {
            this.dispose();
        }
    }

    private boolean checkSave(String workspace) {
        if ("".equals(workspace)) {
            showMessageBox("You must specify a workspace");
            return false;
        } else if (!new File(workspace).exists() || !new File(workspace).isDirectory()) {
            showMessageBox("Invalid workspace, either a non-exist path, nor a regular-file with the same name exists");
            return false;
        }

        Configuration.setWorkspace(workspace);
        return true;
    }

    private void showMessageBox(String message) {
        JOptionPane.showConfirmDialog(this, message, "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        new ConfigurationDialog().setVisible(true);
    }
}
