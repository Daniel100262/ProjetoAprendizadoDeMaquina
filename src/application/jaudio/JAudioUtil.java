package application.jaudio;

import jAudioFeatureExtractor.AudioFeatures.SpectralCentroid;
import jAudioFeatureExtractor.jAudioTools.AudioSamples;

public class JAudioUtil {
	
	public double[] getFeatureSpectralCentroid(AudioSamples audioSample) throws Exception {
		SpectralCentroid spectralCentroid = new SpectralCentroid();
		return spectralCentroid.extractFeature(audioSample.getSamplesMixedDown(), audioSample.getSamplingRateAsDouble(), audioSample.getSampleWindowsMixedDown(512));
	}

}
