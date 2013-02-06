function OracleAIPAccounts(result){
	var cabecalho = new Array();
	//alert("Result: " +result.length);
	result = handleNivelSeguranca(result);
	//alert("Result: " +JSON.stringify(result));
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
		//alert("linha: "+linha.length);
		
	//alert(cabecalho);
	alert(relatorio);
	return relatorio;
}

function handleNivelSeguranca(accounts){
	for(var i=0;i<accounts.length;i++){
		var account = accounts[i];
		if(account.errpasnivelseguranca){
			var errpasnivelseguranca = account.errpasnivelseguranca[0].replace(/&#61;/gi,'=');
			//alert("errpasnivelseguranca: "+errpasnivelseguranca);
			var params = {method:'getUnitList',params:{'nivelSeguranca':errpasnivelseguranca}};
			$.ajax({
				url:'/OracleRetailOperationsWS',
				async: false,
				dataType:'json',
				type:'POST',
				data: encodeURIComponent(JSON.stringify(params)),
				success:function(response){
					//alert('Response: '+JSON.stringify(response));
					if(!response.error){
						mercadologica = response.result.Mercadologica;
						operacional = response.result.Operacional;
					}
				}
			});
			account.mercadologica = mercadologica;
			account.operacional = operacional;
			account.errpasnivelseguranca = undefined;
		}
	}
	return accounts;
}