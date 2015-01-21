import unicodedata

def remove_accents(input_str):
	uText = unicode(input_str, "UTF-8")
	nText = unicodedata.normalize( "NFKD", uText )
	return nText.encode('ASCII', 'ignore')

laudo = open('laudo.txt').read()
laudo = laudo.replace('.', '')
laudo = remove_accents(laudo)
laudo = laudo.lower()
words = laudo.split()


nodeTemplate = "graph.addNode(new Springy.Node('%s', {label:'%s'}));\n"
out = ''
for word in words:
   out += nodeTemplate % (word, word)

edgeTemplate = "graph.newEdge(graph.nodeSet['%s'], graph.nodeSet['%s'], {color: '#00A0B0', label: ''});\n"
for i in range(0,len(words)-1):
   out += edgeTemplate % (words[i], words[i+1])

print 'Writing index.html file from indexModel.html...'
html =  open('indexModel.html').read()
indexFile = open('index.html', 'w');
indexFile.write(html % (out, laudo))
print 'Finished.'


