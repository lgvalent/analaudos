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
		a.canvas = null;
		a.graph = new Springy.Graph();
		
		a.version = "0.1b";
		a.sourceNodeFontColor = "#008000";
		a.targetNodeFontColor = "blue";
		a.rootNodeFontColor = "red";
		a.orphanNodeFontColor = "#000000";
		a.nodeFont = "16px Verdana, sans-serif";
		a.edgeColor = "#00A0B0";
		
		
		a.gui = null;
		a.sourceNode = null;
		
		a.isMuted = false;
		a.mute = function(){a.isMuted=true;a.speak("");};
		a.unmute = function(){a.isMuted=false;};
		
		a.actionsHistory = [];
		a.startTime = null; // filled on a.init();
		a.log = function(action, params){
			var args = Array.prototype.slice.call(arguments, 1);
			a.actionsHistory.push(new Date() - a.startTime + ":" + action + "(" + args.join(",")+ ")");
		};

		/* Edit edges CONTROLS */
		a.selectSource = function(){
			a.log("selectSource", a.gui.getNodeSelected().id);
			//a.toast("Indique o contexto de <b>" + a.gui.getNodeSelected().data.label + "</b>");
			
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

			if(target.id in a.graph.adjacency)
				target.data.fontColor = a.sourceNodeFontColor;
			
			var isTouched = false;
			a.graph.edges.forEach(function(e) {   // in target edges
					if (e.target.id == target.id) isTouched = true;
			});

			if(isTouched) target.data.fontColor = a.targetNodeFontColor;
			
		};

		a.createEdge = function(target){
			if(a.sourceNode != target){
				/* Check sourceNode edges to add ou remove */
				var edges = a.graph.getEdges(a.sourceNode, target);
				if(edges.length > 0){ 
					a.log("removeEdge", a.sourceNode.id, target.id);
					// a.toast("LigaÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â§ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â£o removida");
					for(var i in edges) a.graph.removeEdge(edges[i]);
				}
				else{
					/* Check target edges to avoid cyclic link */
					edges = a.graph.getEdges(target, a.sourceNode);
					if(edges.length > 0){ 
						a.log("cyclicLinkAvoided", a.sourceNode.id, target.id);
						a.toast("NÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â£o ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â© permitida ligaÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â§ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â£o cÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â­clica");
					} else {
						a.log("createEdge", a.sourceNode.id, target.id);
						// a.toast(target.data.label);
						a.addEdge(a.sourceNode, target);
					}
				}
				
				/* Check edges in/out to colorize */
				a.colorizeEdge(a.sourceNode);
				a.colorizeEdge(target);
				a.sourceNode.data.border = false;
				a.sourceNode = null;

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
			for(var id in a.graph.nodeSet){
				if(!(id in a.graph.adjacency)){
					var exists = false;
					for (var x in a.graph.adjacency) {
						if (id in a.graph.adjacency[x]) {
							exists = true;
							break;
						}
					}
					if(!exists) a.graph.removeNode(a.graph.nodeSet[id]);
				} 
			};
		};

		a.addNode = function(id, word, label, fontColor){
			if("undefined" == typeof word)
				word = id;
			if("undefined" == typeof label)
				label = word;
			return a.graph.addNode(new Springy.Node(id, {label:label, word: word, fontColor: fontColor, onclick:a.onNodeClick}));
		};
		
		a.clearNodes = function(){
			var tmpNodes = a.graph.nodes.slice();
			tmpNodes.forEach(function(e){this.removeNode(e);}, a.graph);
			// reset counters
			loadJSONCount = -1;
			wordsCount = 0;


		};

		/* edge.data{color, label, weight, font, directional}*/
		a.addEdge = function(src, trg){
			return a.graph.newEdge(src, trg, {color: a.edgeColor, label: ''});
		};
		
		
		a.reverseEdges = function(){
			var tmpEdges = a.graph.edges.slice();
			tmpEdges.forEach(function(e) {
				this.removeEdge(e);
					a.addEdge(e.target, e.source);
			}, a.graph);
		};
		
		a.joinNodes = function(){
			var tmpNodes1 = a.graph.nodes.slice();
			var tmpNodes2 = a.graph.nodes.slice();
			tmpNodes1.forEach(function(n1) {
				tmpNodes2.forEach(function(n2){
					if(n1.id != n2.id && n1.data.word == n2.data.word && n1.id in this.nodeSet){
						var tempEdges = a.graph.edges.slice();
						for(var i in tempEdges){
							/* Remove adjacents */
							if(tempEdges[i].source.id == n2.id){
								a.addEdge(n1, tempEdges[i].target);
								a.graph.removeEdge(tempEdges[i]);
							}
							/* Remove incidents */
							if(tempEdges[i].target.id == n2.id){
								a.addEdge(tempEdges[i].source, n1);
								a.graph.removeEdge(tempEdges[i]);
							}
						}
						a.graph.removeNode(n2);
					}
				}, this);
			}, a.graph);
		};
		
		/* Return a .dot format 
		 */
		a.createDot = function(){
			var dotGraph = "digraph G {";
			for(var id in a.graph.nodeSet){
				var node = a.graph.nodeSet[id];
				dotGraph += id +"[" + (node.data.fontColor? "fontcolor=\"" + node.data.fontColor + "\", ":"") + (node.data.color? "color=\"" + node.data.color + "\", ":"") + "label=\"" + node.data.word +"\"];";
			}

			for(var i in a.graph.edges){
				var edge = a.graph.edges[i];
				dotGraph +=  edge.source.id + "->" + edge.target.id + "[" + (edge.data.fontColor? "fontcolor=\"" + node.data.fontColor + "\", ":"") + (edge.data.color? "color=\"" + edge.data.color + "\", ":"") + (edge.data.label? "label=\"" + edge.data.label + "\"":"") + "];";
			}

			dotGraph += "}";

			return dotGraph;
		};

		/* Return a .json format 
		 * { "nodes": [{"id":"w0","fontcolor":"blue" , "label":"ULTRASSONOGRAFIA", "word":"ultrassonografia"}, ...],
		 *   "edges":[
		 *   			["w0", "w1"],["w0", "w2"],...
		 *   		 ],
		 *   "version":"0.1"
		 * }
		 */
		a.createJson = function(){
			var jsonGraph = "{\"nodes\":[";
			var nodes = [];
			for(var id in a.graph.nodeSet){
				var node = a.graph.nodeSet[id];
				nodes.push("{\"id\":\""+id+"\"" + (node.data.fontColor !== undefined? ",\"fontColor\":\"" + node.data.fontColor + "\" ":"") + ", \"label\":\"" + node.data.label +"\", \"word\":\"" + node.data.word+ "\"}");
			}
			jsonGraph += nodes.join(",") + "], \"edges\":[";
			
			var links = [];
			for(var sourceId in a.graph.adjacency){
				for(var targetId in a.graph.adjacency[sourceId]){
					links.push("[\""+ sourceId +"\", \"" + targetId + "\"]");
				}
			}

			jsonGraph += links.join(",") + "], \"version\":\"0.2\"}";

			return jsonGraph;
		};

		/* Return a .json format 
		 */
		var loadJSONCount = -1;
		a.loadJson = function(json){
			loadJSONCount++;
			// parse if a string is passed (EC5+ browsers)
			if (typeof json == 'string' || json instanceof String) {
				json = JSON.parse( json );
			}

			if ('nodes' in json || 'edges' in json) {
				var nodes = json['nodes']; 
				for(i in nodes)
					a.addNode(nodes[i].id+loadJSONCount, nodes[i].word, nodes[i].label, nodes[i].fontColor);
				
				var edges = json['edges']; 
				for(i in edges){
					var src = a.graph.nodeSet[edges[i][0]+loadJSONCount];
					var trg = a.graph.nodeSet[edges[i][1]+loadJSONCount];
					a.addEdge(src, trg);
				}
			}
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

			var image = Viz(dotSource, { format: "png-image-element" });
   			
   			window.setTimeout(function (){window.open(image.src);}, 10);
			/*
			var iFrame = $("<iframe/>");
			iFrame.append("<form action='https://chart.googleapis.com/chart' method='POST' target='_blank'/>");
			
			var form = $(iFrame[0].getElementsByTagName('form')[0]);
		    form.append("<textarea name='chl' />");
		    form.append("<input name='cht' value='gv'/>");
		    // Fill textarea value
		    form[0].children[0].value = dotSource || a.createDot();

			form[0].submit();
			
			*/
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
			}
		};

		
		a.updateSize = function(){
			a.gui.renderer.stop();
			
			/* Resize canvas */
			a.canvas.width = a.canvas.parentNode.clientWidth - a.canvas.offsetLeft;
			//a.canvas.height = 550;
			
			a.gui.renderer.start();
		};
		
		var wordsCount = 0;
		a.addText = function(text){
			a.log("Analaudos.addText");

			var words = text.split(' ');

			for(var i in words){
				var word = words[i].toLowerCase();
				word = word.replace(/[:;()\[\]?!]/g, '');
				word = word.replace(/[\n\r\.,](\ |$)/g, ' '); // Replace dot and comma by a space, or word separator
				word = word.trim();
				word = a.stemmer(word);
				
				/* Create a node */
				a.addNode('w'+wordsCount++, word, words[i]);

				/* Link current node to sourceNodeious node */
//				if(i!=0) graph.newEdge(graph.nodeSet['w'+ (i-1)], graph.nodeSet['w'+i], {color: '#AA0000', label: ''});
			}
			
			/* Organize items */
			a.arrange();
			window.setTimeout(a.arrange, 2000); // Refresh first arrange moviment from center

			/* Activate first selection */
			a.gui.setNodeSelected(a.graph.nodes[1]);
			a.selectSource();
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

		a.findNodeByWord = function ( word ){
			for(var i in a.graph.nodes){
				if(a.graph.nodes[i].data.word == word)
				  return a.graph.nodes[i];
			}
		};
		
		var DeltaOperation = function(name, value){
			this.name = name; //n-oop, i-nsert , d-elete, r-eplace 
			this.value = value;
		};
		
		var DeltaResult = function(cost){
			this.cost = cost;
			this.operations = [];
			
			this.insertCount = function(){var result = 0; for(var i in this.operations) if(this.operations[i].name=='i') result++; return result;};
			this.deleteCount = function(){var result = 0; for(var i in this.operations) if(this.operations[i].name=='d') result++; return result;};
			this.replaceCount = function(){var result = 0; for(var i in this.operations) if(this.operations[i].name=='r') result++; return result;};
			this.noopCount = function(){var result = 0; for(var i in this.operations) if(this.operations[i].name=='n') result++; return result;};
		};
		
		a.deltaHighlight = function( deltaResult, sameIds){
			if(sameIds == undefined) sameIds = false;
			for(var i in deltaResult.operations){
				var operation = deltaResult.operations[i];
				if(operation.name == "n"){
					var edgeData = operation.value.data;
					edgeData.label += i;
				} else if(operation.name == "d"){
					var edgeData = operation.value.data;
					edgeData.color ="red";
					edgeData.label += i+"(-)";
				} else if(operation.name == "i"){
					var edge = operation.value;
					var source = sameIds?a.graph.nodeSet[edge.source.id]:a.findNodeByWord(edge.source.data.word);
					var target = sameIds?a.graph.nodeSet[edge.target.id]:a.findNodeByWord(edge.target.data.word);
				
					if(!source)
						source = a.addNode(edge.source.id + 'delta', edge.source.data.word, edge.source.data.label, edge.source.data.color);

					if(!target)
						target = a.addNode(edge.target.id + 'delta', edge.target.data.word, edge.target.data.label, edge.target.data.color);

					edge = a.addEdge(source, target);
					edge.data.color = "lime";
					edge.data.label += i+"(+)";
				} else if(operation.name == "r"){
					var edge1 = operation.value[0];
					edge1.data.color ="red4";
					edge1.data.label += i+"<->";
				
					var edge2 = operation.value[1];
					var source = a.graph.nodeSet[edge2.source.id];
					var target = a.graph.nodeSet[edge2.target.id];
				
					if(!source)
						source = a.addNode(source.id + 'delta', source.data.word, source.data.label, source.data.color);

					if(!target)
						target = a.addNode(target.id + 'delta', target.data.word, target.data.label, target.data.color);

					edge = a.addEdge(source, target);
					edge.data.color = "green";
					edge.data.label += i+"<+>";
				}
			}
		};

		// Levenshtein distance: http://dl.acm.org/citation.cfm?id=2815792
		a.levenshteinDistance = function( a2 , sameIds){
			if(sameIds == undefined) sameIds = false;
			var len0 = a.graph.edges.length;
			var len1 = a2.graph.edges.length;
			var v1 = new Array();
			var v2 = new Array();
			var Cost = function(value, operation, previousCost){
				this.cost = value;
				this.operation = operation;
				this.previous = previousCost;
			};
			
			v1[0] = new Cost(0);
			for(var i = 1; i <= len0; i++){
				var pair1 = a.graph.edges[i-1];
				v1[i] = new Cost(i, new DeltaOperation('i', pair1), v1[i-1]);
			}

			for(var j = 1; j <= len1; j++){
				var pair2 = a2.graph.edges[j-1];
				v2[0] = new Cost(j, new DeltaOperation('i', pair2), v1[0]);
				for(var i = 1; i <= len0; i++){
					var pair1 = a.graph.edges[i-1];
					var cost = new Cost(0, new DeltaOperation('n', pair1));
					if( ((!sameIds) && (pair1.source.data.word != pair2.source.data.word || pair1.target.data.word != pair2.target.data.word))
					   || ((sameIds) && (pair1.source.id != pair2.source.id || pair1.target.id != pair2.target.id))){
						cost = new Cost(1, new DeltaOperation('r', [pair1, pair2]));
					}
					var replaceCost = new Cost(v1[i-1].cost + cost.cost, cost.operation, v1[i-1]);
					var insertCost = new Cost(v1[i].cost + 1, new DeltaOperation('i', pair2), v1[i]);
					var deleteCost = new Cost(v2[i-1].cost + 1, new DeltaOperation('d', pair1), v2[i-1]);
					//console.log("cost:" + cost +" ,rep:" + replaceCost + ", ins:" + insertCost + ", del:" + deleteCost);
					if(replaceCost.cost <= insertCost.cost && replaceCost.cost <= deleteCost.cost){
						v2[i] = replaceCost;
					}else
					if(insertCost.cost <= replaceCost.cost && insertCost.cost <= deleteCost.cost){
						v2[i] = insertCost;
					}else
					if(deleteCost.cost <= insertCost.cost && deleteCost.cost <= replaceCost.cost){
						v2[i] = deleteCost;
					}
				}
				//console.log(v2.toString());
				var t = v1;
				v1 = v2;
				v2 = t;
			}

			var cost = v1[len0];
			var result = new DeltaResult(cost.cost);
			while(cost.previous){
				//console.log(op);	
				result.operations.push(cost.operation);

				cost = cost.previous;
			}

			return result;
		};
		
		// Inserts and Removes distances
		a.delta = function( a2, sameIds){
			if(sameIds == undefined) sameIds = false;
			
			var len0 = a.graph.edges.length;
			var len1 = a2.graph.edges.length;
			var result = new DeltaResult(0);
			for(var i = 0; i < len0; i++){
				var pair1 = a.graph.edges[i];
				var found = false;

				for(var j = 0; j < len1; j++){
					var pair2 = a2.graph.edges[j];
					if( ((!sameIds) && (pair1.source.data.word == pair2.source.data.word && pair1.target.data.word == pair2.target.data.word))
					   || ((sameIds) && (pair1.source.id == pair2.source.id && pair1.target.id == pair2.target.id))){
						found = true;
						break;
					}
				}

				if(!found) result.operations.push(new DeltaOperation('d', pair1));
			}
			for(var i = 0; i < len1; i++){
				var pair1 = a2.graph.edges[i];
				var found = false;

				for(var j = 0; j < len0; j++){
					var pair2 = a.graph.edges[j];
					if( ((!sameIds) && (pair1.source.data.word == pair2.source.data.word && pair1.target.data.word == pair2.target.data.word))
					   || ((sameIds) && (pair1.source.id == pair2.source.id && pair1.target.id == pair2.target.id))){
						found = true;
						break;
					}
				}

				if(!found) result.operations.push(new DeltaOperation('i', pair1));
			}

			result.cost = result.operations.length;

			return result;
		};

		a.initGui = function ( outputCanvas ){
			a.startTime = new Date();
			a.log("Analaudos.initGui");
			
			if(a.gui == null){
				a.canvas = outputCanvas;
				
				/* Select a start node to avoid null pointer */
				a.gui = a.canvas.springy({graph: a.graph, damping:0.00000001, stiffness:400, repulsion:400, minEnergyThreshold:0.0001});
				
				a.canvas = a.canvas[0]; 

				a.updateSize();

				/* Borders and cursor */
				a.canvas.style.border = "solid 1px #333";
				a.canvas.style.cursor = "pointer";
			}

			/* Update graph size while window resizing */
			window.onresize = a.updateSize;
		}
	};
};

Math.sum = function( array ){
	var s = 0;
	for(var i = 0, v; v = array[i]; i++){
		s += v;
	}
	return s;
};

Math.mean = function( array ){
	return Math.sum(array) / array.length;
};

var pool;
function analyzeGraphs(outMatrix, outGraphDot, normalizeValues){
	if(normalizeValues == undefined) normalizeValues = false;

	pool = new Array();
	var lines = document.getElementById('form:sourceText').value.split('\n');
	for(var i = 0, line; line = lines[i]; i++){
		var a = new Analaudos();
		a.loadJson(line);
		pool.push(a);
	}
	var vEdgedLength = new Array(pool.length); 
	var mLevenshtein = new Array(pool.length); 
	var mDeltaInserts = new Array(pool.length); 
	var mDeltaRemoves = new Array(pool.length);
	for(var i = 0, gi; gi = pool[i]; i++){
		vEdgedLength[i] = gi.graph.edges.length;
		mLevenshtein[i] = new Array(pool.length);
		mDeltaInserts[i] = new Array(pool.length);
		mDeltaRemoves[i] = new Array(pool.length);
		for(var j = 0, gj; gj = pool[j]; j++){
			var delta = gi.levenshteinDistance(gj, true);
			if(normalizeValues){
				mLevenshtein[i][j] = (delta.insertCount() + delta.deleteCount() + 2*delta.replaceCount()) / vEdgedLength[i];
			}else
				mLevenshtein[i][j] = delta.cost;

			delta =  gi.delta(gj, true);
			mDeltaInserts[i][j] = delta.insertCount()/ (normalizeValues?vEdgedLength[i]:1) ;
			mDeltaRemoves[i][j] = delta.deleteCount()/ (normalizeValues?vEdgedLength[i]:1) ;
			//console.log("g"+i +":g"+j+"=" + matrix[i][j]);
		}
	}

	var strBuffer = [], strCount = 0;

	strBuffer[strCount++] = "Graphs edges length:";
	strBuffer[strCount++] = vEdgedLength.toString();
	
	strBuffer[strCount++] = "Levenshtein Distance Matriz:";
	for(var i in mLevenshtein) strBuffer[strCount++] = mLevenshtein[i].toString();

	strBuffer[strCount++] = "Delta Inserts Matriz:";
	for(var i in mDeltaInserts) strBuffer[strCount++] = mDeltaInserts[i].toString();

	strBuffer[strCount++] = "Delta Removes Matriz:";
	for(var i in mDeltaRemoves) strBuffer[strCount++] = mDeltaRemoves[i].toString();
	
	outMatrix.value = strBuffer.join("\n");
	
	// Gernerate Graph
	var strBuffer = [], strCount = 0;
	strBuffer[strCount++] = 'digraph {node[style="filled", fontname="bold", fillcolor="gray90"];edge[color="gray30", fontname="bold", arrowhead="vee"];';
	
	for(var i=0; i < mLevenshtein.length; i++)
		for(var j=i+1; j < mLevenshtein[i].length; j++)
			strBuffer[strCount++] = i + '->' + j + '[len='+ (mLevenshtein[i][j]/10)+', label="' + mLevenshtein[i][j] +'"];';
	strBuffer[strCount++] = '}';
	outGraphDot.value = strBuffer.join("");

}

function highlightDelta(){
	analaudos1.clearNodes();
 	analaudos1.loadJson(document.getElementById("form:sourceText").value.split("\n")[0])
	
	analaudos2.clearNodes();
	analaudos2.loadJson(document.getElementById("form:sourceText").value.split("\n")[1])

	var d = analaudos1.levenshteinDistance(analaudos2, false);

	document.getElementById('areaActions').value = analaudos1.createDot();
	analaudos1.deltaHighlight(d);
	document.getElementById('areaGraphDot').value = analaudos1.createDot();
	document.getElementById('areaGraphJson').value = analaudos2.createDot();
}