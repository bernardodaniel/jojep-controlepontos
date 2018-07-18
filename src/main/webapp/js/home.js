angular.module('app', ['ngRoute'])
  .config(function($routeProvider, $httpProvider) {
	  
	  $routeProvider.when('/admin', {
	      templateUrl : 'home.html',
	      controller : 'home',
	      controllerAs: 'controller'
	    }).when('/login', {
	      templateUrl : 'login.html',
	      controller : 'navigation',
	      controllerAs: 'controller'
	    }).when('/senha', {
	      templateUrl : 'senha.html',
	      controller : 'senha',
	      controllerAs: 'controller'
	    }).otherwise('/');
	  
	  $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	  
  }) 
  .controller('home', function( $rootScope, $http, $location ) {
	
	  var self = this;
	  
	  $http({
   		method: 'POST',
 		url: '/participantesporuser',
			data: $rootScope.usuario.nome,
	        headers: {
             'Content-Type': 'application/json; charset=UTF-8'
    		}
	  	})
	  	.then(function successCalback(response) {
    		self.participantes = response.data;
    		
    	   	let cidadesRepetidas = self.participantes.map( p => p.cidade );
    	   	let cidadesNomes = cidadesRepetidas.reduce((x, y) => x.includes(y) ? x : [...x, y], []);
    	   	
    	   	self.cidades = cidadesNomes.map((c) => {
    	   		return {
    	   			nome: c,
    	   			selecionado: true
    	   		}
    	   	});
    	   	
    	   	
    	});

    	self.semana = 0;
    	self.semanas = [
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
    	
    	self.exibirColuna = function(diaDaSemana) {
    		return $rootScope.usuario.role != 'admin' && self.semanas[self.semana].some(elem => elem === diaDaSemana );
    	}
    	
    	self.proximaSemanaHabilitada = false;
    	self.proximaSemana = function() {
    		self.semana++;
    	}
    	
    	self.semanaAnterior = function() {
    		self.semana--;
    	}
    	
    	
    	self.save = function () {
    		
    		self.salvoComSucesso = false;
    		self.emProcessamento = true;
  
     	$http({
     		method: 'POST',
     		url: 'participantes',
    			data: angular.toJson(self.participantes),
    	        headers: {
                 'Content-Type': 'application/json; charset=UTF-8'
        		}
     	}).then(function successCalback(response) {
     		
     		 $http({
     	   		method: 'POST',
     	 		url: '/participantesporuser',
     				data: $rootScope.usuario.nome,
     		        headers: {
     	             'Content-Type': 'application/json; charset=UTF-8'
     	    		}
     		  	})
     		  	.then(function successCalback(response) {
     	    		self.participantes = response.data;
     	    		self.salvoComSucesso = true;
     	    		self.emProcessamento = false;
     	    	});
     		 
     	}, function errorCallback(response) {
     		console.log(response);
     	});
  
       }
    	
    	self.logout = function() {
		  $http.post('logout', {}).finally(function() {
		    $rootScope.authenticated = false;
		    $location.path("/login");
		  });
		}
    	
    	
    	self.nomeFiltro = '';
        self.sexoFeminino = true;
        self.sexoMasculino = true;
        self.sexos = ['M', 'F'];
     	
        self.mulheresBtnClick = function() {
      	  let contem = self.sexos.some( function(i) {
     			return i === 'F';
     		});
      		
      		if (contem) {
      			self.sexos.splice(self.sexos.indexOf('F'), 1);
      			self.sexoFeminino = false;
      		} else {
      			self.sexos.push('F');
      			self.sexoFeminino = true;
      		}
      		
      	}
     	
      	self.homensBtnClick = function() {
      		let contem = self.sexos.some( function(i) {
     			return i === 'M';
     		});
      		
      		if (contem) {
      			self.sexos.splice(self.sexos.indexOf('M'), 1);
      			self.sexoMasculino = false;
      		} else {
      			self.sexos.push('M');
      			self.sexoMasculino = true;
      		}
      	}
       	
      	self.filtros = function(participante) {

      		let cidadesSelecionadas = self.cidades.filter(c => c.selecionado);

      		let cidadeCorrespondente = cidadesSelecionadas.some( function(cidade) {
      			return cidade.nome === participante.cidade;
      		});
      		
      		
      		let nomeCorrespondente = self.nomeFiltro == '' || participante.nome.toLowerCase().includes(self.nomeFiltro.toLowerCase());
      		
      		let sexoCorrespondente = self.sexos.some( function(i) {
      			return i === participante.sexo;
      		});
      		
      		return cidadeCorrespondente && nomeCorrespondente && sexoCorrespondente;
      	}
	  
	  
  })
  .controller('navigation', function($rootScope, $http, $location) {
	  
	  var self = this;
	  
	  let authenticate = function(credentials, callback) {

	    let headers = credentials ? {authorization : "Basic "
	        + btoa(credentials.username + ":" + credentials.password)
	    } : {};

	    $http.get('/user', {headers : headers}).then(function(response) {

	      if (response.data.name) {
      	    let role = response.data.authorities[0].authority;
	        $rootScope.authenticated = true;
	        
	        $rootScope.usuario = {};
	        $rootScope.usuario.nome = response.data.name;
	        $rootScope.usuario.isAdmin = role == 'su' || role == 'admin';
	        $rootScope.usuario.role = role;
	        
	      } else {
	        $rootScope.authenticated = false;
	      }
	      callback && callback();
	    }, function(response) {
	      self.error = false;
	      $location.path("/login");
	      $rootScope.authenticated = false;
	      callback && callback();
	    });

	}

	authenticate();
	self.credentials = {};
	
	self.login = function() {
	    authenticate(self.credentials, function() {
	      if ($rootScope.authenticated) {
	        $location.path("/admin");
	        self.error = false;
	      } else {
	        $location.path("/login");
	        self.error = true;
	      }
	    });
	};
	
  })
  .controller('senha', function($rootScope, $http, $location) {
	  
    var self = this;
	  
	self.credentials = {};
	self.credentials.usuario = $rootScope.usuario.nome;
	
	self.salvarSenha = function() {
	  $http({
   	    method: 'POST',
 		url: '/alterarsenha',
			data: angular.toJson(self.credentials),
	        headers: {
             'Content-Type': 'application/json; charset=UTF-8'
    		}
	  	})
	  	.then(function successCalback(response) {
    		self.success = true;
	  		self.error = false;
    		self.mensagem = response.data;
    	}
	  	,
	  	function errorCalback(response) {
    		self.success = false;
	  		self.error = true;
	  		console.log(response);
    	});
	};
	
	self.logout = function() {
	  $http.post('logout', {}).finally(function() {
	    $rootScope.authenticated = false;
	    $location.path("/login");
	  });
	}
	
  });