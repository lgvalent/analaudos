/**
 * analaudos.js
 *
 * Copyright (c) 2015 Lucio Valentin
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

var sourceNodeFontColor = "#008000";
var targetNodeFontColor = "blue";
var rootNodeFontColor = "red";
var orphanNodeFontColor = "#000000";
var nodeFont = "16px Verdana, sans-serif";

var wordsCount = 0;
var gui = null;
var canvas = null;
var sourceNode = null;

function buildGraph(inputText, outputText, outputCanvas){
	/* Clear sourceNodeious graph */
	var graph = new Springy.Graph();

	var s = inputText.value;
	s = s.toLowerCase();
	s = removeAccents(s);
	s = s.replace(/[:;()\[\]?!]/g, '');
	s = s.replace(/[\.,]\ /g, ' ');
	var words = s.split(' ');

	for(var i in words){
		/* Create a node */
		graph.addNode(new Springy.Node('w'+i, {label:words[i]}));

		/* Link current node to sourceNodeious node */
//		if(i!=0) graph.newEdge(graph.nodeSet['w'+ (i-1)], graph.nodeSet['w'+i], {color: '#AA0000', label: ''});
	}
	wordsCount = words.length;

	outputText.value = s;

	if(gui == null){
		gui = outputCanvas.springy({graph: graph, nodeSelected: graph.nodeSet['w1']});
		selectSource();
		
		canvas = outputCanvas[0]; 

		/* Resize canvas */
		canvas.width = canvas.parentNode.clientWidth - canvas.offsetLeft;
		canvas.height = 500;
		
		/* Borders and cursor */
		canvas.style.border = "solid 1px #333";
		canvas.style.cursor = "pointer";
		
	}

	canvas.ondblclick =function(e) {
		selectSource();
	};
	
	canvas.onclick = function(e) {
		// De-select actual source
		if(gui.getNodeSelected()==sourceNode){
			unselectSource();
		}else if(sourceNode == null)
			selectSource();
		else{
			createEdge(gui.getNodeSelected());
			unselectSource();
		}
	};
	
	arrange();

	/* Activate first selection */
//	sourceNode = gui.getNodeSelected();
//	selectRoot();
}

/* Edit edges CONTROLS */
function selectSource(){
	console.log("selectSource:" + gui.getNodeSelected().data.label);
	/* Unformat actual root node*/
	if(sourceNode != null){
		sourceNode.data.font = nodeFont;
		sourceNode.data.border = false;
	}

	/* Format actual root node*/
	sourceNode = gui.getNodeSelected();
//	sourceNode.data.font = "bold " + nodeFont;
	sourceNode.data.fontColor = rootNodeFontColor;
	sourceNode.data.border = true;
	gui.renderer.start(); // Force a update
}

function unselectSource(){
	console.log("unselectSource:" + gui.getNodeSelected().data.label);

	colorizeEdge(sourceNode);
	sourceNode.data.border = false;
	sourceNode = null;
}

function activateSource(){
	sourceNode.data.font = nodeFont;
	sourceNode = gui.getNodeSelected();
//	sourceNode.data.font = "bold " + nodeFont;
	sourceNode.data.border = true;
	gui.renderer.start(); // Force a update
}

function colorizeEdge(target){
	target.data.fontColor = orphanNodeFontColor;

	if(target.id in gui.graph.adjacency)
		target.data.fontColor = sourceNodeFontColor;
	
	var isTouched = false;
	gui.graph.edges.forEach(function(e) {   // in target edges
			if (e.target.id == target.id) isTouched = true;
	});

	if(isTouched) target.data.fontColor = targetNodeFontColor;
	
}

function createEdge(target){
	console.log("createEdge: s=" + sourceNode.data.label + " t=" + target.data.label);

	if(sourceNode != target){
		/* Check sourceNodeious edge to add ou remove */
		var edges = gui.graph.getEdges(sourceNode, target);
		if(edges.length > 0) for(var i in edges) gui.graph.removeEdge(edges[i]);
		else gui.graph.newEdge(sourceNode, target, {color: '#00A0B0', label: ''});
		
		/* Check edges in/out to colorize */
		colorizeEdge(sourceNode);
		colorizeEdge(target);
		sourceNode.data.border = false;

	}		
}

function arrange(){
	/* First arrange layout*/
	gui.renderer.stop();
	gui.layout.damping = 0.0000000001;
	gui.layout.stiffness = 400;
	gui.layout.repulsion = 400;
//	gui.layout.minEnergyThreshold = 0.00001;

	var x = -1.0;
	var y = -1.0;
	for(var i in gui.layout.nodePoints){
		gui.layout.nodePoints[i].p.x = x;
		gui.layout.nodePoints[i].p.y = y;
		x += 0.6;
		if( x > 2){
			x = -1;
			y += 0.8;
		}
	}
	gui.renderer.start();

}

function removeOrphans(){
	for(var id in gui.graph.nodeSet){
		if(!(id in gui.graph.adjacency)){
			var exists = false;
			for (var x in gui.graph.adjacency) {
				if (id in gui.graph.adjacency[x]) {
					exists = true;
					break;
				}
			}
			if(!exists) gui.graph.removeNode(gui.graph.nodeSet[id]);
		}
	};
}

function addNode(str){
	gui.graph.addNode(new Springy.Node('w'+wordsCount++, {label:str}));
}

/* Return a .dot format 
 */
function createDot(){
	dotGraph = "digraph G {";
	for(var id in gui.graph.nodeSet){
		// Check for orphan nodes
		var orphan = gui.graph.adjacency[id] == undefined; // in source edges
		if(orphan) gui.graph.edges.forEach(function(e) {   // in target edges
			if (e.target.id === id) { orphan = false; }
		});
		if(!orphan){
			var node = gui.graph.nodeSet[id];
			dotGraph += id +"[" + (node.data.fontColor !== undefined? "fontcolor=\"" + node.data.fontColor + "\", ":"") + "label=\"" + node.data.label +"\"];";
		}
	}

	for(var sourceId in gui.graph.adjacency){
		for(var targetId in gui.graph.adjacency[sourceId]){
			dotGraph += targetId + "->" + sourceId + ";";
		}
	}

	dotGraph += "}";

	return dotGraph;
}
/* dotSource: String
 * imgTarget: <img />
 * http://sandbox.kidstrythisathome.com/erdos/
 */
function createImage(dotSource, imgTarget){
	var form = $("<form/>");
    form.append("<textarea name='chl' />");
    form.append("<input name='cht' value='gv'/>");
    // Fill textarea value
    form[0].children[0].value = dotSource;

	var options = form.serialize();
	imgTarget.src = "https://chart.googleapis.com/chart?"+options;
}

/* @deprecated Usando JSF
 * 
 */
function sendGraph()
{
	var xmlhttp;
	if (window.XMLHttpRequest){// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	} else  {// code for IE6, IE5
		  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange=function(){
	  if (xmlhttp.readyState==4 && xmlhttp.status==200){
	       alert(xmlhttp.responseText);
	   }
	 };
	xmlhttp.open("POST","analaudos.php",true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.send("owner=Silva&graph="+dotGraph);
}

