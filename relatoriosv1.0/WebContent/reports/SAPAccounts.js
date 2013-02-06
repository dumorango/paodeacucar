function SAPAccounts(result){
	var cabecalho = new Array();
	for(var i=0;i<result.length;i++){
		var account = result[i]; 
		for(k in account){
			if(cabecalho.indexOf(k)<0){
				cabecalho.push(k);
			}
		}
	}
	//alert("Cabecalho: "+cabecalho.length);
	var relatorio = new Array();
	relatorio.push(cabecalho);
	for(var k=0;k<result.length;k++){
		var account = result[k];
		var linha = new Array();
		$.each(cabecalho,function(i,att){
			linha.push(account[att]);
			});
		relatorio.push(linha);
	}
	alert(JSON.stringify(relatorio));
	return relatorio;
}

