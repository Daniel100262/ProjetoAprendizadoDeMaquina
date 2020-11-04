package application.principal;

import java.io.File;
import java.text.DecimalFormat;

import application.extractor_feature.AprendizagemBayesiana;
import application.extractor_feature.AprendizagemMLP;
import application.extractor_feature.ExtractAudioFeature;
import application.extractor_feature.ExtractFeature;
import jAudioFeatureExtractor.jAudioTools.AudioSamples;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PrincipalController {

	@FXML
	private ImageView imageView;
	@FXML private Label l3;
	@FXML private Label lbPctNedFlanders;
	@FXML private Label lbPctKrusty;
	@FXML private Label lbCamisaNed;
	@FXML private Label lbBarbaNed;
	@FXML private Label lbCabeloKrusty;
	@FXML private Label lbBocaKrusty;
	@FXML private Label lbNarizKrusty;
	@FXML private Label lbMatrizColisao;

	private DecimalFormat df = new DecimalFormat("##0.0000");
	private double[] caracteristicasImgSel = {0,0,0,0,0};

	//	@FXML
	//	public void classificar() {
	//		//********Naive Bayes
	//		double[] nb = AprendizagemBayesiana.naiveBayes(caracteristicasImgSel);
	//		lbPctNedFlanders.setText("NED FLANDERS: "+df.format(nb[0]*100) + "%");
	//		lbPctKrusty.setText("KRUSTY: "+df.format(nb[1]*100) + "%");
	//	}

	@FXML
	public void classificar() {
		//********Naive Bayes
//		double[] nb = AprendizagemMLP.multilayer(caracteristicasSomSel);
		//		lbPctNedFlanders.setText("NED FLANDERS: "+df.format(nb[0]*100) + "%");
		//		lbPctKrusty.setText("KRUSTY: "+df.format(nb[1]*100) + "%");
	}

	@FXML
	public void extrairCaracteristicas() throws Exception {
		File cat = new File("C:\\Users\\Gustavo\\eclipse\\Workspace\\ProjetoAprendizadoDeMaquina\\src\\audio\\train\\cat\\cat_1.wav");
		File dog = new File("C:\\Users\\Gustavo\\eclipse\\Workspace\\ProjetoAprendizadoDeMaquina\\src\\audio\\train\\dog\\dog_barking_0.wav");
		double caract = ExtractAudioFeature.extraiCaracteristicas(dog);
		System.out.println(AprendizagemMLP.multilayerPerceptron(caract));
//		extrair();
	}

	public void abreHistograma(double[] cat, double[] dog) {
		try {
			Stage stage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Histograma.fxml"));
			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.setTitle("Histograma");
			stage.initOwner(imageView.getScene().getWindow());
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setResizable(false);
			stage.initStyle(StageStyle.UTILITY);
			stage.show();
			HistogramaController controller = (HistogramaController)loader.getController();
			getGrafico(cat, controller.grafico1);
			getGrafico(dog, controller.grafico2);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void getGrafico(double[] hist, LineChart<String, Number> grafico) {
		XYChart.Series  vlr = new XYChart.Series();

		for (int i = 0; i < hist.length; i++) {
			vlr.getData().add(new XYChart.Data(i+"", hist[i]));
		}
		grafico.getData().addAll(vlr);
		for (Node n: grafico.lookupAll(".default-color0.chart-bar")) {
			n.setStyle("-fx-bar-fill: red;");
		}
	}

	@FXML
	public void selecionaAudio() {
		File f = buscaAudio();
	}

	private File buscaAudio() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new 
				FileChooser.ExtensionFilter("*.WAV","*.wav")); 	
		fileChooser.setInitialDirectory(new File("src/imagens"));
		File imgSelec = fileChooser.showOpenDialog(null);
		try {
			if (imgSelec != null) {
				return imgSelec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@FXML
	public void mostraMatrizDecisao() throws Exception {
		lbMatrizColisao.setText(AprendizagemBayesiana.gerarMatrizConfusao());
	}

	public static void extrair() throws javax.sound.sampled.UnsupportedAudioFileException, java.io.IOException, com.jlibrosa.audio.wavFile.WavFileException, com.jlibrosa.audio.exception.FileFormatNotSupportedException {

		// Cabe�alho do arquivo Weka
		String exportacao = "@relation caracteristicas\n\n";
		exportacao += "@attribute MelSpectrogram real\n";
		exportacao += "@attribute classe {Gato, Cachorro}\n\n";
		exportacao += "@data\n";

		// Diret�rio onde est�o armazenadas as imagens
		File diretorio = new File("C:\\Users\\Gustavo\\eclipse\\Workspace\\ProjetoAprendizadoDeMaquina\\src\\audio\\train\\all");
		File[] arquivos = diretorio.listFiles();

		// Defini��o do vetor de caracter�sticas
		double[] caracteristicas = new double[280];

		// Percorre todas as imagens do diret�rio
		int cont = -1;
		for (File audio : arquivos) {
			cont++;

			caracteristicas[cont] = ExtractAudioFeature.extraiCaracteristicas(audio);

			String classe = caracteristicas[cont] == 0 ?"Gato":"Cachorro";

			System.out.println("MelSpectrogram: " + caracteristicas[cont]);

			exportacao += caracteristicas[cont] + "," + classe + "\n";;

		}

		try {
			File arquivo = new File("caracteristicasAudioCatDog.arff");
			java.io.FileOutputStream f = new 	java.io.FileOutputStream(arquivo);
			f.write(exportacao.getBytes());
			f.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}