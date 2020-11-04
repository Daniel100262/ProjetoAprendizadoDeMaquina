package application.extractor_feature;

import weka.classifiers.trees.J48;

import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class AprendizagemBayesiana {
	
	public static double[] naiveBayes(double[]caracteristicas) {
		double[] retorno = {0,0};
		try {
			//*******carregar arquivo de características
			DataSource ds = new DataSource("caracteristicas.arff");
			Instances instancias = ds.getDataSet();
			//instancias.setClassIndex(6);
			instancias.setClassIndex(instancias.numAttributes()-1);
			//Classifica com base nas características da imagem selecionada
			NaiveBayes nb = new NaiveBayes();
			nb.buildClassifier(instancias);//aprendizagem (tabela de probabilidades)
			
			
			Instance novo = new DenseInstance(instancias.numAttributes());
			novo.setDataset(instancias);
			novo.setValue(0, caracteristicas[0]);
			novo.setValue(1, caracteristicas[1]);
			novo.setValue(2, caracteristicas[2]);
			novo.setValue(3, caracteristicas[3]);
			novo.setValue(4, caracteristicas[4]);
			novo.setValue(5, caracteristicas[5]);
			
			retorno = nb.distributionForInstance(novo);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
	
	public static String gerarMatrizConfusao() throws Exception {
		 DataSource ds = new DataSource("caracteristicas.arff");
		 Instances instancias = ds.getDataSet();
		 instancias.setClassIndex(instancias.numAttributes()-1);
		 Classifier cls = new J48();
		 cls.buildClassifier(instancias);
		 Evaluation eval = new Evaluation(instancias);
		 Random rand = new Random(1);
		 int folds = 10;
		 eval.crossValidateModel(cls, instancias, folds, rand);
		 return "MATRIZ DE CONFUSÃO::::" + eval.toMatrixString("");
	}

}
