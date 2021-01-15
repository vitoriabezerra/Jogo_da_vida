
/*
 * Autor:Vitoria Maria bezerra da Silva Criated in 7th, September of 2019
 */

package pdi20192;

import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.gui.NewImage;
import ij.gui.Plot;
import ij.gui.PlotWindow;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.util.Scanner;
import java.util.*;

public class lab01_vitoria implements PlugInFilter {

	public lab01_vitoria() {
		int slices; // quantidade de interações
		int life; // quantidade de vida
		int cpf_1; // três primeiros digitos do cpf
		int cpf_2; // três digitos do meio do cpf
		int cpf_3; // três ultimos digitos do cpf
		int cpf_dig; // dois ultimos dígitos do cpf
		int perc; // porcentagem de celulas vivas inicialmente
		int h; // altura da imagem
		int w; // largura da imagem
		int size; // dimensão da imagem
		int xran; // posição aleatoria para x
		int yran; // posição aleatoria para x
		int aux; // variavel auxiliar usada pra preencher a imagem
		ImagePlus image; //
		ImageStack ims;
		ImageProcessor imp1;

		// CRIANDO AS CAIXAS DE DIALOGOS
		GenericDialog box = new GenericDialog("Jogo da vida"); // cria a caixa de dialogo
		box.addNumericField("Escolha o número de gerações para o gif:", 100, 0); // adiciona um campo numerico
		box.addNumericField("Quantidade de vidas:", 7, 0); // adiciona um campo numerico
		box.addNumericField("Digite os três primeiros números do seu CPF:", 702, 0); // adiciona um campo numerico
		box.addNumericField("Três números do meio do seu CPF:", 946, 0); // adiciona um campo numerico
		box.addNumericField("Três últimos do seu CPF sem o digito:", 354, 0); // adiciona um campo numerico
		box.addNumericField("Dois números finais do CPF:", 40, 0); // adiciona um campo numerico
		box.addNumericField("Largura da imagem:", 500, 0); // adiciona um campo numerico
		box.addNumericField("Altura da imagem", 250, 0); // adiciona um campo numerico

		// LENDO OS DADOS E ADICIONANDO NAS VARIÁVEIS
		box.showDialog(); // mostra a caixa de diálogo
		slices = (int) box.getNextNumber(); // pega o 1° valor inserido na caixa de diálogo
		life = (int) box.getNextNumber(); // pega o 2° valor inserido na caixa de diálogo
		cpf_1 = (int) box.getNextNumber(); // pega o 3° valor inserido na caixa de diálogo
		cpf_2 = (int) box.getNextNumber(); // pega o 4° valor inserido na caixa de diálogo
		cpf_3 = (int) box.getNextNumber(); // pega o 5° valor inserido na caixa de diálogo
		cpf_dig = (int) box.getNextNumber(); // pega o 6° valor inserido na caixa de diálogo
		w = (int) box.getNextNumber(); // pega o 7° valor inserido na caixa de diálogo
		h = (int) box.getNextNumber(); // pega o 8° valor inserido na caixa de diálogo

		// System.out.println(escolhe a variavel);

		// CALCULAR O RESTO DE DIVISÃO -> toda vez que dividir por RBG(255bits) sempre
		// será um valor entre 0 e 255, ou seja todos os valores possíveis para 256 bits
		int r = cpf_1 % 256;
		int g = cpf_2 % 256;
		int b = cpf_2 % 256;

		// CALCULAR O COMPLEMENTAR -> Como é para pegar uma cor complementar, e também a
		// imagem são 8 bits por componente (2⁸= 256), vai de 0 a 255
		int rc = (255 - r); // Calculando rc= R complementar
		int gc = (255 - g); // Calculando gc= G complementar
		int bc = (255 - b); // Calculando bc= B complementar

		// PORCENTAGEM DE VIVAS -> perc= porcentagem de células vivas
		if (cpf_dig >= 50) {
			perc = cpf_dig; // Caso os ultimos digitos seja maior que 50, usar os digitos para porcentagem
		} else {
			perc = cpf_dig + 30; // Caso os ultimos dígitos seja menor que 50, somar mais 30
		}

		Color alive1 = new Color(r, g, b); // dá o tom rgb para a variável alive
		int alive = alive1.getRGB(); // transforma a variável em inteira
		Color dead1 = new Color(255, 255, 255); // Toda as células mortas são brancas, então já dá setar o parâmetro com
												// todo o RGB em 255
		int dead = dead1.getRGB(); // transforma a variável em inteira
		Color lifeless1 = new Color(rc, gc, bc); // Dá o tom complementar para variável lifeless
		int lifeless = lifeless1.getRGB(); // transforma a variável em inteira

		// CRIANDO UMA IMAGEM COM O PERCENTUAL DESEJADO
		size = (w * h); // A dimensão da imagem é altura vezes largura
		image = NewImage.createRGBImage("Jogo da vida", w, h, slices, NewImage.FILL_WHITE); // O argumento é (title, int
																							// width, int height, int
																							// slices, int options)
		ims = image.getImageStack(); // Passa a imagem para o stack para poder editar cada imagem, ja que imagem é um
										// objeto que não pode ser editado
		image.show(); // Mostra imagem na tela com a quantidade de frames

		// PREENCHENDO ALEATORIAMENTE O MAPA INICIAL
		Random ran = new Random(); // Criar um objeto aleatório
		int i = 0; // inicia o contador com 0
		int perc1 = (perc * size) / 100; // Para obter a porcentagem, iremos multiplicar a porcentagem(/100)
											// multiplicado pela qtd de pixels
		imp1 = ims.getProcessor(1); // A variavel imp irá acessar o meu processor apenas para imagem 1.

		do {
			xran = ran.nextInt(w); // retorna um valor inteiro de 0 até w para x
			yran = ran.nextInt(h); // retorna um valor inteiro de 0 até h para y
			aux = imp1.getPixel(xran, yran); // usamos uma variável auxiliar para pegar o pixel que já foi escolhido
												// aleatoriamente
			if (aux == dead) { // Para certificar que o mesmo pixel não seja pintado mais de uma vez, vamos
								// restriguir a pintura para apenas celulas mortas, ou seja, as brancas
				imp1.set(xran, yran, alive); // Essa função irá pintar a celula escolhida com a cor do meu cpf
				i++; // o contador irá servir para controlar a estrutura de repetição
			}
		} while (i < perc1); // Ele parará de pintar as celulas até que chegue na porcentagem requerida

		// CRIANDO VARIAVEIS PARA VARRER A IMAGEM
		int x;// variavel utilizada para varrer as linhas da imagem
		int y;// variavel utilizada para varrer as colunas da imagem
		int p;// varivael auxiliar utilizada para varrer os vizinhos horizontais
		int q;// varivael auxiliar utilizada para varrer os vizinhos horizontais
		int[][] vidas = new int[w][h];// criando uma nova matriz para guardar as vidas de cada celula
		int count1 = -1; // A celula ja está viva, logo, temos que tirar-la do calculo

		// CRIANDO VETORES PARA PLOTAR OS GRAFICOS
		double[] vectviva = new double[slices]; // vetor para guardar numero de celulas vivas por geração
		double[] vectmorto = new double[slices];// vetor para guardar numero de celulas vivas por geração
		double[] vectsemvida = new double[slices];// vetor para guardar numero de celulas vivas por geração
		double[] geracoes = new double[slices];// vetor que armazena as gerações
		// contador para celulas sem vida.

		// ESTRUTURA DE REPETIÇÃO PARA VARRER AS IMAGENS
		for (int G = 1; G < slices; G++) {// Fazendo um for para rodar varias gerações de imagens - GIF
			imp1 = ims.getProcessor(G + 1);// O stack vai acessar o proecessor de todas as outras imagens, a partir da
											// segunfa
			// System.out.println(G) para teste;
			geracoes[G] = G;
			int countviva = 0;// contador para celulas vivas
			int countmorto = 0;// contador para celulas mortas
			int countsemvida = 0;
			// ESTRUTURA DE REPETIÇÃO PARA VARRER OS PIXELS
			for (x = 0; x < w; x++) {// Varrendo as linhas até w-1
				for (y = 0; y < h; y++) {// Varrendo as colunas até h-1
					// System.out.println(vidas[x][y]);

					if (ims.getProcessor(G).getPixel(x, y) == alive) {
						countviva++;
					}
					if (ims.getProcessor(G).getPixel(x, y) == dead) {
						countmorto++;
					}
					if (ims.getProcessor(G).getPixel(x, y) == lifeless) {
						countsemvida++;
					}

					vectviva[G] = countviva;// armazena o numero de vivos no vetor para cada geração
					vectmorto[G] = countmorto;// armazena o numero de morto no vetor para cada geração
					vectsemvida[G] = countsemvida;// armazena o numero de celulas sem vida no vetor para cada geração

					if (ims.getProcessor(G).getPixel(x, y) == alive) {// Se a celula a ser analisada os vizinhos for
																		// viva entrará no if e o contador será iniciado
																		// com -1;

						count1 = -1;
						// ESTRUTURA DE REPETIÇÃO PARA VARRER OS VIZINHOS- P(x) e Q(y)
						for (int x1 = x - 1; x1 <= x + 1; x1++) {// Sempre adicionando 1 no eixo x para mais e para
																	// menos.
							for (int y1 = y - 1; y1 <= y + 1; y1++) {// Sempre adicionando 1 no eixo x para mais e para
																		// menos.
								p = x1;// Não queremos que p mude para sempre o seu valor, por isso xi e y1 funcionam
										// como uma variavel auxilar
								q = y1;
								if (p < 0) {// Condição1- Borda esquerda
									p = p + w; // apenas mais w porquq p=-1, Soma-se a largura
								}
								if (q < 0) {// Condição2- Borda superior
									q = q + h; // Soma-se a altura

								}
								if (q >= h) {// Condição 3- Borda Direita
									q = q - h;// Subtrai a altura
								}
								if (p > w) {// Condição 4- Borda inferior
									p = p - w;// Subtrai a largura.

								}

								if (p >= 0 && p < w && q >= 0 && q < h) {// Sobrará apenas os pixels com conectavidade 8

									if (ims.getProcessor(G).getPixel(p, q) == alive) {
										count1++;

									}
								}
							}

						}

						if (count1 < 2 || count1 > 3) {// A celula morre com menos de duas celulas vivas ou mais do que
														// 3.
							imp1.set(x, y, dead);// logo, pintaremos a celula de preto
							vidas[x][y]++;// adiciona 1 no vetor cada vez que o a celula passa de viva pra morta
							if (vidas[x][y] == life) {// se o a vida na matriz for igual a vida estabelecida
								imp1.set(x, y, lifeless);// a celula mudará de cor e ficará na configuração sem vida
								// System.out.println(vidas[x][y]);
							}

						} else {
							imp1.set(x, y, alive);// se não acabar as vidas, continua a vida
						}
					}
					if (ims.getProcessor(G).getPixel(x, y) == dead) {// se a celula estiver morta entra no loop
						count1 = 0;// o contador começa com 0

						for (int x1 = x - 1; x1 <= x + 1; x1++) {
							for (int y1 = y - 1; y1 <= y + 1; y1++) {
								p = x1;
								q = y1;
								if (p < 0) {
									p = p + w; // apenas mais w porquq p=-1
								}
								if (q < 0) {
									q = q + h;

								}
								if (q >= h) {
									q = q - h;
								}
								if (p > w) {
									p = p - w;

								}
								if (p >= 0 && p < w && q >= 0 && q < h) {

									if (ims.getProcessor(G).getPixel(p, q) == alive) {
										count1++;

									} // fecha o if do get pixel para a e b
								}
							} // fecha o quarto for

						} // fecha o terceiro for
							// fecha o if alive

						if (count1 == 3 || count1 > 5) {// Se o numero de vivos vizinhos for 3 ou mais do que 5, se
														// torna uma celula viva.
							imp1.set(x, y, alive);

						} else {
							imp1.set(x, y, dead);
						}
					}
					if (ims.getProcessor(G).getPixel(x, y) == lifeless) {
						imp1.set(x, y, lifeless);

					}
				}

			}

		}
		System.out.println(Arrays.toString(vectsemvida));
		System.out.println(Arrays.toString(vectmorto));
		System.out.println(Arrays.toString(vectviva));
		System.out.println(Arrays.toString(geracoes));

		Plot plotar = new Plot("Grafico de células sem vida", "Gerações", "Número de células");
		plotar.setColor(new Color(255, 0, 0));// plota de vermelho as sem vida
		plotar.add("Celula viva", geracoes, vectsemvida);
		plotar.setColor(new Color(0, 0, 0));// plota de preto as vivas
		plotar.add("Celula morta", geracoes, vectviva);
		plotar.setColor(new Color(0, 0, 255));// plota de azul as mortas
		plotar.add("Célula sem vida", geracoes, vectmorto);
		plotar.show();
		plotar.addLegend("Vermelho- Sem vidas / Preto -Vivas / Azul- Mortas ");

	}

	@Override
	public int setup(String arg, ImagePlus imp) {
		// TODO Auto-generated method stub
		// Nessa parte precisa ter uma imagem já aberta
		return DOES_ALL;
	}

	@Override
	public void run(ImageProcessor ip) {

	}
}