package org.palina.reports;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.palina.reports.ReportsApplication;
import org.palina.reports.dto.ReporteDto;
import org.palina.reports.service.ConnectioService;
import org.palina.reports.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import groovy.sql.Sql;

@SpringBootApplication
class ReportsApplication extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private ReportsService reportsService; 
	private ConnectioService connectionService;
	private ReporteDto reporte;
	private Map<String, JTextField> fields;

	@Autowired	
	public ReportsApplication(ReportsService reportsService, ConnectioService connectionService) {
		this.reportsService   = reportsService;		
		this.connectionService = connectionService;
		this.reporte = new ReporteDto();				
		initUI(this.reportsService, connectionService, reporte);
	}

	private void initUI(final ReportsService reportsService, final ConnectioService connectioService,ReporteDto reporte) {
		String s1[] = { "Seleccionar" , "DB Provedores", "DB Clientes", "DB Transacciones"};
		
		Container pane = getContentPane();		
		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        pane.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pane.setLayout(gridbag);
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        JLabel lblSelectDb = new JLabel("Selecciona base de datos");        
        gridbag.setConstraints(lblSelectDb, c);
        add(lblSelectDb);
        
  
        JComboBox<String> comboBases = new JComboBox<String>(s1); 
        gridbag.setConstraints(comboBases, c);
        add(comboBases);
        
        c.gridwidth = GridBagConstraints.REMAINDER; //end row      
        c.weightx = 0.0;  
        JButton btnSeleccionar = new JButton("Seleccionar Reporte");
        gridbag.setConstraints(btnSeleccionar, c);
        add(btnSeleccionar);
        
        c.gridwidth = 0;                
        c.gridheight = 2;
        c.weighty = 1.0; 
        JTextArea txtQuery = new JTextArea("La consulta");
        txtQuery.setEditable(false);
        gridbag.setConstraints(txtQuery, c);
        add(txtQuery);
 
        c.gridwidth = GridBagConstraints.REMAINDER; //end row      
        c.weightx = 0.0;  
        JLabel lblParametros = new JLabel("Parametros");
        gridbag.setConstraints(lblParametros, c);
        add(lblParametros);
        
        c.gridwidth = GridBagConstraints.REMAINDER; //end row      
        c.weightx = 0.0;  
		JPanel panel = new JPanel();
		gridbag.setConstraints(panel, c);
		add(panel);
        
        c.gridwidth = GridBagConstraints.REMAINDER; //end row      
        c.weightx = 0.0;  
        c.weighty = 0.0;
        JButton btnGenerar  = new JButton("Generar Reporte");
        gridbag.setConstraints(btnGenerar, c);
        add(btnGenerar);
        
		setTitle("Reporteador");
		setSize(500, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
	
		
		
		/**Action select DB **/
		comboBases.addItemListener((ItemEvent event) -> {			 
			if (event.getStateChange() == ItemEvent.SELECTED) {
		          String item = (String) event.getItem();
		          if(!item.equals("Seleccionar")) {
		        	  Sql conn = connectionService.getConnection(item);
		        	  
		        	  if(conn!= null) {
		        		  reporte.setConn(conn);
		        		  JOptionPane.showMessageDialog(this, "Conexion establecida con " + item, "Estatus conexión", JOptionPane.INFORMATION_MESSAGE);
		        	  }else {
		        		  JOptionPane.showMessageDialog(this, "No se puedo conectar a " + item, "Estatus conexión", JOptionPane.ERROR_MESSAGE);
		        	  }
		        	  
		          }	          
			}
	    });
		
		/** Action select report **/
		btnSeleccionar.addActionListener((ActionEvent event) -> {
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
				String query = connectioService.getQuery(selectedFile.getAbsolutePath());
				List<String> params = connectioService.getParams(query);
				reporte.setQuery(query);	
				txtQuery.setText(query);
				
				panel.removeAll();
				fields = new HashMap<String,JTextField>();
				JLabel[] labels = new JLabel[params.size()];
 				
				int i = 0;
				for (String entry : params) {
					labels[i] = new JLabel(entry);
					JTextField field = new JTextField();
					
					fields.put(entry, field);
					panel.add(labels[i]);
					panel.add(field);
					i++;
				}
		           
				panel.setLayout(new GridLayout(1,4));  
				System.out.println("Agregados");
			}

		});
		
		/** Action generate report **/
		btnGenerar.addActionListener((ActionEvent event) -> {
			 System.out.println("Generar reporte");
			 Map<String,Object> params = new HashMap<String,Object>();
			 
		       for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
		    	   params.put(entry.getKey(), entry.getValue().getText());		    	 
		       }
		        	
		     reporte.setParams(params);			 
			 reportsService.generateReport(reporte);
			 
	      });
		
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
