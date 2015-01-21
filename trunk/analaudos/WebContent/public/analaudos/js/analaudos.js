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

var sourceNodeFontColor = "blue";
var targetNodeFontColor = "#008000";
var orphanNodeFontColor = "#000000";
var nodeFont = "16px Verdana, sans-serif";

var wordsCount = 0;
var gui = null;
function buildGraph(inputText, outputText, outputCanvas){
	/* Clear previous graph */
	var graph = new Springy.Graph();

	var s = inputText.value;
	s = s.toLowerCase();
//	s = s.replace(/[:.()?!]/g, '');
	s = removeAccents(s);
	var words = s.split(' ');

	for(var i in words){
		/* Create a node */
		graph.addNode(new Springy.Node('w'+i, {label:words[i]}));

		/* Link current node to previous node */
//		if(i!=0) graph.newEdge(graph.nodeSet['w'+ (i-1)], graph.nodeSet['w'+i], {color: '#AA0000', label: ''});
	}
	wordsCount = words.length;

	outputText.value = s;

	if(gui == null){
		gui = outputCanvas.springy({graph: graph, nodeSelected: graph.nodeSet['w0']});
		
		var canvas = outputCanvas[0]; 

		/* Resize canvas */
		canvas.width = canvas.parentNode.clientWidth - canvas.offsetLeft;
		canvas.height = 500;
		
		/* Borders and cursor */
		canvas.style.border = "solid 1px #333";
		canvas.style.cursor = "pointer";
	}

	/* Edit edges CONTROLS */
	var prev = gui.getNodeSelected();
gui.dblclick(function(e) {
		prev.data.font = nodeFont;
		prev = gui.getNodeSelected();
		prev.data.font = "bold " + nodeFont;
		gui.renderer.start(); // Force a update
	});
	gui.click(function(e) {
		if(e.shiftKey){
			var now = gui.getNodeSelected();
			if(prev != now){
				/* Check previous edge to add ou remove */
				var edges = graph.getEdges(prev, now);
				if(edges.length > 0) for(var i in edges) graph.removeEdge(edges[i]);
				else graph.newEdge(prev, now, {color: '#00A0B0', label: ''});
				
				/* Check edges in/out to colorize */
				if(prev.id in graph.adjacency){ 
					prev.data.fontColor = sourceNodeFontColor;
				}else{
					prev.data.fontColor = targetNodeFontColor;
				}
				if(now.id in graph.adjacency){
					now.data.fontColor = sourceNodeFontColor;
				}else{
					now.data.fontColor = targetNodeFontColor;
				}
			}
		}
		if(e.ctrlKey){
			prev.data.font = nodeFont;
			prev = gui.getNodeSelected();
			prev.data.font = "bold " + nodeFont;
			gui.renderer.start(); // Force a update
		}
	});
	
	arrange();
}

function activateSource(){
	prev.data.font = nodeFont;
	prev = gui.getNodeSelected();
	prev.data.font = "bold " + nodeFont;
	gui.renderer.start(); // Force a update
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
			dotGraph += sourceId +"->" + targetId + ";";
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
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
	  xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
	       alert(xmlhttp.responseText);
	    }
	  }
	xmlhttp.open("POST","analaudos.php",true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.send("owner=Silva&graph="+dotGraph);
}

