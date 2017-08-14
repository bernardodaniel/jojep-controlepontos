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
	    }).otherwise('/');
	  
	  $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	  
  }) 
  .controller('home', function( $rootScope, $http, $location ) {
	
	  var self = this;
	  self.isAdmin = $rootScope.usuario.nome == 'admin';
	  
	  $http({
   		method: self.isAdmin ? 'GET' : 'POST',
 		url: self.isAdmin ? '/participantes' : '/participantesporcidade',
			data: self.isAdmin ? [] : $rootScope.usuario.cidade,
	        headers: {
             'Content-Type': 'application/json; charset=UTF-8'
    		}
	  	})
	  	.then(function successCalback(response) {
    		self.participantes = response.data;
    		
    	   	self.cidades = [];
    	   	self.cidadesSelecionadas = [];
        	
        	for (var i=0, len = self.participantes.length; i < len; i++) {
        	  var cidade = self.participantes[i].cidade;
        	  
        	  var contemCidade = self.cidades.some(function(c) {
        		  return c === cidade;
        	  });
        	  
        	  if (!contemCidade) {
        	    self.cidades.push(cidade);
        	    self.cidadesSelecionadas.push(cidade);
        	  }
        	}
    	});
	  
	  self.nomeFiltro = '';
      self.sexoFeminino = true;
      self.sexoMasculino = true;
      self.sexos = ['M', 'F'];
   	
      self.mulheresBtnClick = function() {
    	 debugger;
    	 
    		var contem = self.sexos.some( function(i) {
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
    		var contem = self.sexos.some( function(i) {
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
    		debugger;
    		
    		var cidadeCorrespondente = self.cidadesSelecionadas.some( function(i) {
    			return i === participante.cidade;
    		});
    		
    		
    		var nomeCorrespondente = self.nomeFiltro == '' || participante.nome.toLowerCase().includes(self.nomeFiltro.toLowerCase());
    		
    		var sexoCorrespondente = self.sexos.some( function(i) {
    			return i === participante.sexo;
    		});
    		
    		return cidadeCorrespondente && nomeCorrespondente && sexoCorrespondente;
    	}
    	
    	self.semana = 0;
    	self.semanas = [
    		["30/07", "31/07", "01/08", "02/08", "03/08", "04/08", "05/08"],
    		["06/08", "07/08", "09/08", "10/08", "11/08", "12/08", "13/08"],
    		["14/08", "15/08", "16/08", "17/08", "18/08", "19/08", "20/08"],
    		["21/08", "22/08", "23/08", "24/08", "25/08", "26/08", "27/08"],
    		["28/08", "29/08", "30/08", "31/08", "01/09", "02/09", "03/09"],
    		["04/09", "05/09", "06/09", "07/09", "08/09", "09/09", "10/09"],
    		["11/09", "12/09", "13/09", "14/09", "15/09", "16/09", "17/09"],
    		["18/09", "19/09", "20/09", "21/09", "22/09", "23/09", "24/09"],
    		["25/09", "26/09", "27/09", "28/09", "29/09", "30/09", "01/10"]
    		];
    	
    	self.exibirColuna = function(diaDaSemana) {
    		return self.semanas[self.semana].some(elem => elem === diaDaSemana );
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
  
    		console.log(angular.toJson(self.participantes));
    		
     	$http({
     		method: 'POST',
     		url: 'participantes',
    			data: angular.toJson(self.participantes),
    	        headers: {
                 'Content-Type': 'application/json; charset=UTF-8'
        		}
     	}).then(function successCalback(response) {
     		
     		self.participantes = response.data;
     		
     		self.salvoComSucesso = true;
     		self.emProcessamento = false;
     	}, function errorCallback(response) {
     		console.log(response);
     	});
  
       }
    	
    	self.logout = function() {
		  $http.post('logout', {}).finally(function() {
			  
			  debugger;
			  
		    $rootScope.authenticated = false;
		    $location.path("/login");
		  });
		}
	  
	  
  })
  .controller('navigation', function($rootScope, $http, $location) {
	  
	  var self = this;
	  
	  var authenticate = function(credentials, callback) {

	    var headers = credentials ? {authorization : "Basic "
	        + btoa(credentials.username + ":" + credentials.password)
	    } : {};

	    $http.get('/user', {headers : headers}).then(function(response) {

	      if (response.data.name) {
	        $rootScope.authenticated = true;
	        
	        $rootScope.usuario = {};
	        $rootScope.usuario.nome = response.data.name;
	        $rootScope.usuario.cidade = response.data.authorities[0].authority.substring(5);
	        
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
	
  });