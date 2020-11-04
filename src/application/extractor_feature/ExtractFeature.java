package application.extractor_feature;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.jlibrosa.audio.JLibrosa;
import com.jlibrosa.audio.exception.FileFormatNotSupportedException;
import com.jlibrosa.audio.wavFile.WavFile;
import com.jlibrosa.audio.wavFile.WavFileException;


public class ExtractFeature {
	
	public static double[] extraiCaracteristicas(File f) throws UnsupportedAudioFileException, IOException, WavFileException, FileFormatNotSupportedException{
		
		double[] caracteristicas = new double[2]; //Apenas magnitude(0) e classe(1)
		float[][] magnitude;// = new float[0][0];
		
		float[] MagnitudeFeature = new float[0];
		MagnitudeFeature = extraiMagnitudeFeature(f,magnitude);

		JLibrosa jLibrosa2 = new JLibrosa();
		WavFile wavFile = WavFile.openWavFile(f);
		int mSampleRate = (int) wavFile.getSampleRate();
		float [][] melSpectrogramGerado = jLibrosa2.generateMelSpectroGram(MagnitudeFeature, mSampleRate, 2048, 128, 256);
		
		double MediaAreaMomentosMFCC = melSpectrogramGerado[0][0];
		
		caracteristicas[0] = MediaAreaMomentosMFCC;
        //APRENDIZADO SUPERVISIONADO - JÁ SABE QUAL A CLASSE NAS IMAGENS DE TREINAMENTO
        caracteristicas[1] = f.getName().charAt(0)=='G'?0:1; //GATO=0, CAO=1
        
        System.out.println(caracteristicas);
		
		return caracteristicas;
	}
	
	public static double extraiCarct (File f) throws IOException, WavFileException, FileFormatNotSupportedException {
		double caracteristica1 = 0;
		
		WavFile wavfile = WavFile.openWavFile(f);
		int sampleRate = (int)wavfile.getSampleRate();
		int duration = (int)wavfile.getDuration();
		
		JLibrosa jLibrosa = new JLibrosa();
		
		float[] magnitude = jLibrosa.loadAndRead(f.getAbsolutePath(), sampleRate, duration);
		wavfile.close();
		float [][] melSpectogramGerado = jLibrosa.generateMelSpectroGram(magnitude, sampleRate, 2048, 128, 256);
		System.out.println("Mel Spectrogram: "+melSpectogramGerado[0][0]);
		caracteristica1 = melSpectogramGerado[0][0];
		return caracteristica1;
	}
		
	
	public static float[] extraiMagnitudeFeature (File f,float[][] magnitude) throws IOException, WavFileException, FileFormatNotSupportedException {
		
		WavFile wavFile = WavFile.openWavFile(f);;
		int mNumFrames = (int) (wavFile.getNumFrames());
		int mChannels = wavFile.getNumChannels();
		wavFile.close();
		
		DecimalFormat df = new DecimalFormat("#,#####");
		df.setRoundingMode(RoundingMode.CEILING);
		
		// take the mean of amplitude values across all the channels and convert the
		// signal to mono mode
		
		float[] melSpectrogram = new float[mNumFrames];
				
		for (int q = 0; q < mNumFrames; q++) {
			double frameVal = 0;
			for (int p = 0; p < mChannels; p++) {
				frameVal = frameVal + magnitude[p][q];
			}
			melSpectrogram[q] = Float.parseFloat(df.format(frameVal / mChannels));
		}

		return melSpectrogram;
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
        	//caracteristicas[cont] = extraiCaracteristicas(imagem);
        	
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
