package application.extractor_feature;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class AprendizagemMLP {
	
	public static double[] multilayerPerceptron(double caracteristica) {
		double[] retorno = {0,0};
		try {
		
			//*******carregar arquivo de características
			DataSource ds = new DataSource("apenasColuna13.arff");
			Instances instancias = ds.getDataSet();
			instancias.setClassIndex(instancias.numAttributes()-1);
			
			MultilayerPerceptron arvore = new MultilayerPerceptron();
			arvore.setTrainingTime(500);
			arvore.setLearningRate(0.3);
			arvore.setMomentum(0.2);
			arvore.setNumDecimalPlaces(2);
			arvore.setHiddenLayers("4");
			arvore.buildClassifier(instancias);
			
			Instance novo = new DenseInstance(instancias.numAttributes());
			novo.setDataset(instancias);
			novo.setValue(0, caracteristica);
			retorno = arvore.distributionForInstance(novo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
}
