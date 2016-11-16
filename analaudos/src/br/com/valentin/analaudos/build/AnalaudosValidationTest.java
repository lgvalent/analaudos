package br.com.valentin.analaudos.build;


import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgraph.graph.GraphModel;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;

import br.com.valentin.analaudos.entities.DocumentContent;
import br.com.valentin.analaudos.entities.DocumentGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;

public class AnalaudosValidationTest {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void showGraph(Graph g) {
		// http://jgrapht.org/visualizations.html

		// Construct Model and Graph
		GraphModel model = new JGraphModelAdapter(g);

		JGraph graph = new JGraph(model);
		// Control-drag should clone selection
		graph.setCloneable(true);

		// Enable edit without final RETURN keystroke
		graph.setInvokesStopCellEditing(true);

		// When over a cell, jump to its default port (we only have one, anyway)
		graph.setJumpToDefaultPort(true);

		// positioning via jgraphx layouts
		JGraphXAdapter jgxAdapter = new JGraphXAdapter(g);;
		mxCircleLayout layout = new mxCircleLayout(jgxAdapter);
		layout.execute(jgxAdapter.getDefaultParent());

		// Show in Frame
		JFrame frame = new JFrame();
		frame.getContentPane().add(new mxGraphComponent(jgxAdapter));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		
		
		System.out.println("2,3 cm. Olá,2 tudo bem".matches(".*\\.[ a-zA-Z].*"));
		System.out.println("cm.".matches(".*\\.([ a-zA-Z].*|$)"));
		System.out.println(1.0 == 1);

//		if(true)return ;

		DocumentGraph document1 = prepareDocumentGraphTest1();
		AnalaudosDocument docGraph1 = new AnalaudosDocument(document1.getGraphJson());
		System.out.println(docGraph1.toString());
		docGraph1.addActionLogData(document1.getActions());
		System.out.println(docGraph1.toString());

		DocumentGraph document2 = prepareDocumentGraphTest2();
		AnalaudosDocument docGraph2 = new AnalaudosDocument(document2.getGraphJson());
		System.out.println(docGraph2.toString());
		docGraph2.addActionLogData(document2.getActions());
		System.out.println(docGraph2.toString());

		AnalaudosGraph analGraph = new AnalaudosGraph();
		analGraph.addDocGraph(docGraph1);
		analGraph.addDocGraph(docGraph2);
		//        showGraph(documentGraph);
		System.out.println("ANALAUDOS:");
		System.out.println(analGraph.toDot(true));
		System.out.println("DOCUMENTO:");
		System.out.println(docGraph1.toDot(true));
		System.out.println(docGraph2.toDot(true));
		
		AnalaudosDocument docBuilt = analGraph.createDocGraph(document1.getDocumentContent().getContentPlain(), new AnalaudosGraphStrategyDeltaWord());
		System.out.println("BUILT:");
		System.out.println(docBuilt.toDot(true));
		System.out.println(docBuilt.toJson());
		
//		AnalaudosNode n1 = (AnalaudosNode) analGraph.vertexSet().toArray()[0];
//		System.out.println("--->DegreeOf:"+ analGraph.outDegreeOf(n1));
//		for(AnalaudosEdge e: analGraph.edgesOf(n1)){
//			System.out.println("--->"+ e.getSource() + "->" + e.getTarget() + ":" + e);
//			System.out.println("-->" + analGraph.getEdgeWeight(e));
//		}
	}

	public static DocumentGraph prepareDocumentGraphTest1(){
		DocumentContent con = new DocumentContent();
		con.setContent("ULTRASSONOGRAFIA PÉLVICA TRANSVAGINAL. ÚTERO: Apresentando-se em anteversoflexão e mediano. Os seus contornos são regulares e a ecotextura é homogênea. Observa-se estratificação miometrial preservada. Relação corpo-colo normal para a idade. Dimensões: 8,6 x 4,5 x 5,5 cm. Volume estimado 114,7 cm³ O eco endometrial foi bem visibilizado, medindo 14 mm de espessura. Colo uterino de aspecto habitual. Canal endocervical sem alterações ecográficas. OVARIO DIREITO: tópico, contornos regulares e ecotextura habitual. Dimensões: 2,7 x 2,74 x 2,2 cm. Volume estimado: 7,4 cm³. OVÁRIO ESQUERDO: tópico, contornos regulares e ecotextura habitual. Dimensões: 2,7 x 1,6 x 1,6 cm. Volume estimado: 3,4 cm³. FUNDO DE SACO DE DOUGLAS: Sem coleções anormais. IMPRESSÃO DIAGNÓSTICA: - Útero de volume aumenrtado");

		DocumentGraph doc = new DocumentGraph();
		doc.setId(84);
		doc.setAuthor("4aa1fbbc-1efa-45db-b399-6a25cfe9dcda");
		//		doc.setTimeStamp(timeStamp);
		//		doc.setSuggestions(suggestions);
		//		doc.setGraduationYear(graduationYear);
		//		doc.setResidenceYear(residenceYear);
		//		doc.setSpecialistYear(specialistYear);
		//		doc.setMasterYear(masterYear);
		//		doc.setDoctorYear(doctorYear);
		doc.setActions("0:Analaudos.init(); 5:Analaudos.addText(); 8:selectSource(w1); 6369:unselectSource(w1); 8024:selectSource(w1); 9186:createEdge(w1;w0); 10416:selectSource(w2); 11324:createEdge(w2;w0); 17372:selectSource(w3); 18617:createEdge(w3;w0); 20182:selectSource(w4); 20937:createEdge(w4;w3); 22151:selectSource(w6); 23288:createEdge(w6;w3); 25747:selectSource(w8); 28441:createEdge(w8;w3); 31891:selectSource(w11); 33199:createEdge(w11;w3); 34437:selectSource(w13); 35715:createEdge(w13;w11); 37142:selectSource(w16); 39360:createEdge(w16;w3); 41209:selectSource(w18); 42691:createEdge(w18;w16); 50888:selectSource(w20); 51804:createEdge(w20;w21); 54260:selectSource(w22); 55650:createEdge(w22;w20); 62086:selectSource(w25); 63794:createEdge(w25;w23); 65948:selectSource(w23); 67303:createEdge(w23;w24); 69457:selectSource(w28); 72331:createEdge(w28;w23); 75435:selectSource(w29); 78003:createEdge(w29;w3); 79435:selectSource(w30); 80797:createEdge(w30;w29); 82076:selectSource(w32); 83337:createEdge(w32;w29); 84233:selectSource(w34); 85117:createEdge(w34;w29); 86099:selectSource(w35); 87087:createEdge(w35;w34); 88173:selectSource(w35); 89269:createEdge(w35;w32); 90981:selectSource(w35); 94735:createEdge(w35;w30); 99832:selectSource(w37); 100748:createEdge(w37;w36); 102236:selectSource(w38); 103618:createEdge(w38;w36); 104684:selectSource(w36); 107416:createEdge(w36;w3); 108916:selectSource(w39); 109832:createEdge(w39;w38); 115278:selectSource(w44); 118619:createEdge(w44;w41); 120507:selectSource(w42); 122377:createEdge(w42;w41); 124675:selectSource(w45); 126449:createEdge(w45;w41); 128555:selectSource(w45); 129821:unselectSource(w45); 130891:selectSource(w44); 132031:createEdge(w44;w45); 135533:selectSource(w46); 140621:createEdge(w46;w41); 142434:selectSource(w47); 143254:createEdge(w47;w46); 144934:selectSource(w48); 146087:createEdge(w48;w47); 147196:selectSource(w50); 149206:createEdge(w50;w41); 155786:selectSource(w51); 156746:createEdge(w51;w52); 159954:selectSource(w54); 161438:createEdge(w54;w51); 162474:selectSource(w55); 163382:createEdge(w55;w54); 168154:selectSource(w57); 169113:unselectSource(w57); 169941:selectSource(w56); 171049:createEdge(w56;w57); 172833:selectSource(w58); 173931:createEdge(w58;w56); 175129:selectSource(w59); 176905:createEdge(w59;w56); 179409:selectSource(w58); 180341:createEdge(w58;w59); 183279:selectSource(w60); 185209:createEdge(w60;w59); 187793:selectSource(w61); 189512:createEdge(w61;w0); 191044:selectSource(w62); 191881:createEdge(w62;w61); 193592:selectSource(w63); 195292:createEdge(w63;w61); 196730:selectSource(w64); 198230:createEdge(w64;w61); 199718:selectSource(w65); 200658:createEdge(w65;w64); 202096:selectSource(w67); 203016:createEdge(w67;w61); 203860:selectSource(w68); 204592:createEdge(w68;w67); 206192:selectSource(w69); 207194:createEdge(w69;w61); 209372:selectSource(w70); 210320:createEdge(w70;w69); 211698:selectSource(w72); 212856:createEdge(w72;w69); 213756:selectSource(w74); 214544:createEdge(w74;w69); 215352:selectSource(w75); 216219:createEdge(w75;w74); 217184:selectSource(w75); 218688:createEdge(w75;w72); 220033:selectSource(w75); 221061:createEdge(w75;w70); 223777:selectSource(w76); 225541:createEdge(w76;w61); 232299:selectSource(w77); 233001:createEdge(w77;w76); 236693:selectSource(w78); 239373:createEdge(w78;w76); 240651:selectSource(w79); 241403:createEdge(w79;w78); 242971:selectSource(w81); 243765:createEdge(w81;w80); 244433:selectSource(w80); 245819:createEdge(w80;w0); 247347:selectSource(w82); 248505:createEdge(w82;w80); 250817:selectSource(w83); 251797:createEdge(w83;w80); 254856:selectSource(w84); 256326:createEdge(w84;w83); 257758:selectSource(w86); 258622:createEdge(w86;w80); 259626:selectSource(w87); 260462:createEdge(w87;w86); 261476:selectSource(w88); 262520:createEdge(w88;w80); 263794:selectSource(w89); 264696:createEdge(w89;w88); 265434:selectSource(w93); 266206:createEdge(w93;w88); 267224:selectSource(w91); 268438:createEdge(w91;w88); 270754:selectSource(w94); 271834:createEdge(w94;w89); 272856:selectSource(w94); 273570:createEdge(w94;w93); 274728:selectSource(w94); 275867:createEdge(w94;w91); 277731:selectSource(w96); 279074:createEdge(w96;w95); 279873:selectSource(w95); 281407:createEdge(w95;w80); 283811:selectSource(w97); 284893:createEdge(w97;w95); 286219:selectSource(w98); 287185:createEdge(w98;w97); 288527:selectSource(w99); 289527:createEdge(w99;w101); 291223:selectSource(w103); 295183:createEdge(w103;w104); 297506:selectSource(w101); 298490:createEdge(w101;w105); 299705:selectSource(w101); 302690:unselectSource(w101); 303777:selectSource(w105); 305464:cyclicLinkAvoided(w105;w101); 306312:selectSource(w101); 307542:removeEdge(w101;w105); 309057:selectSource(w105); 310380:createEdge(w105;w101); 311790:selectSource(w104); 312650:createEdge(w104;w105); 314822:selectSource(w106); 315532:createEdge(w106;w105); 321542:selectSource(w113); 322974:createEdge(w113;w110); 325718:selectSource(w110); 328134:createEdge(w110;w108)");
		doc.setGraphJson("{\"nodes\":[{\"id\":\"w0\",\"fontColor\":\"blue\" , \"label\":\"ULTRASSONOGRAFIA\", \"word\":\"ultrassonografia\"},{\"id\":\"w1\",\"fontColor\":\"#008000\" , \"label\":\"PÉLVICA\", \"word\":\"pelvica\"},{\"id\":\"w2\",\"fontColor\":\"#008000\" , \"label\":\"TRANSVAGINAL.\", \"word\":\"transvaginal \"},{\"id\":\"w3\",\"fontColor\":\"blue\" , \"label\":\"ÚTERO:\", \"word\":\"utero\"},{\"id\":\"w4\",\"fontColor\":\"#008000\" , \"label\":\"Apresentando-se\", \"word\":\"apresentando-se\"},{\"id\":\"w6\",\"fontColor\":\"#008000\" , \"label\":\"anteversoflexão\", \"word\":\"anteversoflexao\"},{\"id\":\"w8\",\"fontColor\":\"#008000\" , \"label\":\"mediano.\", \"word\":\"mediano \"},{\"id\":\"w11\",\"fontColor\":\"blue\" , \"label\":\"contornos\", \"word\":\"contornos\"},{\"id\":\"w13\",\"fontColor\":\"#008000\" , \"label\":\"regulares\", \"word\":\"regulares\"},{\"id\":\"w16\",\"fontColor\":\"blue\" , \"label\":\"ecotextura\", \"word\":\"ecotextura\"},{\"id\":\"w18\",\"fontColor\":\"#008000\" , \"label\":\"homogênea.\", \"word\":\"homogenea \"},{\"id\":\"w20\",\"fontColor\":\"blue\" , \"label\":\"estratificação\", \"word\":\"estratificacao\"},{\"id\":\"w21\",\"fontColor\":\"blue\" , \"label\":\"miometrial\", \"word\":\"miometrial\"},{\"id\":\"w22\",\"fontColor\":\"#008000\" , \"label\":\"preservada.\", \"word\":\"preservada \"},{\"id\":\"w23\",\"fontColor\":\"blue\" , \"label\":\"Relação\", \"word\":\"relacao\"},{\"id\":\"w24\",\"fontColor\":\"blue\" , \"label\":\"corpo-colo\", \"word\":\"corpo-colo\"},{\"id\":\"w25\",\"fontColor\":\"#008000\" , \"label\":\"normal\", \"word\":\"normal\"},{\"id\":\"w28\",\"fontColor\":\"#008000\" , \"label\":\"idade.\", \"word\":\"idade \"},{\"id\":\"w29\",\"fontColor\":\"blue\" , \"label\":\"Dimensões:\", \"word\":\"dimensoes\"},{\"id\":\"w30\",\"fontColor\":\"blue\" , \"label\":\"8,6\", \"word\":\"8,6\"},{\"id\":\"w32\",\"fontColor\":\"blue\" , \"label\":\"4,5\", \"word\":\"4,5\"},{\"id\":\"w34\",\"fontColor\":\"blue\" , \"label\":\"5,5\", \"word\":\"5,5\"},{\"id\":\"w35\",\"fontColor\":\"#008000\" , \"label\":\"cm.\", \"word\":\"cm \"},{\"id\":\"w36\",\"fontColor\":\"blue\" , \"label\":\"Volume\", \"word\":\"volume\"},{\"id\":\"w37\",\"fontColor\":\"#008000\" , \"label\":\"estimado\", \"word\":\"estimado\"},{\"id\":\"w38\",\"fontColor\":\"blue\" , \"label\":\"114,7\", \"word\":\"114,7\"},{\"id\":\"w39\",\"fontColor\":\"#008000\" , \"label\":\"cm³\", \"word\":\"cm³\"},{\"id\":\"w41\",\"fontColor\":\"blue\" , \"label\":\"eco\", \"word\":\"eco\"},{\"id\":\"w42\",\"fontColor\":\"#008000\" , \"label\":\"endometrial\", \"word\":\"endometrial\"},{\"id\":\"w44\",\"fontColor\":\"#008000\" , \"label\":\"bem\", \"word\":\"bem\"},{\"id\":\"w45\",\"fontColor\":\"blue\" , \"label\":\"visibilizado,\", \"word\":\"visibilizado \"},{\"id\":\"w46\",\"fontColor\":\"blue\" , \"label\":\"medindo\", \"word\":\"medindo\"},{\"id\":\"w47\",\"fontColor\":\"blue\" , \"label\":\"14\", \"word\":\"14\"},{\"id\":\"w48\",\"fontColor\":\"#008000\" , \"label\":\"mm\", \"word\":\"mm\"},{\"id\":\"w50\",\"fontColor\":\"#008000\" , \"label\":\"espessura.\", \"word\":\"espessura \"},{\"id\":\"w51\",\"fontColor\":\"blue\" , \"label\":\"Colo\", \"word\":\"colo\"},{\"id\":\"w52\",\"fontColor\":\"blue\" , \"label\":\"uterino\", \"word\":\"uterino\"},{\"id\":\"w54\",\"fontColor\":\"blue\" , \"label\":\"aspecto\", \"word\":\"aspecto\"},{\"id\":\"w55\",\"fontColor\":\"#008000\" , \"label\":\"habitual.\", \"word\":\"habitual \"},{\"id\":\"w56\",\"fontColor\":\"blue\" , \"label\":\"Canal\", \"word\":\"canal\"},{\"id\":\"w57\",\"fontColor\":\"blue\" , \"label\":\"endocervical\", \"word\":\"endocervical\"},{\"id\":\"w58\",\"fontColor\":\"#008000\" , \"label\":\"sem\", \"word\":\"sem\"},{\"id\":\"w59\",\"fontColor\":\"blue\" , \"label\":\"alterações\", \"word\":\"alteracoes\"},{\"id\":\"w60\",\"fontColor\":\"#008000\" , \"label\":\"ecográficas.\", \"word\":\"ecograficas \"},{\"id\":\"w61\",\"fontColor\":\"blue\" , \"label\":\"OVARIO\", \"word\":\"ovario\"},{\"id\":\"w62\",\"fontColor\":\"#008000\" , \"label\":\"DIREITO:\", \"word\":\"direito\"},{\"id\":\"w63\",\"fontColor\":\"#008000\" , \"label\":\"tópico,\", \"word\":\"topico \"},{\"id\":\"w64\",\"fontColor\":\"blue\" , \"label\":\"contornos\", \"word\":\"contornos\"},{\"id\":\"w65\",\"fontColor\":\"#008000\" , \"label\":\"regulares\", \"word\":\"regulares\"},{\"id\":\"w67\",\"fontColor\":\"blue\" , \"label\":\"ecotextura\", \"word\":\"ecotextura\"},{\"id\":\"w68\",\"fontColor\":\"#008000\" , \"label\":\"habitual.\", \"word\":\"habitual \"},{\"id\":\"w69\",\"fontColor\":\"blue\" , \"label\":\"Dimensões:\", \"word\":\"dimensoes\"},{\"id\":\"w70\",\"fontColor\":\"blue\" , \"label\":\"2,7\", \"word\":\"2,7\"},{\"id\":\"w72\",\"fontColor\":\"blue\" , \"label\":\"2,74\", \"word\":\"2,74\"},{\"id\":\"w74\",\"fontColor\":\"blue\" , \"label\":\"2,2\", \"word\":\"2,2\"},{\"id\":\"w75\",\"fontColor\":\"#008000\" , \"label\":\"cm.\", \"word\":\"cm \"},{\"id\":\"w76\",\"fontColor\":\"blue\" , \"label\":\"Volume\", \"word\":\"volume\"},{\"id\":\"w77\",\"fontColor\":\"#008000\" , \"label\":\"estimado:\", \"word\":\"estimado\"},{\"id\":\"w78\",\"fontColor\":\"blue\" , \"label\":\"7,4\", \"word\":\"7,4\"},{\"id\":\"w79\",\"fontColor\":\"#008000\" , \"label\":\"cm³.\", \"word\":\"cm³ \"},{\"id\":\"w80\",\"fontColor\":\"blue\" , \"label\":\"OVÁRIO\", \"word\":\"ovario\"},{\"id\":\"w81\",\"fontColor\":\"#008000\" , \"label\":\"ESQUERDO:\", \"word\":\"esquerdo\"},{\"id\":\"w82\",\"fontColor\":\"#008000\" , \"label\":\"tópico,\", \"word\":\"topico \"},{\"id\":\"w83\",\"fontColor\":\"blue\" , \"label\":\"contornos\", \"word\":\"contornos\"},{\"id\":\"w84\",\"fontColor\":\"#008000\" , \"label\":\"regulares\", \"word\":\"regulares\"},{\"id\":\"w86\",\"fontColor\":\"blue\" , \"label\":\"ecotextura\", \"word\":\"ecotextura\"},{\"id\":\"w87\",\"fontColor\":\"#008000\" , \"label\":\"habitual.\", \"word\":\"habitual \"},{\"id\":\"w88\",\"fontColor\":\"blue\" , \"label\":\"Dimensões:\", \"word\":\"dimensoes\"},{\"id\":\"w89\",\"fontColor\":\"blue\" , \"label\":\"2,7\", \"word\":\"2,7\"},{\"id\":\"w91\",\"fontColor\":\"blue\" , \"label\":\"1,6\", \"word\":\"1,6\"},{\"id\":\"w93\",\"fontColor\":\"blue\" , \"label\":\"1,6\", \"word\":\"1,6\"},{\"id\":\"w94\",\"fontColor\":\"#008000\" , \"label\":\"cm.\", \"word\":\"cm \"},{\"id\":\"w95\",\"fontColor\":\"blue\" , \"label\":\"Volume\", \"word\":\"volume\"},{\"id\":\"w96\",\"fontColor\":\"#008000\" , \"label\":\"estimado:\", \"word\":\"estimado\"},{\"id\":\"w97\",\"fontColor\":\"blue\" , \"label\":\"3,4\", \"word\":\"3,4\"},{\"id\":\"w98\",\"fontColor\":\"#008000\" , \"label\":\"cm³.\", \"word\":\"cm³ \"},{\"id\":\"w99\",\"fontColor\":\"#008000\" , \"label\":\"FUNDO\", \"word\":\"fundo\"},{\"id\":\"w101\",\"fontColor\":\"blue\" , \"label\":\"SACO\", \"word\":\"saco\"},{\"id\":\"w103\",\"fontColor\":\"#008000\" , \"label\":\"DOUGLAS:\", \"word\":\"douglas\"},{\"id\":\"w104\",\"fontColor\":\"blue\" , \"label\":\"Sem\", \"word\":\"sem\"},{\"id\":\"w105\",\"fontColor\":\"blue\" , \"label\":\"coleções\", \"word\":\"colecoes\"},{\"id\":\"w106\",\"fontColor\":\"#008000\" , \"label\":\"anormais.\", \"word\":\"anormais \"},{\"id\":\"w108\",\"fontColor\":\"blue\" , \"label\":\"DIAGNÓSTICA:\", \"word\":\"diagnostica\"},{\"id\":\"w110\",\"fontColor\":\"blue\" , \"label\":\"Útero\", \"word\":\"utero\"},{\"id\":\"w113\",\"fontColor\":\"#008000\" , \"label\":\"aumenrtado\", \"word\":\"aumenrtado\"}], \"edges\":[[\"w1\", \"w0\"],[\"w2\", \"w0\"],[\"w3\", \"w0\"],[\"w4\", \"w3\"],[\"w6\", \"w3\"],[\"w8\", \"w3\"],[\"w11\", \"w3\"],[\"w13\", \"w11\"],[\"w16\", \"w3\"],[\"w18\", \"w16\"],[\"w20\", \"w21\"],[\"w22\", \"w20\"],[\"w25\", \"w23\"],[\"w23\", \"w24\"],[\"w28\", \"w23\"],[\"w29\", \"w3\"],[\"w30\", \"w29\"],[\"w32\", \"w29\"],[\"w34\", \"w29\"],[\"w35\", \"w34\"],[\"w35\", \"w32\"],[\"w35\", \"w30\"],[\"w37\", \"w36\"],[\"w38\", \"w36\"],[\"w36\", \"w3\"],[\"w39\", \"w38\"],[\"w44\", \"w41\"],[\"w44\", \"w45\"],[\"w42\", \"w41\"],[\"w45\", \"w41\"],[\"w46\", \"w41\"],[\"w47\", \"w46\"],[\"w48\", \"w47\"],[\"w50\", \"w41\"],[\"w51\", \"w52\"],[\"w54\", \"w51\"],[\"w55\", \"w54\"],[\"w56\", \"w57\"],[\"w58\", \"w56\"],[\"w58\", \"w59\"],[\"w59\", \"w56\"],[\"w60\", \"w59\"],[\"w61\", \"w0\"],[\"w62\", \"w61\"],[\"w63\", \"w61\"],[\"w64\", \"w61\"],[\"w65\", \"w64\"],[\"w67\", \"w61\"],[\"w68\", \"w67\"],[\"w69\", \"w61\"],[\"w70\", \"w69\"],[\"w72\", \"w69\"],[\"w74\", \"w69\"],[\"w75\", \"w74\"],[\"w75\", \"w72\"],[\"w75\", \"w70\"],[\"w76\", \"w61\"],[\"w77\", \"w76\"],[\"w78\", \"w76\"],[\"w79\", \"w78\"],[\"w81\", \"w80\"],[\"w80\", \"w0\"],[\"w82\", \"w80\"],[\"w83\", \"w80\"],[\"w84\", \"w83\"],[\"w86\", \"w80\"],[\"w87\", \"w86\"],[\"w88\", \"w80\"],[\"w89\", \"w88\"],[\"w93\", \"w88\"],[\"w91\", \"w88\"],[\"w94\", \"w89\"],[\"w94\", \"w93\"],[\"w94\", \"w91\"],[\"w96\", \"w95\"],[\"w95\", \"w80\"],[\"w97\", \"w95\"],[\"w98\", \"w97\"],[\"w99\", \"w101\"],[\"w103\", \"w104\"],[\"w105\", \"w101\"],[\"w104\", \"w105\"],[\"w106\", \"w105\"],[\"w113\", \"w110\"],[\"w110\", \"w108\"]], \"version\":\"0.2\"}");
		//		doc.setGraphDot(source);
		doc.setDocumentContent(con);

		return doc;
	}

	public static DocumentGraph prepareDocumentGraphTest2(){
		DocumentContent con = new DocumentContent();
		con.setContent("ULTRASSONOGRAFIA PÉLVICA TRANSVAGINAL. BEXIGA: Com repleção adequada para o exame. ÚTERO: ausente, conforme histórico cirúrgico prévio. OVÁRIO DIREITO: tópico, contornos regulares e ecotextura habitual. Dimensões: 2,6 x 0,9 x 1,5 cm. Volume estimado: 2,0 cm³. OVÁRIO ESQUERDO: não caracterizado. FUNDO DE SACO DE DOUGLAS: Sem coleções anormais. IMPRESSÃO DIAGNÓSTICA: - Status pós cirúrgico.");

		DocumentGraph doc = new DocumentGraph();
		doc.setId(84);
		doc.setAuthor("4aa1fbbc-1efa-45db-b399-6a25cfe9dcda");
		//		doc.setTimeStamp(timeStamp);
		//		doc.setSuggestions(suggestions);
		//		doc.setGraduationYear(graduationYear);
		//		doc.setResidenceYear(residenceYear);
		//		doc.setSpecialistYear(specialistYear);
		//		doc.setMasterYear(masterYear);
		//		doc.setDoctorYear(doctorYear);
		doc.setActions("1:Analaudos.init(); 2:Analaudos.addText(); 3:selectSource(w1); 5154:unselectSource(w1); 5874:selectSource(w0); 7224:createEdge(w0;w2); 12920:selectSource(w0); 14128:removeEdge(w0;w2); 16249:selectSource(w1); 17263:createEdge(w1;w0); 18729:selectSource(w2); 23316:createEdge(w2;w0); 29339:selectSource(w5); 30379:createEdge(w5;w3); 31401:selectSource(w3); 32831:createEdge(w3;w0); 34339:selectSource(w6); 35527:createEdge(w6;w5); 41178:selectSource(w9); 43248:createEdge(w9;w0); 48427:selectSource(w9); 49592:removeEdge(w9;w0); 50842:selectSource(w9); 51734:createEdge(w9;w2); 54042:selectSource(w10); 55518:createEdge(w10;w0); 57632:selectSource(w11); 58588:createEdge(w11;w10); 61654:selectSource(w12); 66064:unselectSource(w12); 67724:selectSource(w11); 69260:createEdge(w11;w12); 71036:selectSource(w12); 74529:createEdge(w12;w13); 82295:selectSource(w14); 83379:createEdge(w14;w13); 84959:selectSource(w15); 86543:createEdge(w15;w13); 89561:selectSource(w17); 90397:createEdge(w17;w16); 92442:selectSource(w18); 93866:createEdge(w18;w16); 96359:selectSource(w19); 97567:createEdge(w19;w16); 98476:selectSource(w20); 99400:createEdge(w20;w19); 100846:selectSource(w22); 101662:createEdge(w22;w16); 102796:selectSource(w23); 103632:createEdge(w23;w22); 106360:selectSource(w24); 107486:createEdge(w24;w16); 108602:selectSource(w25); 109294:createEdge(w25;w24); 110334:selectSource(w27); 111180:createEdge(w27;w24); 112434:selectSource(w29); 113382:createEdge(w29;w24); 114375:selectSource(w30); 115252:createEdge(w30;w25); 116232:selectSource(w30); 117276:createEdge(w30;w27); 118374:selectSource(w30); 119353:createEdge(w30;w29); 120938:selectSource(w31); 122285:createEdge(w31;w16); 123417:selectSource(w32); 124197:createEdge(w32;w31); 125701:selectSource(w33); 126693:createEdge(w33;w31); 128181:selectSource(w34); 129001:createEdge(w34;w33); 130563:selectSource(w36); 131753:createEdge(w36;w35); 132533:selectSource(w35); 133971:createEdge(w35;w0); 135383:selectSource(w16); 136789:createEdge(w16;w0); 150874:selectSource(w37); 152204:createEdge(w37;w35); 153650:selectSource(w38); 155720:createEdge(w38;w37); 160052:selectSource(w38); 162838:createEdge(w38;w35); 167199:selectSource(w39); 168355:createEdge(w39;w41); 171210:selectSource(w43); 172845:createEdge(w43;w41); 178025:selectSource(w44); 178943:createEdge(w44;w45); 179883:selectSource(w45); 185023:createEdge(w45;w41); 186771:selectSource(w46); 187639:createEdge(w46;w45); 191694:selectSource(w52); 192989:createEdge(w52;w50)");
		doc.setGraphJson("{\"nodes\":[{\"id\":\"w0\",\"fontColor\":\"blue\" , \"label\":\"ULTRASSONOGRAFIA\", \"word\":\"ultrassonografia\"},{\"id\":\"w1\",\"fontColor\":\"#008000\" , \"label\":\"PÉLVICA\", \"word\":\"pelvica\"},{\"id\":\"w2\",\"fontColor\":\"blue\" , \"label\":\"TRANSVAGINAL.\", \"word\":\"transvaginal \"},{\"id\":\"w3\",\"fontColor\":\"blue\" , \"label\":\"BEXIGA:\", \"word\":\"bexiga\"},{\"id\":\"w5\",\"fontColor\":\"blue\" , \"label\":\"repleção\", \"word\":\"replecao\"},{\"id\":\"w6\",\"fontColor\":\"#008000\" , \"label\":\"adequada\", \"word\":\"adequada\"},{\"id\":\"w9\",\"fontColor\":\"#008000\" , \"label\":\"exame.\", \"word\":\"exame \"},{\"id\":\"w10\",\"fontColor\":\"blue\" , \"label\":\"ÚTERO:\", \"word\":\"utero\"},{\"id\":\"w11\",\"fontColor\":\"#008000\" , \"label\":\"ausente,\", \"word\":\"ausente \"},{\"id\":\"w12\",\"fontColor\":\"blue\" , \"label\":\"conforme\", \"word\":\"conforme\"},{\"id\":\"w13\",\"fontColor\":\"blue\" , \"label\":\"histórico\", \"word\":\"historico\"},{\"id\":\"w14\",\"fontColor\":\"#008000\" , \"label\":\"cirúrgico\", \"word\":\"cirurgico\"},{\"id\":\"w15\",\"fontColor\":\"#008000\" , \"label\":\"prévio.\", \"word\":\"previo \"},{\"id\":\"w16\",\"fontColor\":\"blue\" , \"label\":\"OVÁRIO\", \"word\":\"ovario\"},{\"id\":\"w17\",\"fontColor\":\"#008000\" , \"label\":\"DIREITO:\", \"word\":\"direito\"},{\"id\":\"w18\",\"fontColor\":\"#008000\" , \"label\":\"tópico,\", \"word\":\"topico \"},{\"id\":\"w19\",\"fontColor\":\"blue\" , \"label\":\"contornos\", \"word\":\"contornos\"},{\"id\":\"w20\",\"fontColor\":\"#008000\" , \"label\":\"regulares\", \"word\":\"regulares\"},{\"id\":\"w22\",\"fontColor\":\"blue\" , \"label\":\"ecotextura\", \"word\":\"ecotextura\"},{\"id\":\"w23\",\"fontColor\":\"#008000\" , \"label\":\"habitual.\", \"word\":\"habitual \"},{\"id\":\"w24\",\"fontColor\":\"blue\" , \"label\":\"Dimensões:\", \"word\":\"dimensoes\"},{\"id\":\"w25\",\"fontColor\":\"blue\" , \"label\":\"2,6\", \"word\":\"2,6\"},{\"id\":\"w27\",\"fontColor\":\"blue\" , \"label\":\"0,9\", \"word\":\"0,9\"},{\"id\":\"w29\",\"fontColor\":\"blue\" , \"label\":\"1,5\", \"word\":\"1,5\"},{\"id\":\"w30\",\"fontColor\":\"#008000\" , \"label\":\"cm.\", \"word\":\"cm \"},{\"id\":\"w31\",\"fontColor\":\"blue\" , \"label\":\"Volume\", \"word\":\"volume\"},{\"id\":\"w32\",\"fontColor\":\"#008000\" , \"label\":\"estimado:\", \"word\":\"estimado\"},{\"id\":\"w33\",\"fontColor\":\"blue\" , \"label\":\"2,0\", \"word\":\"2,0\"},{\"id\":\"w34\",\"fontColor\":\"#008000\" , \"label\":\"cm³.\", \"word\":\"cm³ \"},{\"id\":\"w35\",\"fontColor\":\"blue\" , \"label\":\"OVÁRIO\", \"word\":\"ovario\"},{\"id\":\"w36\",\"fontColor\":\"#008000\" , \"label\":\"ESQUERDO:\", \"word\":\"esquerdo\"},{\"id\":\"w37\",\"fontColor\":\"blue\" , \"label\":\"não\", \"word\":\"nao\"},{\"id\":\"w38\",\"fontColor\":\"#008000\" , \"label\":\"caracterizado.\", \"word\":\"caracterizado \"},{\"id\":\"w39\",\"fontColor\":\"#008000\" , \"label\":\"FUNDO\", \"word\":\"fundo\"},{\"id\":\"w41\",\"fontColor\":\"blue\" , \"label\":\"SACO\", \"word\":\"saco\"},{\"id\":\"w43\",\"fontColor\":\"#008000\" , \"label\":\"DOUGLAS:\", \"word\":\"douglas\"},{\"id\":\"w44\",\"fontColor\":\"#008000\" , \"label\":\"Sem\", \"word\":\"sem\"},{\"id\":\"w45\",\"fontColor\":\"blue\" , \"label\":\"coleções\", \"word\":\"colecoes\"},{\"id\":\"w46\",\"fontColor\":\"#008000\" , \"label\":\"anormais.\", \"word\":\"anormais \"},{\"id\":\"w50\",\"fontColor\":\"blue\" , \"label\":\"Status\", \"word\":\"status\"},{\"id\":\"w52\",\"fontColor\":\"#008000\" , \"label\":\"cirúrgico.\", \"word\":\"cirurgico \"}], \"edges\":[[\"w1\", \"w0\"],[\"w2\", \"w0\"],[\"w5\", \"w3\"],[\"w3\", \"w0\"],[\"w6\", \"w5\"],[\"w9\", \"w2\"],[\"w10\", \"w0\"],[\"w11\", \"w10\"],[\"w11\", \"w12\"],[\"w12\", \"w13\"],[\"w14\", \"w13\"],[\"w15\", \"w13\"],[\"w17\", \"w16\"],[\"w18\", \"w16\"],[\"w19\", \"w16\"],[\"w20\", \"w19\"],[\"w22\", \"w16\"],[\"w23\", \"w22\"],[\"w24\", \"w16\"],[\"w25\", \"w24\"],[\"w27\", \"w24\"],[\"w29\", \"w24\"],[\"w30\", \"w25\"],[\"w30\", \"w27\"],[\"w30\", \"w29\"],[\"w31\", \"w16\"],[\"w32\", \"w31\"],[\"w33\", \"w31\"],[\"w34\", \"w33\"],[\"w36\", \"w35\"],[\"w35\", \"w0\"],[\"w16\", \"w0\"],[\"w37\", \"w35\"],[\"w38\", \"w37\"],[\"w38\", \"w35\"],[\"w39\", \"w41\"],[\"w43\", \"w41\"],[\"w44\", \"w45\"],[\"w45\", \"w41\"],[\"w46\", \"w45\"],[\"w52\", \"w50\"]], \"version\":\"0.2\"}");
		//		doc.setGraphDot(source);
		doc.setDocumentContent(con);

		return doc;
	}
}
