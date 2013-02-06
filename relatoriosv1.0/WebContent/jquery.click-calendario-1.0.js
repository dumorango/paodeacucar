function ultimo_dia(mes,ano){
	if(mes==1||mes==3||mes==5||mes==7||mes==8||mes==10||mes==12) return 31;
	if(mes==4||mes==6||mes==9||mes==11) return 30;
	if(mes==2) if(ano%400==0) return 29;
	if(mes==2) if(ano%100==0) return 28;	
	if(mes==2) if(ano%4==0) return 29;	else return 28; 
}
var nomeMes = new Array("Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro");		
var hoje = new Date();
var diaAtual = hoje.getDate();
var mesAtual = hoje.getMonth()+1;
var anoAtual = hoje.getFullYear();
var dataAtual = diaAtual+'/'+mesAtual+'/'+anoAtual;
var contCalendario = 0;

jQuery.fn.calendario = function(options){
	
	
	var settings = {
		target:'',
		targetDay:'',
		targetMonth:'',
		targetYear:'',
		minDate:'',
		maxDate:'',
		dateDefault: dataAtual,
		left:'0',
		top:'30',
		referencePosition: this,
		closeClick: true
	};
	options = jQuery.extend(settings, options);
	
	arrData = options.dateDefault.split('/');
	
	diaOriginal = parseInt(arrData[0],10);
	mesOriginal = parseInt(arrData[1],10)-1;
	anoOriginal = parseInt(arrData[2],10);
	
	
	
	// VERIFICANDO RANGE
	var diaMinimo = 0; var mesMinimo = 0;var anoMinimo = 0;
	if(options.minDate!=''){
		arrData = options.minDate.split('/');	
		diaMinimo = parseInt(arrData[0],10);
		mesMinimo = parseInt(arrData[1],10)-1;
		anoMinimo = parseInt(arrData[2],10);	
	}
	var diaMaximo = 9999; var mesMaximo = 9999;	var anoMaximo = 9999;
	if(options.maxDate!=''){
		arrData = options.maxDate.split('/');	
		diaMaximo = parseInt(arrData[0],10);
		mesMaximo = parseInt(arrData[1],10)-1;
		anoMaximo = parseInt(arrData[2],10);	
	}	
	
	
	
	this.each(function(){
	
		
		// Verificando se o botao tem id, senão tem, vai atribuir um id pro botão, para evitar de gerar 2 calendarios
		if(jQuery(this).attr('id')==''){
			contCalendario++;
			jQuery(this).attr('id','chamada_cal_'+contCalendario)
		}
		idChamada = jQuery(this).attr('id');
		
		
		var mes = mesOriginal;
		var ano = anoOriginal;	
	
		// determinando id pro calendário
		idCalendario = 'cal_'+idChamada;
		idCalendario = idCalendario.replace('_dia','').replace('_mes','').replace('_ano','');
		
		if(jQuery('#'+idCalendario).size()>0) return false;
		
		
		
		//criando div
		jQuery('body').append('<div class="calendario" id="'+idCalendario+'"><a href="#" class="fechar" title="Fechar">X</a><a href="#" class="bt_controle_mes bt_voltar_mes">&laquo;</a><p class="nome_mes">mês ano</p><a href="#" class="bt_controle_mes bt_avancar_mes">&raquo;</a><ul class="lista_dia"><li class="semana">D</li><li class="semana">S</li><li class="semana">T</li><li class="semana">Q</li><li class="semana">Q</li><li class="semana">S</li><li class="semana">S</li></ul></div>');
		jQuery('#'+idCalendario).append('<input type="hidden" name="calendarioMes" value="'+mes+'"/>');
		jQuery('#'+idCalendario).append('<input type="hidden" name="calendarioAno" value="'+ano+'"/>');		
		
	

		function preencher_calendario(idCalendario){
		
			// colocando ou alterando título do calendário
			var titulo = nomeMes[mes]+" "+ano;
			jQuery('#'+idCalendario+' p.nome_mes').html(titulo);
			
			// Apagando dias do calendário (caso o usuario esteja avancando / voltando o mes)
			jQuery('#'+idCalendario+' ul.lista_dia li.dia_vazio').remove();							
			jQuery('#'+idCalendario+' ul.lista_dia li.dia').remove();
			
			// Obtendo o dia da semana do primeiro dia do mês
			var primeiro = new Date();
			primeiro.setFullYear(ano,mes,1);
			var inicioSemana = primeiro.getDay();
			
			// Preenchendo dias vazios no calendário
			for(i=0;i<inicioSemana;i++){ 
				jQuery('#'+idCalendario+' ul.lista_dia').append("<li class='dia_vazio'>&nbsp;<\/li>"); 
			}
			
			// preenchendo dias do mes
			var fimMes = ultimo_dia(mes+1,ano);
			for(i=1;i<=fimMes;i++){ 
				if( (ano == anoMinimo && mes == mesMinimo && i < diaMinimo ) || (ano == anoMaximo && mes == mesMaximo && i > diaMaximo )  ){
					jQuery('#'+idCalendario+' ul.lista_dia').append("<li class='dia dia_n"+i+"'>"+i+"<\/li>");			
				} else {
					if(options.target!='' || options.targetDay != '' || options.targetMonth != '' || options.targetYear != ''){
						jQuery('#'+idCalendario+' ul.lista_dia').append("<li class='dia dia_n"+i+"'><a href='#'>"+i+"<\/a><\/li>");		
					} else {
						jQuery('#'+idCalendario+' ul.lista_dia').append("<li class='dia dia_n"+i+"'>"+i+"<\/li>");						
					}
				}
			}
			// verificando se a data preenchida é hoje
			if(mes == mesOriginal && ano == anoOriginal){
				jQuery('#'+idCalendario+' ul.lista_dia li.dia_n'+diaOriginal).addClass('default');
			} 			
			
			jQuery('#'+idCalendario+' ul.lista_dia li a').click(function(){
				var dia = jQuery.trim(jQuery(this).html());
				if(dia.length==1)dia = '0'+dia;
				var mes = (1 + parseInt(jQuery.trim(jQuery(this.parentNode.parentNode.parentNode).find('input[name="calendarioMes"]').val()),10)).toString();
				if(mes.length==1)mes = '0'+mes;
				var ano = parseInt(jQuery.trim(jQuery(this.parentNode.parentNode.parentNode).find('input[name="calendarioAno"]').val()),10);					
				
				if(options.target!='' && jQuery(options.target).size()>0){
					var tag = jQuery(options.target).get(0).tagName.toLowerCase();
					if(tag=='input'){
						jQuery(options.target).val(dia+'/'+mes+'/'+ano);
					} else {
						jQuery(options.target).html(dia+'/'+mes+'/'+ano);
					}
				}				
				if(options.targetDay!='' && jQuery(options.targetDay).size()>0){
					var tag = jQuery(options.targetDay).get(0).tagName.toLowerCase();
					if(tag=='input'){
						jQuery(options.targetDay).val(dia);
					} else {
						jQuery(options.targetDay).html(dia);
					}
				}	
				if(options.targetMonth!='' && jQuery(options.targetMonth).size()>0){
					var tag = jQuery(options.targetMonth).get(0).tagName.toLowerCase();
					if(tag=='input'){
						jQuery(options.targetMonth).val(mes);
					} else {
						jQuery(options.targetMonth).html(mes);
					}
				}	
				if(options.targetYear!='' && jQuery(options.targetYear).size()>0){
					var tag = jQuery(options.targetYear).get(0).tagName.toLowerCase();
					if(tag=='input'){
						jQuery(options.targetYear).val(ano);
					} else {
						jQuery(options.targetYear).html(ano);
					}
				}	

				if(options.closeClick)jQuery('#'+idCalendario).remove();
				return false;
			});
			navegacaoCalendario(idCalendario);
		}	
		
		function navegacaoCalendario(idCalendario){

			jQuery('#'+idCalendario+' a.fechar').unbind();
			jQuery('#'+idCalendario+' a.fechar').click(function(){
				jQuery('#'+idCalendario).remove();
				return false;
			});
			
			//alert('ano = '+ano+' / mes = '+mes+'\nanoMinimo = '+anoMinimo+' e mesMinimo '+mesMinimo);
			if(ano == anoMinimo && mes == mesMinimo){
				jQuery('#'+idCalendario+' a.bt_voltar_mes').hide();
			} else {
				jQuery('#'+idCalendario+' a.bt_voltar_mes').show();
				jQuery('#'+idCalendario+' a.bt_voltar_mes').unbind();
				jQuery('#'+idCalendario+' a.bt_voltar_mes').click(function(){			
					mes = parseInt(jQuery('input[name="calendarioMes"]').val(),10);
					ano = parseInt(jQuery('input[name="calendarioAno"]').val(),10);
					mes--;
					if(mes < 0){
						mes = 11;
						ano--;
					}
					jQuery('input[name="calendarioMes"]').val(mes);
					jQuery('input[name="calendarioAno"]').val(ano);
					preencher_calendario(idCalendario);
					return false;
				});				
			}

			if(ano == anoMaximo && mes == mesMaximo){
				jQuery('#'+idCalendario+' a.bt_avancar_mes').hide();
			} else {
				jQuery('#'+idCalendario+' a.bt_avancar_mes').show();
				jQuery('#'+idCalendario+' a.bt_avancar_mes').unbind();
				jQuery('#'+idCalendario+' a.bt_avancar_mes').click(function(){		
					mes = parseInt(jQuery('input[name="calendarioMes"]').val(),10);
					ano = parseInt(jQuery('input[name="calendarioAno"]').val(),10);
					mes++;
					if(mes == 12){
						mes = 0;
						ano++;
					}
					jQuery('input[name="calendarioMes"]').val(mes);
					jQuery('input[name="calendarioAno"]').val(ano);				
					preencher_calendario(idCalendario);
					return false;
				});	
			}
		}	
		
		
		preencher_calendario(idCalendario);
		
		var posicoes = jQuery(options.referencePosition).offset();
		var leftPosition = posicoes.left + parseInt(options.left,10);
		var topPosition = posicoes.top + parseInt(options.top,10);
		
		jQuery('#'+idCalendario).css({
			'left':leftPosition,
			'top':topPosition
		});
		jQuery('#'+idCalendario).show();
		
	});
};

