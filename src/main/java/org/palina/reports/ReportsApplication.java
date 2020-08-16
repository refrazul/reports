package org.palina.reports;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;

import org.palina.reports.ReportsApplication;
import org.palina.reports.dto.ReporteDto;
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
	private ReporteDto reporte;

	@Autowired	
	public ReportsApplication(ReportsService reportsService) {
		this.reportsService   = reportsService;		
		this.reporte = new ReporteDto();
		initUI(this.reportsService, reporte);
	}

	private void initUI(final ReportsService reportsService, ReporteDto reporte) {
		JButton quitButton = new JButton("Quit");
		ReportsService rs = reportsService;
		
		 quitButton.addActionListener((ActionEvent event) -> {
			 rs.readQueryParams("dsd");
				System.exit(0);
	      });

		 createLayout(reporte);

		setTitle("Reporteador");
		setSize(500, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void createLayout(ReporteDto reporte) {
		// array of string contating cities 
        String s1[] = { "Seleccionar" , "DB Provedores", "DB Clientes", "DB Transacciones"}; 
  		
		Container pane = getContentPane();
		JPanel jPanelComboDb = new JPanel();
		JButton btnTestDb = new JButton("Validar conexión");
		btnTestDb.setVisible(false);
		
		
		JLabel lblBaseDatos = new JLabel("Selecciona una base de datos");
        JComboBox<String> comboBases = new JComboBox<String>(s1); 
		jPanelComboDb.add(lblBaseDatos);
		jPanelComboDb.add(comboBases);
		jPanelComboDb.add(btnTestDb);
		
		comboBases.addItemListener((ItemEvent event) -> {			 
			if (event.getStateChange() == ItemEvent.SELECTED) {
		          String item = (String) event.getItem();
		          if(!item.equals("Seleccionar")) {
		        	  btnTestDb.setVisible(true);
		          }else {
		        	  btnTestDb.setVisible(false);
		          }		          
			}
	    });
		
		JPanel jPanelFile = new JPanel();
		JButton btnChooseFile = new JButton("Seleccionar reporte");
		jPanelFile.add(btnChooseFile);
		
		btnChooseFile.addActionListener((ActionEvent event) -> {
			String path = FileSystemView.getFileSystemView().getHomeDirectory() + "/reports";
			JFileChooser jfc = new JFileChooser(path);
			jfc.setFileFilter(new javax.swing.filechooser.FileFilter() {
				
				public String getDescription() {					
					return "SQL reports (*.sql)";
				}
				
				public boolean accept(File f) {
					 if (f.isDirectory()) {
				           return true;
				       } else {
				           String filename = f.getName().toLowerCase();
				           return filename.endsWith(".sql");
				       }
				}
			});
			int returnValue = jfc.showOpenDialog(null);
		
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = jfc.getSelectedFile();
				reporte.setRuta(selectedFile.getAbsolutePath());				
			}

		});
		
		
		pane.setLayout(new BorderLayout());
        this.add(jPanelComboDb,BorderLayout.NORTH);		
        this.add(jPanelFile, BorderLayout.CENTER);
		
		
		
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
