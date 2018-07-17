  angular.module('app', ['checklist-model']).controller('AppController', function($scope, $http) {
    
	  $scope.username;
	  $scope.password;
	  
	  $scope.login = function() {
		  $http({
	    		method: 'POST',
	    		url: '/oauth/token',
	    		headers : {
	    		      "Content-Type" : 'application/x-www-form-urlencoded',    
	    		      "Authorization": 'Basic YW5ndWxhcjphbmd1bGFyMA=='
	    		},
	    		data: $.param({client_id:"angular",grant_type : 'password', username : $scope.username, password : $scope.password})

	    	}).then(function successCalback(response) {
	    		
	    		$scope.token = response.data.access_token;
	    		
	    	}, function errorCallback(response) {
	    		
	    		alert('falha no login: ' + response.data.error_description)
	    		console.log(response);
	    	});
	  }
    
    load();
    
    function load() {
    	
//    	if (!$scope.token) {
//    		alert('não tem token');
//    	}
    	
    	$http({
    		method: 'GET',
    		url: 'participantes'
    	}).then(function successCalback(response) {
    		$scope.participantes = response.data;
    		
    	   	$scope.cidades = [];
    	   	$scope.cidadesSelecionadas = [];
        	
        	for (var i=0, len = $scope.participantes.length; i < len; i++) {
        	  var cidade = $scope.participantes[i].cidade;
        	  
        	  var contemCidade = $scope.cidades.some(function(c) {
        		  return c === cidade;
        	  });
        	  
        	  if (!contemCidade) {
        	    $scope.cidades.push(cidade);
        	    $scope.cidadesSelecionadas.push(cidade);
        	  }
        	}
    	}, function errorCallback(response) {
    		console.log(response.statusText);
    	});

    }
   	$scope.nomeFiltro = '';
   	$scope.sexoFeminino = true;
   	$scope.sexoMasculino = true;
   	$scope.sexos = ['M', 'F'];
  	
   	$scope.mulheresBtnClick = function() {
   		var contem = $scope.sexos.some( function(i) {
  			return i === 'F';
  		});
   		
   		if (contem) {
   			$scope.sexos.splice($scope.sexos.indexOf('F'), 1);
   			$scope.sexoFeminino = false;
   		} else {
   			$scope.sexos.push('F');
   			$scope.sexoFeminino = true;
   		}
   		
   	}
  	
   	$scope.homensBtnClick = function() {
   		var contem = $scope.sexos.some( function(i) {
  			return i === 'M';
  		});
   		
   		if (contem) {
   			$scope.sexos.splice($scope.sexos.indexOf('M'), 1);
   			$scope.sexoMasculino = false;
   		} else {
   			$scope.sexos.push('M');
   			$scope.sexoMasculino = true;
   		}
   	}
    	
   	$scope.filtros = function(participante) {
   		var cidadeCorrespondente = $scope.cidadesSelecionadas.some( function(i) {
   			return i === participante.cidade;
   		});
   		
   		
   		var nomeCorrespondente = $scope.nomeFiltro == '' || participante.nome.toLowerCase().includes($scope.nomeFiltro.toLowerCase());
   		
   		var sexoCorrespondente = $scope.sexos.some( function(i) {
   			return i === participante.sexo;
   		});
   		
   		return cidadeCorrespondente && nomeCorrespondente && sexoCorrespondente;
   	}
   	
   	$scope.semana = 0;
   	$scope.semanas = [
   		["08/07", "09/07", "10/07", "11/07", "12/07", "13/07", "14/07"],
   		["15/07", "16/07", "17/07", "18/07", "19/07", "20/07", "21/07"],
   		["22/07", "23/07", "24/07", "25/07", "26/07", "27/07", "28/07"],
   		["29/07", "30/07", "31/07", "01/08", "02/08", "03/08", "04/08"],
   		["05/08", "06/08", "07/08", "08/08", "09/08", "10/08", "11/08"],
   		["12/08", "13/08", "14/08", "15/08", "16/08", "17/08", "18/08"],
   		["19/08", "20/08", "21/08", "22/08", "23/08", "24/08", "25/08"],
   		["26/08", "27/08", "28/08", "29/08", "30/08", "31/08", "01/09"],
   		["02/09", "03/09", "04/09"]
   		];
   	
   	$scope.exibirColuna = function(diaDaSemana) {
   		return $scope.semanas[$scope.semana].some(elem => elem === diaDaSemana );
   	}
   	
   	$scope.proximaSemanaHabilitada = false;
   	$scope.proximaSemana = function() {
   		$scope.semana++;
   	}
   	
   	$scope.semanaAnterior = function() {
   		$scope.semana--;
   	}
   	
   	
   	$scope.save = function () {
   		
   		$scope.salvoComSucesso = false;
   		$scope.emProcessamento = true;

   		console.log(angular.toJson($scope.participantes));
   		
    	$http({
    		method: 'POST',
    		url: 'participantes',
   			data: angular.toJson($scope.participantes),
   	        headers: {
                'Content-Type': 'application/json; charset=UTF-8'
       		}
    	}).then(function successCalback(response) {
    		
    		$scope.participantes = response.data;
    		
    		$scope.salvoComSucesso = true;
    		$scope.emProcessamento = false;
    	}, function errorCallback(response) {
    		console.log(response);
    	});

    }

    
  });