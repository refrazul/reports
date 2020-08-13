package org.palina.reports

import javax.swing.GroupLayout
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JFrame
import java.awt.EventQueue

import org.palina.reports.ReportsApplication
import org.palina.reports.service.ReportsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class ReportsApplication extends JFrame {

	private ReportsService reportsService 

	@Autowired	
	public ReportsApplication(ReportsService reportsService) {
		this.reportsService = reportsService
		initUI(this.reportsService)
	}

	private void initUI(final ReportsService reportsService) {
		def quitButton = new JButton("Quit");
		ReportsService rs = reportsService
		
		quitButton.addActionListener( { 		
			rs.executeReport(null,null)
			System.exit(0);
		});

		createLayout(quitButton);

		setTitle("Quit button");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void createLayout(JComponent []arg) {

		def pane = getContentPane();
		def gl = new GroupLayout(pane);
		pane.setLayout(gl);

		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(gl.createSequentialGroup()
		.addComponent(arg[0])
		);

		gl.setVerticalGroup(gl.createSequentialGroup()
		.addComponent(arg[0])
		);
	}

	static void main(String[] args) {
		
		def ctx = new SpringApplicationBuilder(ReportsApplication.class)
                .headless(false).run(args);

        EventQueue.invokeLater( {
            def ex = ctx.getBean(ReportsApplication.class);
            ex.setVisible(true);
        });
	}
}
