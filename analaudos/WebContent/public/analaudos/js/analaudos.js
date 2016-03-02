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

if("undefined" != typeof analaudos) console.log("Analaudos already defined");
else{
	var Analaudos = function(){
		var a = this;
		
		a.version = "0.1b";
		a.sourceNodeFontColor = "#008000";
		a.targetNodeFontColor = "blue";
		a.rootNodeFontColor = "red";
		a.orphanNodeFontColor = "#000000";
		a.nodeFont = "16px Verdana, sans-serif";
		
		
		a.wordsCount = 0;
		a.gui = null;
		a.canvas = null;
		a.sourceNode = null;
		
		a.isMuted = false;
		a.mute = function(){a.isMuted=true;a.speak("");};
		a.unmute = function(){a.isMuted=false;};
		
		a.actionsHistory = [];
		a.startTime = null; // filled on a.init();
		a.log = function(action, params){
			var args = Array.prototype.slice.call(arguments, 1);
			a.actionsHistory.push(new Date() - a.startTime + ":" + action + "(" + args.join(";")+ ")");
		};

		/* Edit edges CONTROLS */
		a.selectSource = function(){
			a.log("selectSource", a.gui.getNodeSelected().id);
			a.toast("Indique o contexto de <b>" + a.gui.getNodeSelected().data.label + "</b>");
			
			/* Unformat actual root node*/
			if(a.sourceNode != null){
				a.sourceNode.data.font = a.nodeFont;
				a.sourceNode.data.border = false;
			}

			/* Format actual root node*/
			a.sourceNode = a.gui.getNodeSelected();
//			a.sourceNode.data.font = "bold " + nodeFont;
			a.sourceNode.data.fontColor = a.rootNodeFontColor;
			a.sourceNode.data.border = true;
			a.gui.renderer.start(); // Force a update
		};

		a.unselectSource = function(){
			a.log("unselectSource", a.gui.getNodeSelected().id);

			a.colorizeEdge(a.sourceNode);
			a.sourceNode.data.border = false;
			a.sourceNode = null;
		};

		a.colorizeEdge = function(target){
			target.data.fontColor = a.orphanNodeFontColor;

			if(target.id in a.gui.graph.adjacency)
				target.data.fontColor = a.sourceNodeFontColor;
			
			var isTouched = false;
			a.gui.graph.edges.forEach(function(e) {   // in target edges
					if (e.target.id == target.id) isTouched = true;
			});

			if(isTouched) target.data.fontColor = a.targetNodeFontColor;
			
		};

		a.createEdge = function(target){

			if(a.sourceNode != target){
				/* Check sourceNode edges to add ou remove */
				var edges = a.gui.graph.getEdges(a.sourceNode, target);
				if(edges.length > 0){ 
					a.log("removeEdge", a.sourceNode.id, target.id);
					a.toast("Ligação removida");
					for(var i in edges) a.gui.graph.removeEdge(edges[i]);
				}
				else{
					/* Check target edges to avoid cyclic link */
					edges = a.gui.graph.getEdges(target, a.sourceNode);
					if(edges.length > 0){ 
						a.log("cyclicLinkAvoided", a.sourceNode.id, target.id);
						a.toast("Não é permitida ligação cíclica");
					} else {
						a.log("createEdge", a.sourceNode.id, target.id);
						a.toast(target.data.label);
						a.gui.graph.newEdge(a.sourceNode, target, {color: '#00A0B0', label: ''});
					}
				}
				
				/* Check edges in/out to colorize */
				a.colorizeEdge(a.sourceNode);
				a.colorizeEdge(target);
				a.sourceNode.data.border = false;

			}		
		};

		a.arrange = function(){
			/* First arrange layout*/
			a.gui.renderer.stop();

			var x = -1.8;
			var y = -2.0;
			for(var i in a.gui.layout.nodePoints){
				a.gui.layout.nodePoints[i].p.x = x;
				a.gui.layout.nodePoints[i].p.y = y;
				x += 0.7;
				if( x > 2){
					x = -1.8;
					y += 0.6;
				}
			}
			a.gui.renderer.start();

		};

		a.removeOrphans = function(){
			for(var id in a.gui.graph.nodeSet){
				if(!(id in a.gui.graph.adjacency)){
					var exists = false;
					for (var x in a.gui.graph.adjacency) {
						if (id in a.gui.graph.adjacency[x]) {
							exists = true;
							break;
						}
					}
					if(!exists) a.gui.graph.removeNode(a.gui.graph.nodeSet[id]);
				}
			};
		};

		a.addNode = function(str){
			a.gui.graph.addNode(new Springy.Node('w'+a.wordsCount++, {label:str}));
		};

		/* Return a .dot format 
		 */
		a.createDot = function(){
			var dotGraph = "digraph G {";
			for(var id in a.gui.graph.nodeSet){
				// Check for orphan nodes
				var orphan = a.gui.graph.adjacency[id] == undefined; // in source edges
				if(orphan) a.gui.graph.edges.forEach(function(e) {   // in target edges
					if (e.target.id === id) { orphan = false; }
				});
				if(!orphan){
					var node = a.gui.graph.nodeSet[id];
					dotGraph += id +"[" + (node.data.fontColor !== undefined? "fontcolor=\"" + node.data.fontColor + "\", ":"") + "label=\"" + node.data.word +"\"];";
				}
			}

			for(var sourceId in a.gui.graph.adjacency){
				for(var targetId in a.gui.graph.adjacency[sourceId]){
					dotGraph += targetId + "->" + sourceId + ";";
				}
			}

			dotGraph += "}";

			return dotGraph;
		};

		/* Return a .json format 
		 */
		a.createJson = function(){
			var jsonGraph = "{\"graph\":{\"nodes\":[";
			var nodes = [];
			for(var id in a.gui.graph.nodeSet){
				// Check for orphan nodes
				var orphan = a.gui.graph.adjacency[id] == undefined; // in source edges
				if(orphan) a.gui.graph.edges.forEach(function(e) {   // in target edges
					if (e.target.id === id) { orphan = false; }
				});
				if(!orphan){
					var node = a.gui.graph.nodeSet[id];
					nodes.push("{\"id\":\""+id+"\"" + (node.data.fontColor !== undefined? ",\"fontcolor\":\"" + node.data.fontColor + "\" ":"") + ", \"label\":\"" + node.data.label +"\", \"word\":\"" + node.data.word+ "\"}");
				}
			}
			jsonGraph += nodes.join(",") + "], \"links\":[";
			
			var links = [];
			for(var sourceId in a.gui.graph.adjacency){
				for(var targetId in a.gui.graph.adjacency[sourceId]){
					links.push("{\"source\":\""+targetId +"\", \"target\":\"" + sourceId + "\"}");
				}
			}

			jsonGraph += links.join(",") + "]}}";

			return jsonGraph;
		};

		/* dotSource: String
		 * imgTarget: <img />
		 * http://sandbox.kidstrythisathome.com/erdos/
		 */
		a.createImageLocal = function(imgTarget, dotSource){
			var form = $("<form/>");
		    form.append("<textarea name='chl' />");
		    form.append("<input name='cht' value='gv'/>");
		    // Fill textarea value
		    form[0].children[0].value = dotSource ;

			var options = form.serialize();
			imgTarget.src = "https://chart.googleapis.com/chart?"+options;
		};

		a.createImageIFrame = function(targetIFrame, dotSource){
			/*
			 * Uncaught SecurityError: Failed to read the 'contentDocument' property from 'HTMLIFrameElement': 
			 * Sandbox access violation: Blocked a frame at "http://localhost:8080" 
			 * from accessing a frame at "http://localhost:8080".  
			 * The frame being accessed is sandboxed and lacks the "allow-same-origin" flag.
			 */
			var frame = targetIFrame;
			
			var form = document.createElement("form");
			form.action = "https://chart.googleapis.com/chart";
			form.method = "post";
			
			var text = document.createElement("textarea");
			text.namespaceURI = "chl";
			text.value = dotSource;
			form.appendChild(text);
			
			var input = document.createElement("input");
			input.name = "cht";
			input.value = "gv";
			form.appendChild(input);
			
			var btn = document.createElement("input");
			btn.type = "submit";
			btn.value = "Ok";
			form.appendChild(btn);

			frame.contentDocument.body.appendChild(form);
			
//			form.submit();
		};
		
		a.createImagePopup = function(dotSource){
			var iFrame = $("<iframe/>");
			iFrame.append("<form action='https://chart.googleapis.com/chart' method='POST' target='_blank'/>");
			
			var form = $(iFrame[0].getElementsByTagName('form')[0]);
		    form.append("<textarea name='chl' />");
		    form.append("<input name='cht' value='gv'/>");
		    // Fill textarea value
		    form[0].children[0].value = dotSource || a.createDot();

			form[0].submit();
			
		};
		/* @deprecated Usando JSF
		 * 
		 */
		a.sendGraph = function(){
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
		};
		
		a.stemmer = function(word){
			return removeAccents(word);
		};
		
		a.onNodeClick = function() {
			// De-select actual source
			if(a.gui.getNodeSelected()==a.sourceNode){
				a.unselectSource();
			}else if(a.sourceNode == null)
				a.selectSource();
			else{
				a.createEdge(a.gui.getNodeSelected());
				a.unselectSource();
			}
		};

		
		a.updateSize = function(){
			a.gui.renderer.stop();
			
			/* Resize canvas */
			a.canvas.width = a.canvas.parentNode.clientWidth - a.canvas.offsetLeft;
			a.canvas.height = 500;
			
			a.gui.renderer.start();
		};
		
		a.init = function(inputText, outputCanvas){
			a.startTime = new Date();
			a.log("start");
			/* Clear sourceNodes graph */
			var graph = new Springy.Graph();

			var words = inputText.value.split(' ');

			for(var i in words){
				var word = words[i].toLowerCase();
				word = word.replace(/[:;()\[\]?!]/g, '');
				word = word.replace(/[\n\r\.,](\ |$)/g, ' '); // Replace dot and comma by a space, or word separator
				word = a.stemmer(word);
				
				/* Create a node */
				graph.addNode(new Springy.Node('w'+i, {label:words[i], word: word, onclick:a.onNodeClick}));

				/* Link current node to sourceNodeious node */
//				if(i!=0) graph.newEdge(graph.nodeSet['w'+ (i-1)], graph.nodeSet['w'+i], {color: '#AA0000', label: ''});
			}
			a.wordsCount = words.length;

			if(a.gui == null){
				/* Select a start node to avoid null pointer */
				a.gui = outputCanvas.springy({graph: graph, nodeSelected: graph.nodeSet['w1'], damping:0.00000001, stiffness:400, repulsion:400, minEnergyThreshold:0.0001});
				
				a.canvas = outputCanvas[0]; 

				a.updateSize();

				/* Borders and cursor */
				a.canvas.style.border = "solid 1px #333";
				a.canvas.style.cursor = "pointer";
			}

			/* Organize items */
			a.arrange();
			window.setTimeout(a.arrange, 2000); // Refresh first arrange moviment from center

			/* Update graph size while window resizing */
			window.onresize = a.updateSize;
			
			a.selectSource();
			/* Activate first selection */
//			sourceNode = a.gui.getNodeSelected();
//			selectRoot();
		};

		a.toast = function (text){
				$.growl({title:"", message:text, location:"tc"});
				try{
					if(!a.isMuted)a.speak(text);
				}catch(error){
					console.log(error.message);
				}

		};
		
		a.speak = function speak(text, rate){
			/* Remove <TAGs>*/
			text = text.replace(/<(?:.|\n)*?>/gm, '');
			responsiveVoice.speak(text, "Portuguese Female", {rate: (typeof rate=="undefined"?1.5:rate)});
		};

	};
	analaudos = new Analaudos;
};




