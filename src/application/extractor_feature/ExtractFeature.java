package application.extractor_feature;

import java.io.File;
import java.io.FileOutputStream;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class ExtractFeature {

	
	
	public static double[] extraiCaracteristicas(File f) {
		
		double[] caracteristicas = new double[6];
		
		double verdeCamisaNed = 0;
		double narizKrusty = 0;
		double cabeloKrusty = 0;
		double bocaKrusty = 0;
		double barbaNedFlanders = 0;
		
		
		Image img = new Image(f.toURI().toString());
		PixelReader pr = img.getPixelReader();
		
		Mat imagemOriginal = Imgcodecs.imread(f.getPath());
        Mat imagemProcessada = imagemOriginal.clone();
		
		int w = (int)img.getWidth();
		int h = (int)img.getHeight();
		
		
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				
				Color cor = pr.getColor(j,i);
				
				double r = cor.getRed()*255; 
				double g = cor.getGreen()*255;
				double b = cor.getBlue()*255;
				
				if(isCamisaVerdeNedFlanders(r, g, b)) {
					verdeCamisaNed++;
					imagemProcessada.put(i, j, new double[]{0, 255, 128});
				}
				if(i < (h/2 + h/3) && i > (h/3) && isBarbaNedFlanders(r, g, b)) {
					barbaNedFlanders++;
					imagemProcessada.put(i, j, new double[]{0, 255, 255});
				}
				if(i < (h/2) && isNarizKrusty(r, g, b)) {
					narizKrusty++;
					imagemProcessada.put(i, j, new double[]{0, 255, 128});
				}
				if (i < (h/2) && isCabeloKrusty(r, g, b)) {
					cabeloKrusty++;
					imagemProcessada.put(i, j, new double[]{0, 255, 128});
				}
				if(i < (h/2 + h/3) && isBocaKrusty(r, g, b)) {
					bocaKrusty++;
					imagemProcessada.put(i, j, new double[]{0, 255, 255});
				}
				
			}
		}
		
		// Normaliza as características pelo número de pixels totais da imagem para %
        verdeCamisaNed = (verdeCamisaNed / (w * h)) * 100;
        narizKrusty = (narizKrusty / (w * h)) * 100;
        cabeloKrusty = (cabeloKrusty / (w * h)) * 100;
        bocaKrusty = (bocaKrusty / (w * h)) * 100;
        barbaNedFlanders = (barbaNedFlanders / (w * h)) * 100;
        
        caracteristicas[0] = verdeCamisaNed;
        caracteristicas[1] = barbaNedFlanders;
        caracteristicas[2] = cabeloKrusty;
        caracteristicas[3] = bocaKrusty;
        caracteristicas[4] = narizKrusty;
        //APRENDIZADO SUPERVISIONADO - JÁ SABE QUAL A CLASSE NAS IMAGENS DE TREINAMENTO
        caracteristicas[5] = f.getName().charAt(0)=='n'?0:1;
		
        HighGui.imshow("Imagem original", imagemOriginal);
        HighGui.imshow("Imagem processada", imagemProcessada);
        
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
		
		return caracteristicas;
	}
	
	public static boolean isCamisaVerdeNedFlanders(double r, double g, double b) {
		 if (b >= 16 && b <= 42 &&  g >= 55 && g <= 104 &&  r >= 45 && r <= 70) {                       
         	return true;
         }
		 return false;
	}
	public static boolean isNarizKrusty(double r, double g, double b) {
		if (b >= 25 && b <= 39 &&  g >= 9 && g <= 50 &&  r >= 152 && r <= 215) {                       
			return true;
		}
		return false;
	}
	public static boolean isCabeloKrusty(double r, double g, double b) {
		if (b >= 107 && b <= 135 &&  g >= 125 && g <= 140 &&  r >= 0 && r <= 35) {                       
			return true;
		}
		return false;
	}
	public static boolean isBocaKrusty(double r, double g, double b) {
		if (b >= 70 && b <= 105 &&  g >= 103 && g <= 168 &&  r >= 180 && r <= 240) {                       
			return true;
		}
		return false;
	}
	public static boolean isBarbaNedFlanders(double r, double g, double b) {
		if (b >= 0 && b <= 39 &&  g >= 27 && g <= 97 &&  r >= 63 && r <= 131) {                       
			return true;
		}
		return false;
	}


	public static void extrair() {
				
	    // Cabeçalho do arquivo Weka
		String exportacao = "@relation caracteristicas\n\n";
		exportacao += "@attribute camisa_ned_flanders real\n";
		exportacao += "@attribute barba_ned_flanders real\n";
		exportacao += "@attribute cabelo_krusty real\n";
		exportacao += "@attribute boca_krusty real\n";
		exportacao += "@attribute nariz_krusty real\n";
		exportacao += "@attribute classe {NedFlanders, Krusty}\n\n";
		exportacao += "@data\n";
		
		
	        
	    // Diretório onde estão armazenadas as imagens
	    File diretorio = new File("src\\application\\imagens");
	    File[] arquivos = diretorio.listFiles();
	    
        // Definição do vetor de características
        double[][] caracteristicas = new double[2600][6];
        
        // Percorre todas as imagens do diretório
        int cont = -1;
        for (File imagem : arquivos) {
        	cont++;
        	caracteristicas[cont] = extraiCaracteristicas(imagem);
        	
        	String classe = caracteristicas[cont][5] == 0 ?"NedFlanders":"Krusty";
        	
        	System.out.println("Camisa Ned Flanders: " + caracteristicas[cont][0] 
            		+ " - Barba Ned Flanders: " + caracteristicas[cont][1] 
            		+ " - Cabelo Krusty: " + caracteristicas[cont][2] 
            		+ " - Boca Krusty: " + caracteristicas[cont][3] 
            		+ " - Nariz Krusty: " + caracteristicas[cont][4] 
            		+ " - Classe: " + classe);
        	
        	exportacao += caracteristicas[cont][0] + "," 
                    + caracteristicas[cont][1] + "," 
        		    + caracteristicas[cont][2] + "," 
                    + caracteristicas[cont][3] + "," 
        		    + caracteristicas[cont][4] + "," 
                    + classe + "\n";
        }
        
     // Grava o arquivo ARFF no disco
        try {
        	File arquivo = new File("caracteristicas.arff");
        	FileOutputStream f = new FileOutputStream(arquivo);
        	f.write(exportacao.getBytes());
        	f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

}
