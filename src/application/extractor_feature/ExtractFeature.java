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
		//MagnitudeFeature = extraiMagnitudeFeature(f,magnitude);

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
	



	public static void extrair() {
				
	    // Cabeçalho do arquivo Weka
		String exportacao = "@relation caracteristicas\n\n";
		exportacao += "@attribute Area Method of Moments of MFCCs Overall Average3 real\n";
		exportacao += "@attribute classe {Gato, Cachorro}\n\n";
		exportacao += "@data\n";
		
		
	        
	    // Diretório onde estão armazenados os sons
	    File diretorio = new File("src\\application\\test");
	    File[] arquivos = diretorio.listFiles();
	    
        // Definição do vetor de características
        double[][] caracteristicas = new double[2600][2];
        
        // Percorre todas as imagens do diretório
        int cont = -1;
        for (File wav : arquivos) {
        	cont++;
        	//caracteristicas[cont] = extraiCaracteristicas(imagem);
        	
        	String classe = caracteristicas[cont][2] == 0 ?"Gato":"Cachorro";
        	
        	
        	exportacao += caracteristicas[cont][0] + "," 
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
