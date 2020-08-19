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
import javax.swing.SwingConstants;
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
		//String s1[] = { "Seleccionar" , "DB Provedores", "DB Clientes", "DB Transacciones"};
		String s1[] = connectioService.getConnectionsNames();  
		String s2[] = { "CSV" , "Excel"};
		
		Container pane = getContentPane();		
		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        pane.setFont(new Font("SansSerif", Font.PLAIN, 14));
        pane.setLayout(gridbag);        
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.weightx = 4;
        JLabel lblSelectDb = new JLabel("Selecciona base de datos");        
        gridbag.setConstraints(lblSelectDb, c);
        add(lblSelectDb);
        
        c.gridx = 1;
        c.weightx = 0.0;
        JComboBox<String> comboBases = new JComboBox<String>(s1); 
        gridbag.setConstraints(comboBases, c);
        add(comboBases);
        
        c.gridx = 2;                
        JButton btnSeleccionar = new JButton("Seleccionar Reporte");
        gridbag.setConstraints(btnSeleccionar, c);
        add(btnSeleccionar);
        
        
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;        
        JLabel lblSelectType = new JLabel("Tipo reporte");        
        gridbag.setConstraints(lblSelectType, c);
        add(lblSelectType);
        
  
        c.gridx = 1;
        c.gridy = 1;
        JComboBox<String> comboReportes = new JComboBox<String>(s2); 
        gridbag.setConstraints(comboReportes, c);
        add(comboReportes);
        
        
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 2; 
        c.gridx = 0;
        c.gridwidth = 3;      
        c.gridheight = 2;
        JTextArea txtQuery = new JTextArea("La consulta");
        txtQuery.setEditable(false);
        gridbag.setConstraints(txtQuery, c);
        add(txtQuery);
        
        c.gridy = 4; 
        c.gridx = 0;
        c.gridwidth = 3;                 
        JLabel lblParametros = new JLabel("Parametros");
        gridbag.setConstraints(lblParametros, c);
        add(lblParametros);
        

        c.gridy = 6; 
        c.gridx = 0;
        c.gridwidth = 3;                 
		JPanel panel = new JPanel();
		gridbag.setConstraints(panel, c);
		add(panel);
        
		
		c.gridy = 16; 
        c.gridx = 1;
        c.gridwidth = 1;     
        JButton btnGenerar  = new JButton("Generar Reporte");
        gridbag.setConstraints(btnGenerar, c);
        add(btnGenerar);
        
        c.gridy = 16; 
        c.gridx = 2;
        JLabel lblMsg= new JLabel("", SwingConstants.CENTER);
        gridbag.setConstraints(lblMsg, c);
        add(lblMsg);
		
		setTitle("Reporteador");
		setSize(500, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		/** Apaga mensjae **/
		Runnable runnable = () -> {
		    try {
		    	Thread.sleep(3000);
		    	lblMsg.setVisible(false);
		    	lblMsg.setText("");
		    }
		    catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		};
		
		
		/**Action select DB **/
		comboBases.addItemListener((ItemEvent event) -> {			 
			if (event.getStateChange() == ItemEvent.SELECTED) {
		          String item = (String) event.getItem();
		          if(!item.equals("Seleccionar")) {
		        	  Sql conn = connectionService.getConnection(item);
		        	  
		        	  if(conn!= null) {
		        		  reporte.setConn(conn);
		        		  message("Conexion establecida con " + item, "Estatus conexión", JOptionPane.INFORMATION_MESSAGE);		        		  
		        	  }else {
		        		  message("No se puedo conectar a " + item, "Estatus conexión", JOptionPane.ERROR_MESSAGE);		        		  
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
			if(reporte.getConn() == null) {
				message("Debe seleccionar una conexión", "Estatus reporte", JOptionPane.ERROR_MESSAGE );
			}else if(reporte.getQuery() == null) {
				message("Debse serleccionar el reporte a ejecutar", "Estatus reporte", JOptionPane.ERROR_MESSAGE );
			}else {
				Map<String,Object> params = new HashMap<String,Object>();
				boolean generar = true;
				
			       for (Map.Entry<String, JTextField> entry : fields.entrySet()) {
			    	   if(entry.getValue().getText().length() == 0) {
			    		   message("Ingrese el valor del parametro " + entry.getKey()  , "Estatus parametro(s)", JOptionPane.ERROR_MESSAGE );
			    		   generar = false;
			    		   break;
			    	   }
			    	   params.put(entry.getKey(), entry.getValue().getText());
			       }
			        	
			       if(generar) {
			    	   String path = FileSystemView.getFileSystemView().getHomeDirectory() + "/reports/output";
			    	   JFileChooser jfc = new JFileChooser(path);
			    	   jfc.setDialogTitle("Guardar reporte");   
			    	   
			    	   int userSelection = jfc.showSaveDialog(this);
			    	   
			    	   if (userSelection == JFileChooser.APPROVE_OPTION) {
			    		   lblMsg.setVisible(true);
			    		   lblMsg.setText("Generando reporte...");
			    		   File fileToSave = jfc.getSelectedFile();
			    		   reporte.setParams(params);
			    		   reporte.setType((String)comboReportes.getSelectedItem());
			    		   reporte.setFile(fileToSave.getAbsolutePath());
			    		   		    		
			    		   reportsService.generateReport(reporte);
			    		   lblMsg.setText("Reporte generado");
			    		   
			    		   Thread thread = new Thread(runnable);
			    		   thread.start();		    		   
			    		}			    	   					   
			       }	
			}							    	   
	      });		
	}
	
	
	private void message(String msg, String title, int type) {
		JOptionPane.showMessageDialog(this, msg, title, type);
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
