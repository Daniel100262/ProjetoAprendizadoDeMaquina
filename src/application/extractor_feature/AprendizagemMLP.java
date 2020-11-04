package application.extractor_feature;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class AprendizagemMLP {
	
	public static double[] multilayerPerceptron(double[] caracteristicas) {
		double[] retorno = {0,0};
		try {
		
			//*******carregar arquivo de características
			DataSource ds = new DataSource("apenasColuna13.arff");
			Instances instancias = ds.getDataSet();
			instancias.setClassIndex(instancias.numAttributes()-1);
			
			MultilayerPerceptron mlp = new MultilayerPerceptron();
			mlp.setTrainingTime(500);
			mlp.setLearningRate(0.3);
			mlp.setMomentum(0.2);
			mlp.setNumDecimalPlaces(2);
			mlp.setHiddenLayers("4");
			mlp.buildClassifier(instancias);
			
			Instance novo = new DenseInstance(instancias.numAttributes());
			novo.setDataset(instancias);
			novo.setValue(0, caracteristicas[0]);
			retorno = mlp.distributionForInstance(novo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
}
