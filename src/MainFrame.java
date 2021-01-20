import javax.swing.*;

public class MainFrame extends JFrame{
    private JPanel panel;

    public MainFrame(String title){
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        panel.add(new CP());
//        panel.revalidate();
//        bp= new BouncingPanel();
//        panel.add(bp);
        setSize(1700,1000);

//        pack();
        setVisible(true);
    }


    public static void main(String[] args) {
        MainFrame frame = new MainFrame("bouncing");
    }


}
