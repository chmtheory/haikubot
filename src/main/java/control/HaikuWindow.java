package control;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HaikuWindow {

    private HaikuListener listener;
    private JTextField inputField;
    private JTextArea textArea;

    public void setup() {
        FlatLightLaf.install();
        JFrame frame = new JFrame("control.HaikuBot");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (listener != null) {
                    listener.notifyShutdown();
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        textArea = new JTextArea(15,50);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        panel.add(scroll);

        inputField = new JTextField(20);
        JButton button = new JButton(">");

        button.addActionListener(e -> sendMessage());

        inputField.addActionListener(e -> sendMessage());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        inputPanel.add(inputField);
        inputPanel.add(button);
        panel.add(inputPanel);

        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public void registerListener(HaikuListener listener) {
        this.listener = listener;
    }

    private void sendMessage() {
        String message = inputField.getText();
        inputField.setText("");
        if (listener != null) {
            listener.notifyMessage(message);
        }
    }

    public void postMessage(String message) {
        textArea.append(message + "\n");
    }
}
