package application.extractor_feature;


import java.io.File;
import java.io.IOException;

import com.jlibrosa.audio.JLibrosa;
import com.jlibrosa.audio.exception.FileFormatNotSupportedException;
import com.jlibrosa.audio.wavFile.WavFile;
import com.jlibrosa.audio.wavFile.WavFileException;


public class ExtractAudioFeature {

	public static double extraiCaracteristicas(File f) throws IOException, WavFileException, FileFormatNotSupportedException {
		double caracteristica1 = 0;
		WavFile wavFile = WavFile.openWavFile(f);
		int sampleRate = (int)wavFile.getSampleRate();
		int duration = (int)wavFile.getDuration();
		//---
		JLibrosa jLibrosa = new JLibrosa();
		float[] magnitude = jLibrosa.loadAndRead(f.getAbsolutePath(), sampleRate, -1);
		wavFile.close();
		float [][] melSpectrogramGerado = jLibrosa.generateMelSpectroGram(magnitude, sampleRate, 2048, 128, 256);
		System.out.println("Mel Spectrogram: "+melSpectrogramGerado[0][0]);
		caracteristica1 = melSpectrogramGerado[0][0];
		return caracteristica1;
	}
	
}
