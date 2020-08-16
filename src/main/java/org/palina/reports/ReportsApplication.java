package org.palina.reports;

import javax.swing.JButton;
import javax.swing.JFrame;


import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import org.palina.reports.ReportsApplication;
import org.palina.reports.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
class ReportsApplication extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private ReportsService reportsService; 
	

	@Autowired	
	public ReportsApplication(ReportsService reportsService) {
		this.reportsService = reportsService;
		initUI(this.reportsService);
	}

	private void initUI(final ReportsService reportsService) {
		JButton quitButton = new JButton("Quit");
		ReportsService rs = reportsService;
		
		 quitButton.addActionListener((ActionEvent event) -> {
			 rs.readQueryParams("dsd");
				System.exit(0);
	        });

		 createLayout();

		setTitle("Quit button");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void createLayout() {

		Container pane = getContentPane();
		
		JButton button,button1, button2, button3,button4;
		button = new JButton("button 1");
		button1 = new JButton("button 2");
		button2 = new JButton("button 3");
		button3 = new JButton("button 4");
		button4 = new JButton("button 5");
		this.add(button);
		this.add(button1);
		this.add(button2);
		this.add(button3);
		this.add(button4);
		
		GridLayout gl = new GridLayout(2,3);
		pane.setLayout(gl);
		
		
		}

	public static void main(String[] args) {

		ApplicationContext ctx = new SpringApplicationBuilder(ReportsApplication.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            ReportsApplication ex = ctx.getBean(ReportsApplication.class);
            ex.setVisible(true);
        });
    }
}
