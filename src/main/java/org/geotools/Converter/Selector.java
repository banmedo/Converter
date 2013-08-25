package org.geotools.Converter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class Selector {
	private JFrame window = new JFrame();
	private JScrollPane scroll = new JScrollPane();
	private JCheckBox[] field;
	private JButton check = new JButton("Select all"),
			uncheck = new JButton("Unselect all"),
			done = new JButton("Proceed");
	private JPanel panel1 = new JPanel(),
			paneltop = new JPanel(),
			panel2 = new JPanel();
	private JLabel label = new JLabel("Check the fields you want to export for ");
	private int n;
	private ArrayList<String> selection = new ArrayList<String>();
	boolean looper = true;
	
	public ArrayList<String> getSelection(ArrayList<String> list,String s){
		n = list.size();
		field = new JCheckBox[n];
		Color c = Color.yellow;
		window.setSize(300,200);
		window.setLocationRelativeTo(null);
		window.setUndecorated(true);
		window.setTitle("Select fields for "+s);
		window.setLayout(new BorderLayout());
		panel1.setLayout(new BoxLayout(panel1,BoxLayout.Y_AXIS));
		label.setText(label.getText()+s);
		panel1.setBackground(c);
		for(int i=0;i<n;i++){
			field[i] = new JCheckBox(list.get(i));
			field[i].setBackground(c);
			field[i].setSelected(true);
			panel1.add(field[i]);
		}
		scroll.getViewport().add( panel1 );
		
		panel2.setBackground(Color.LIGHT_GRAY);
		panel2.setLayout(new FlowLayout());
		panel2.add(check);
		panel2.add(uncheck);
		panel2.add(done);
		
		check.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(int i=0;i<n;i++){
					field[i].setSelected(true);
				}
			}
		});
		uncheck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(int i=0;i<n;i++){
					field[i].setSelected(false);
				}
			}
		});
		done.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				for(int i=0;i<n;i++){
					if (field[i].isSelected()) selection.add(field[i].getText());
				}
				window.setVisible(false);
				looper =false;
			}
		});
		paneltop.add(label,SwingConstants.CENTER);
		paneltop.setBackground(Color.LIGHT_GRAY);
		window.add(paneltop,BorderLayout.NORTH);
		window.add(scroll,BorderLayout.CENTER);
		window.add(panel2,BorderLayout.SOUTH);
		window.setVisible(true);
		while(looper){}
		return selection;
	}
	
}
